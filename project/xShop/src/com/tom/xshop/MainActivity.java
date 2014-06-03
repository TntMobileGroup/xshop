package com.tom.xshop;

import com.tom.xshop.cloud.api.demo.APIDemoDialog;
import com.tom.xshop.config.PrefConfig;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.gallery.GalleryView;
import com.tom.xshop.gallery.util.ImageCache;
import com.tom.xshop.gallery.util.ImageFetcher;
import com.tom.xshop.ui.navigation.NavigationMenuItem;
import com.tom.xshop.ui.navigation.NavigationPanel;
import com.tom.xshop.util.CacheUtil;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.PrefUtil;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.igexin.slavesdk.MessageManager;

public class MainActivity extends FragmentActivity {

	private static final String IMAGE_CACHE_DIR = "images";
    private NavigationPanel mNavigationPanel;    
    private GalleryView mCenterView = null;
    
    private static MainActivity _instance;
    private static ImageFetcher mImageFetcher;
	private DrawerLayout mDrawerLayout;
	private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;
    
    public static MainActivity getInstance()
    {
        return _instance;
    }
    
    
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }
    
    private void createSlidingUI()
    {
    	mDrawerLayout = new DrawerLayout(this);
        this.setContentView(mDrawerLayout);
        
        mNavigationPanel = new NavigationPanel(this);
        mNavigationPanel.setOnMenuItemClickListener(new XShopMenItemClickListener());
        mCenterView = new GalleryView(this);
        
        mDrawerLayout.addView(mCenterView, new DrawerLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.START;
        mDrawerLayout.addView(mNavigationPanel, lp);
        
        
        
        mDrawerLayout.setDrawerListener(new XShopDrawerListener());
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // The drawer title must be set in order to announce state changes when
        // accessibility is turned on. This is typically a simple description,
        // e.g. "Navigation".
        mDrawerLayout.setDrawerTitle(GravityCompat.START, getString(R.string.drawer_title));


        mActionBar = createActionBarHelper();
        mActionBar.init();

        // ActionBarDrawerToggle provides convenient helpers for tying together the
        // prescribed interactions between a top-level sliding drawer and the action bar.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
    }
    
    public NavigationPanel getNavigationPanel()
    {
        return mNavigationPanel;
    }
    
//    public SlidingGalleryView getSlidingGalleryView()
//    {
//        return mCenterView;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        PrefUtil.init(this);
        setWindowStyle();
        createSlidingUI();
        MessageManager.getInstance().initialize(this.getApplicationContext());
        tryToFocus();
        
        this.getActionBar().setTitle(R.string.customer_name);
        this.getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.actionbar_bkg));
        
        
        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width) / 2;

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.setImageFadeIn(false);
    }
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * The action bar home/up action should open or close the drawer.
         * mDrawerToggle will take care of this.
         */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
	@Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        APIDemoDialog.destroy();
    }

    
    private String curGoodsUUID = "";
    private void tryToFocus()
    {
        curGoodsUUID = PrefUtil.getStringValue(PrefConfig.Key_GoodsUUID);
        if (curGoodsUUID.length() > 0)
        {
            String message = PrefUtil.getStringValue(PrefConfig.Key_EventMessage);
            String category = GlobalData.getData().getCategoryByGoods(curGoodsUUID);
            GlobalData.getData().updateFocusData(curGoodsUUID, message);
            if (category.length() > 0)
            {
                mCenterView.scrollToCategory(category, true);
            }
            
            clearParams();
        }
    }
    
    private void clearParams()
    {
        PrefUtil.clearValue(PrefConfig.Key_GoodsUUID);
        PrefUtil.clearValue(PrefConfig.Key_EventMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery, menu);
        return true;
    }

    private void setWindowStyle() {
        //requestWindowFeature(Window.FEATURE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        DensityAdaptor.init(this);
        CacheUtil.initialize(this);
    }
    
    
    
	private class XShopMenItemClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mDrawerLayout.closeDrawer(mNavigationPanel);

			NavigationMenuItem item = (NavigationMenuItem) v;
			if (item != null) {
				String title = item.getTitle();
				if (title.startsWith("[")) {
					Toast.makeText(v.getContext(), "Coming soon!",
							Toast.LENGTH_SHORT).show();
				} else if (title.equalsIgnoreCase(APIDemoDialog.APIDemoLabel)) {
					APIDemoDialog.getInstance(v.getContext()).show();
				} else {
						mCenterView.scrollToCategory(title, false);
				}
			}
		}

	}
    
    /**
     * A drawer listener can be used to respond to drawer events such as becoming
     * fully opened or closed. You should always prefer to perform expensive operations
     * such as drastic relayout when no animation is currently in progress, either before
     * or after the drawer animates.
     *
     * When using ActionBarDrawerToggle, all DrawerLayout listener methods should be forwarded
     * if the ActionBarDrawerToggle is not used as the DrawerLayout listener directly.
     */
    private class XShopDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerToggle.onDrawerOpened(drawerView);
            mActionBar.onDrawerOpened();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerToggle.onDrawerClosed(drawerView);
            mActionBar.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

    /**
     * Create a compatible helper that will manipulate the action bar if available.
     */
    private ActionBarHelper createActionBarHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new ActionBarHelperICS();
        } else {
            return new ActionBarHelper();
        }
    }
    
    
    /**
     * Stub action bar helper; this does nothing.
     */
    private class ActionBarHelper {
        public void init() {}
        public void onDrawerClosed() {}
        public void onDrawerOpened() {}
        public void setTitle(CharSequence title) {}
    }
    
    /**
     * Action bar helper for use on ICS and newer devices.
     */
    private class ActionBarHelperICS extends ActionBarHelper {
        private final ActionBar mActionBar;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;

        ActionBarHelperICS() {
            mActionBar = getActionBar();
        }

        @Override
        public void init() {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mTitle = mDrawerTitle = getTitle();
        }

        /**
         * When the drawer is closed we restore the action bar state reflecting
         * the specific contents in view.
         */
        @Override
        public void onDrawerClosed() {
            super.onDrawerClosed();
            mActionBar.setTitle(mTitle);
        }

        /**
         * When the drawer is open we set the action bar to a generic title.
         * The action bar should only contain data relevant at the top level of
         * the nav hierarchy represented by the drawer, as the rest of your content
         * will be dimmed down and non-interactive.
         */
        @Override
        public void onDrawerOpened() {
            super.onDrawerOpened();
            mActionBar.setTitle(mDrawerTitle);
        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
        }
    }
}
