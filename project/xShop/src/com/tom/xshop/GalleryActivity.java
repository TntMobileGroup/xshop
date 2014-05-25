
package com.tom.xshop;

import com.tom.xshop.config.PrefConfig;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.gallery.SlidingGalleryView;
import com.tom.xshop.ui.navigation.NavigationPanel;
import com.tom.xshop.ui.sliding.SlidingMenu;
import com.tom.xshop.util.CacheUtil;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.PrefUtil;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.igexin.slavesdk.MessageManager;

public class GalleryActivity extends FragmentActivity {

    private SlidingMenu mSlidingMenu;
    
    private NavigationPanel mNavigationPanel;
    
    private View mTestView = null;
    
    private SlidingGalleryView mCenterView = null;
    
    private static GalleryActivity _instance;
    
    public static GalleryActivity getInstance()
    {
        return _instance;
    }
    
    public void toggleNavigationPanel()
    {
        if (null != mSlidingMenu)
        {
            mSlidingMenu.toggleLeftView();
        }
    }
    
    private void createSlidingUI()
    {
        mSlidingMenu = new SlidingMenu(this);
        this.setContentView(mSlidingMenu);
        mNavigationPanel = new NavigationPanel(this);
        mTestView = new View(this);
        mCenterView = new SlidingGalleryView(this);
        mSlidingMenu.setLeftView(mNavigationPanel);
        mSlidingMenu.setRightView(mTestView);
        mSlidingMenu.setCenterView(mCenterView);
    }
    
    public NavigationPanel getNavigationPanel()
    {
        return mNavigationPanel;
    }
    
    public SlidingGalleryView getSlidingGalleryView()
    {
        return mCenterView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        PrefUtil.init(this);
        setWindowStyle();
        createSlidingUI();
        MessageManager.getInstance().initialize(this.getApplicationContext());
        tryToFocus();
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
                GalleryActivity.getInstance().getSlidingGalleryView().scrollToCategory(category, true);
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
        requestWindowFeature(Window.FEATURE_PROGRESS);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        DensityAdaptor.init(this);
        CacheUtil.initialize(this);
    }
}
