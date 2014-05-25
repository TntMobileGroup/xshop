package com.tom.xshop.gallery;

import java.util.ArrayList;

import com.tom.xshop.GalleryActivity;
import com.tom.xshop.R;
import com.tom.xshop.cloud.CloudAPIAsyncTask;
import com.tom.xshop.config.PrefConfig;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.gallery.ui.ImageGridFragment;
import com.tom.xshop.ui.control.HaloButton;
import com.tom.xshop.util.CacheUtil;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.JSONUtil;
import com.tom.xshop.util.LayoutUtil;
import com.tom.xshop.util.PrefUtil;
import com.tom.xshop.util.UIConfig;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlidingGalleryView extends RelativeLayout {
    private static final String TAG = "ImageGridActivity";
    private ViewPager mViewPager;
    private PagerTabStrip mPagerTabStrip;
    private ArrayList<FrameLayout> mFragmentViewList = null;
    private ArrayList<String> mCategories = null;
    private int mTestId = 100;
    private int mFavoriteId = 90;
    private RelativeLayout mTopBanner = null;
    private HaloButton mToggleNavigationPanelBtn = null;
    
    private View mOverlay = null;
    private TextView mTopTextView = null;
    
    private boolean mInactive = false;
    
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

    private void createTopBanner() {
        Context context = this.getContext();
//        int smallMargin = LayoutUtil.getSmallMargin();
        int mediumMargin = LayoutUtil.getMediumMargin();
        mTopBanner = new RelativeLayout(context);
        mTopBanner.setBackgroundResource(R.drawable.hori_bar_wood);
        RelativeLayout.LayoutParams topViewLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                LayoutUtil.getGalleryTopPanelHeight());
//        topViewLP.height = LayoutUtil.getGalleryTopPanelHeight();
        mTopBanner.setLayoutParams(topViewLP);
        this.addView(mTopBanner, topViewLP);
        
        mToggleNavigationPanelBtn = new HaloButton(context, R.drawable.menu);
//        mToggleNavigationPanelBtn.setBackgroundColor(Color.YELLOW);
        RelativeLayout.LayoutParams toggleNaviBtnLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        toggleNaviBtnLP.leftMargin = mediumMargin;
        toggleNaviBtnLP.width = DensityAdaptor.getDensityIndependentValue(32);
        toggleNaviBtnLP.height = DensityAdaptor.getDensityIndependentValue(32);
        toggleNaviBtnLP.addRule(RelativeLayout.CENTER_VERTICAL);
        mToggleNavigationPanelBtn.setLayoutParams(toggleNaviBtnLP);
        mTopBanner.addView(mToggleNavigationPanelBtn);
        
        mTopTextView = new TextView(context);
        mTopTextView.setTextSize(UIConfig.getTitleTextSize());
        mTopTextView.setText("[Shop Name Here]");
        mTopTextView.setTextColor(UIConfig.getLightTextColor());
        RelativeLayout.LayoutParams topTextBtnLP = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        topTextBtnLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTopBanner.addView(mTopTextView, topTextBtnLP);
    }

    private void createUI()
    {
        this.setBackgroundResource(R.drawable.wood_bk);
        createTopBanner();
        
        mViewPager = new ViewPager(this.getContext());
        RelativeLayout.LayoutParams vpLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        vpLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
        this.addView(mViewPager, vpLP);
        
        
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
        mToggleNavigationPanelBtn.setOnClickListener(new View.OnClickListener() {
          
          @Override
          public void onClick(View v) {
              // TODO Auto-generated method stub
              GalleryActivity.getInstance().toggleNavigationPanel();
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
        CloudAPIAsyncTask task = new CloudAPIAsyncTask("show", new CloudAPIAsyncTask.ICloudAPITaskListener() {
            
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
        mPagerTabStrip.setTabIndicatorColor(Color.WHITE);
        mPagerTabStrip.setDrawFullUnderline(false);
        mPagerTabStrip.setBackgroundColor(Color.TRANSPARENT);
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
            ft.replace(id, new ImageGridFragment(category), curTag);
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
        }
        else
        {
            getFromCloud();
        }
    }
}
