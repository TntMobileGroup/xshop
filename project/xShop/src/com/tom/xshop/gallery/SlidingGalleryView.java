package com.tom.xshop.gallery;

import java.util.ArrayList;






import com.tom.xshop.GalleryActivity;
import com.tom.xshop.R;
import com.tom.xshop.cloud.CloudAPIAsyncTask;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsDataChangeListener;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.gallery.ui.ImageGridFragment;
import com.tom.xshop.gallery.ui.OnScrollStateChangedListener;
import com.tom.xshop.ui.thirdparty.ViewBadger.BadgeView;
import com.tom.xshop.util.CacheUtil;
import com.tom.xshop.util.JSONUtil;
import com.tom.xshop.util.LayoutUtil;






import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public class SlidingGalleryView extends RelativeLayout {
    private static final String TAG = "ImageGridActivity";
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;
    private ArrayList<FrameLayout> mFragmentViewList = null;
    private ArrayList<String> mCategories = null;
    private int mTestId = 100;
    private int mFavoriteId = 90;
    private RelativeLayout mBanner = null;
    private Button mBannerBtn = null;
    private BadgeView mBadge = null;
    
    
    public SlidingGalleryView(Context context) {
        super(context);
        createUI();
        addListeners();
        loadFromCache();
    }
    
    public void scrollToCategory(String category, boolean updateFocus)
    {
        int count = mCategories.size();
        for (int i = 0; i < count; ++i)
        {
            if (mCategories.get(i).equalsIgnoreCase(category))
            {
                mViewPager.setCurrentItem(i);
                return;
            }
        }
        
        if (updateFocus)
        {
            mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

//    private void createTopBanner() {
//        Context context = this.getContext();
////        int smallMargin = LayoutUtil.getSmallMargin();
//        int mediumMargin = LayoutUtil.getMediumMargin();
//        mTopBanner = new RelativeLayout(context);
//        mTopBanner.setBackgroundResource(R.drawable.hori_bar_wood);
//        RelativeLayout.LayoutParams topViewLP = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                LayoutUtil.getGalleryTopPanelHeight());
////        topViewLP.height = LayoutUtil.getGalleryTopPanelHeight();
//        mTopBanner.setLayoutParams(topViewLP);
//        this.addView(mTopBanner, topViewLP);
//        
//        mToggleNavigationPanelBtn = new HaloButton(context, R.drawable.menu);
////        mToggleNavigationPanelBtn.setBackgroundColor(Color.YELLOW);
//        RelativeLayout.LayoutParams toggleNaviBtnLP = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        toggleNaviBtnLP.leftMargin = mediumMargin;
//        toggleNaviBtnLP.width = DensityAdaptor.getDensityIndependentValue(32);
//        toggleNaviBtnLP.height = DensityAdaptor.getDensityIndependentValue(32);
//        toggleNaviBtnLP.addRule(RelativeLayout.CENTER_VERTICAL);
//        mToggleNavigationPanelBtn.setLayoutParams(toggleNaviBtnLP);
//        mTopBanner.addView(mToggleNavigationPanelBtn);
//        
//        mTopTextView = new TextView(context);
//        mTopTextView.setTextSize(UIConfig.getTitleTextSize());
//        mTopTextView.setText("[Shop Name Here]");
//        mTopTextView.setTextColor(UIConfig.getLightTextColor());
//        RelativeLayout.LayoutParams topTextBtnLP = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        topTextBtnLP.addRule(RelativeLayout.CENTER_IN_PARENT);
//        mTopBanner.addView(mTopTextView, topTextBtnLP);
//    }
//    
    
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
//        mBadge.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (mBadge.isShown()) {
//					mBadge.increment(1);
//				} else {
//					mBadge.show();
//				}
//			}
//		});
        
      
    }

    private void createUI()
    {
        //this.setBackgroundResource(R.drawable.wood_bk);
        //createTopBanner();

        
        mViewPager = new ViewPager(this.getContext());
        RelativeLayout.LayoutParams vpLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        //vpLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
        this.addView(mViewPager, vpLP);
        
        createBottomBanner();
//        mOverlay = new View(this.getContext());
//        RelativeLayout.LayoutParams overlayLP = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        mOverlay.setBackgroundColor(Color.BLUE);
//        mOverlay.setAlpha(0.5f);
//        this.addView(mOverlay, overlayLP);
//        
//        mOverlay.setOnTouchListener(new View.OnTouchListener() {
//            
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                return false;
//            }
//        });
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
        mViewPager.getAdapter().notifyDataSetChanged();
        GalleryActivity.getInstance().getNavigationPanel().refreshMenu();
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
    {
        PagerAdapter pagerAdapter = new PagerAdapter() {

            private int getCategoryCount()
            {
                return mCategories.size();
            }

            @Override
            public int getCount()
            {
                return getCategoryCount();
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView(mFragmentViewList.get(position));
            }

            @Override
            public int getItemPosition(Object object)
            {

                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position)
            {
                return mCategories.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                View v = mFragmentViewList.get(position);
                container.addView(v);

                attachGridFragment(mTestId + position, mCategories.get(position));
                return v;
            }

        };
        mViewPager.setAdapter(pagerAdapter);
    }
    
    private void createContentFragments()
    {
        Context context = this.getContext();
        
        mPagerTabStrip = new PagerTabStrip(context);
        mPagerTabStrip.setTabIndicatorColor(Color.GREEN);
        mPagerTabStrip.setDrawFullUnderline(true);
        mPagerTabStrip.setBackgroundColor(Color.WHITE);
        mPagerTabStrip.setTextSpacing(50);
        
        ViewPager.LayoutParams tabLP = new ViewPager.LayoutParams();
        tabLP.height = ViewPager.LayoutParams.WRAP_CONTENT;
        tabLP.width = ViewPager.LayoutParams.WRAP_CONTENT;
        tabLP.gravity = Gravity.TOP;

        mViewPager.addView(mPagerTabStrip, tabLP);
        
        mCategories = GlobalData.getData().getAllCategories();
        mFragmentViewList = new ArrayList<FrameLayout>();
        int count = mCategories.size();
        for (int i = 0; i < count; ++i)
        {
            FrameLayout f = new FrameLayout(context);
            f.setId(i + mTestId);
            mFragmentViewList.add(f);
        }
        
        // Liked category
        FrameLayout f = new FrameLayout(context);
        f.setId(mFavoriteId);
        mFragmentViewList.add(f);
    }
    
    private void attachGridFragment(int id, String category)
    {
        GalleryActivity activity = GalleryActivity.getInstance();
        String curTag = TAG + String.valueOf(id);
        if (activity.getSupportFragmentManager().findFragmentByTag(curTag) == null)
        {
            final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ImageGridFragment f = new ImageGridFragment(category);
            f.setOnScrollStateChangedListener(mScrollListener);
            ft.replace(id, f, curTag);
            ft.commit();
        }
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
	
	private OnScrollStateChangedListener mScrollListener = new OnScrollStateChangedListener() {

		@Override
		public void onScrollStateChanged(AbsListView absListView,
				int scrollState) {
			
			if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				if(mBanner.getVisibility() == View.VISIBLE)
					showBanner(false, true);
			}
			
		}};
}
