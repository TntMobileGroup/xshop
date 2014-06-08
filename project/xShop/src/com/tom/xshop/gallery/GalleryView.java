package com.tom.xshop.gallery;

import java.util.ArrayList;


import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


import com.tom.xshop.MainActivity;
import com.tom.xshop.R;
import com.tom.xshop.cloud.CloudAPIAsyncTask;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsDataChangeListener;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.ui.thirdparty.ViewBadger.BadgeView;
import com.tom.xshop.util.CacheUtil;
import com.tom.xshop.util.JSONUtil;
import com.tom.xshop.util.LayoutUtil;





import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.RelativeLayout;


public class GalleryView extends RelativeLayout implements StickyListHeadersListView.OnStickyHeaderOffsetChangedListener{
    private ArrayList<String> mCategories = null;
    private RelativeLayout mBanner = null;
    private Button mBannerBtn = null;
    private BadgeView mBadge = null;
    
    private StickyListHeadersListView stickyList;
    private StickyListViewAdapter mAdapter;
    
    
    public GalleryView(Context context) {
        super(context);
        createUI();
        addListeners();
        loadFromCache();
    }
    
    public void scrollToCategory(String category, boolean updateFocus)
    {
    	if(mCategories == null)
    		 mCategories = GlobalData.getData().getAllCategories();
    	
        int count = mCategories.size();
        int selection = 0;
        for (int i = 0; i < count - 1; ++i)
        {
            if (mCategories.get(i).equalsIgnoreCase(category))
            {
            	// update selection
            	if(i == 0) {
            		selection = 0;
            	} 
            	
            	stickyList.setSelection(selection);
            	
                return;
            } else {
        		ArrayList<GoodsItem> list = GlobalData.getData().getGoodsListByCategory(mCategories.get(i));
           	     selection += list.size(); 
            }
        }
        
        if (updateFocus)
        {
        	mAdapter.notifyDataSetChanged();
        }
    }
  
    
    public View getBanner() {
    	return mBannerBtn;
    }
    
    
    private void createBottomBanner() {
        Context context = this.getContext();
        mBanner = new RelativeLayout(context);
        RelativeLayout.LayoutParams topViewLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                LayoutUtil.getGalleryTopPanelHeight());
        topViewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        this.addView(mBanner, topViewLP);
        
        mBannerBtn = new Button(context);
        mBannerBtn.setText(R.string.gouwuche);
        mBannerBtn.setBackgroundResource(R.drawable.wood_bk);
        mBannerBtn.setAlpha(0.8f);
        RelativeLayout.LayoutParams toggleNaviBtnLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        
        mBanner.addView(mBannerBtn, toggleNaviBtnLP);
        mBadge = new BadgeView(context, mBannerBtn);
        mBadge.setText("0");
      
    }

    private void createUI()
    {
    	stickyList = new StickyListHeadersListView(this.getContext());
        RelativeLayout.LayoutParams vpLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        //vpLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
        stickyList.setOnStickyHeaderOffsetChangedListener(this);
        stickyList.setOnScrollListener(mScrollListener);
        this.addView(stickyList, vpLP);
        
        createBottomBanner();
    }
    
    private void addListeners()
    {
    	GlobalData.getData().setFavorGoodsDataChangeListener(new GoodsDataChangeListener() {

			@Override
			public void notifyDataSetChanged() {
				handleBannerStatus();
			}
    		
    	});

    }

    private void generateFragmentsForData()
    {
        createContentFragments();
        addAdaptor();
        mAdapter.notifyDataSetChanged();
        MainActivity.getInstance().getNavigationPanel().refreshMenu();
    }
    
    private void getFromCloud()
    {
        CloudAPIAsyncTask task = new CloudAPIAsyncTask("GetAllProductInfo", new CloudAPIAsyncTask.ICloudAPITaskListener() {
            
            @Override
            public void onFinish(int returnCode, String strResult) {
                // TODO Auto-generated method stub
                if (CacheUtil.needUpdate(CacheUtil.Key_visited, strResult))
                {
                    CacheUtil.update(CacheUtil.Key_visited, strResult);
                }
                JSONUtil.parseGoodsListInfo(strResult);
                //---------------------------------------------------------------
                generateFragmentsForData();
                
                updateBannerStatus();
            }
        });
        task.execute();
    }
    
    private void addAdaptor()
    {   mAdapter = new StickyListViewAdapter(this.getContext());
        stickyList.setAdapter(mAdapter);
    }
    
    private void createContentFragments()
    {

    }
    

    
    private void loadFromCache()
    {
        String visitedData = CacheUtil.loadCachedData(CacheUtil.Key_visited);
        if (null != visitedData && !visitedData.isEmpty())
        {
            JSONUtil.parseGoodsListInfo(visitedData);
            generateFragmentsForData();
            
            updateBannerStatus();
        }
        else
        {
            getFromCloud();
        }
    }
    

    private int getLikedGoodsNum() {
    	ArrayList<GoodsItem> list = GlobalData.getData().getGoodsListByCategory(GlobalData.FavoriteCategory);
    	int count = list.size(); // need improve
    	
    	return count;
    }
    
    private void updateBannerStatus() {
    	// update Banner visibility when start up
    	// if like goods is empty, then hide it.
    	// if not , then show banner
    	int count = getLikedGoodsNum();
    	if(count <= 0) {
    		mBadge.hide();
    		mBanner.setVisibility(View.GONE);
    	} else {
    		mBadge.setText(Integer.toString(count));
    		mBadge.show();
    		mBanner.setVisibility(View.VISIBLE);
    	}
    }
    
    
    private void handleBannerStatus() {
    	int count = getLikedGoodsNum();
    	boolean showBanner = false;
    	boolean useAnimation = false;
    	if(count <= 0) {
    		mBadge.setText("0");
    		showBanner = false;
    		useAnimation = (View.VISIBLE == mBanner.getVisibility());
    	} else {
    		mBadge.show();
    		mBadge.setText(Integer.toString(count));
    		showBanner = true;
    		useAnimation = (View.VISIBLE != mBanner.getVisibility());
    	}
    	
    	
    	showBanner(showBanner, useAnimation);
    }
    
    
	public void showBanner(boolean show, boolean withAnim) {
		if (show) {
			if (withAnim) {
				Animation anim = AnimationUtils.loadAnimation(
						this.getContext(), R.anim.push_up_in);
				//anim.setFillAfter(true);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						mBanner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}});
				
				mBanner.setVisibility(View.VISIBLE);
				mBanner.startAnimation(anim);
			} else {
				mBanner.setVisibility(View.VISIBLE);
			}
			
		} else {
			if (withAnim) {
				Animation anim = AnimationUtils.loadAnimation(
						this.getContext(), R.anim.push_up_out);
				//anim.setFillAfter(true);
				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						mBanner.clearFocus();
						mBanner.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}});
				
				mBanner.startAnimation(anim);

			} else {
				mBanner.setVisibility(View.GONE);
			}
		}
	}
	
	
	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView absListView,
				int scrollState) {
			
			if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				if(mBanner.getVisibility() == View.VISIBLE)
					showBanner(false, true);
			}
			
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}};


	@Override
	public void onStickyHeaderOffsetChanged(StickyListHeadersListView l,
			View header, int offset) {
       
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
		
	}
}
