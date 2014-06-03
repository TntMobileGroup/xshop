package com.tom.xshop.ui.navigation;

import com.tom.xshop.R;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.LayoutUtil;
import com.tom.xshop.util.UIConfig;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigationPanel extends RelativeLayout {

	private RelativeLayout mTopBanner = null;
//	private RelativeLayout mBottomBanner = null;
	private ImageView mLogoBtn = null;
	private TextView mNameTextView = null;
	
	private NavigationMenu mMenuView = null;

	public NavigationPanel(Context context) {
		super(context);
		createUI(context);
	}
	
	public void refreshMenu()
	{
	    if (null != mMenuView)
	    {
	        mMenuView.refreshMenu();
	    }
	}

	public void setOnMenuItemClickListener(View.OnClickListener  listener) {
		if(mMenuView != null) {
			mMenuView.setOnMenuItemClickListener(listener);
		}
	}
	
	private void createUI(Context context)
	{
		this.setBackgroundResource(R.drawable.wood_bk);

		createTopBanner(context);
		createMenu(context);
//		createBottomBanner(context);
	}
	
	private void createMenu(Context context)
	{
	    mMenuView = new NavigationMenu(context);
	    RelativeLayout.LayoutParams menuLP = new RelativeLayout.LayoutParams(
	            LayoutUtil.getNavigationPanelWidth(),
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        menuLP.topMargin = LayoutUtil.getGalleryTopPanelHeight();
        this.addView(mMenuView, menuLP);
	}

	private void createTopBanner(Context context) {
//		int smallMargin = LayoutUtil.getSmallMargin();
	    int mediumMargin = LayoutUtil.getMediumMargin();
		mTopBanner = new RelativeLayout(context);
		mTopBanner.setBackgroundResource(R.drawable.hori_bar_wood);
		RelativeLayout.LayoutParams topViewLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		topViewLP.width = LayoutUtil.getNavigationPanelWidth();
		topViewLP.height = LayoutUtil.getGalleryTopPanelHeight();
		mTopBanner.setLayoutParams(topViewLP);
		this.addView(mTopBanner, topViewLP);
		
		mLogoBtn = new ImageView(context);
		mLogoBtn.setImageResource(R.drawable.ic_launcher);
		RelativeLayout.LayoutParams avatarLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarLP.leftMargin = mediumMargin;
		avatarLP.width = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.height = DensityAdaptor.getDensityIndependentValue(32);
		avatarLP.addRule(RelativeLayout.CENTER_VERTICAL);
		mLogoBtn.setLayoutParams(avatarLP);
		mTopBanner.addView(mLogoBtn);
		
		mNameTextView = new TextView(context);
		mNameTextView.setText("[App Name Here]");
		mNameTextView.setTextSize(UIConfig.getTitleTextSize());
		mNameTextView.setTextColor(UIConfig.getLightTextColor());
		RelativeLayout.LayoutParams nameTextLP = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameTextLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		mNameTextView.setLayoutParams(nameTextLP);
		mTopBanner.addView(mNameTextView);
		
//		
//		View separator = ControlFactory
//				.createVertSeparatorForRelativeLayout(context);
//		RelativeLayout.LayoutParams separatorLP = (LayoutParams) separator
//				.getLayoutParams();
//		separatorLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		separatorLP.rightMargin = DensityAdaptor.getDensityIndependentValue(48);
//		separator.setLayoutParams(separatorLP);
//		mTopBanner.addView(separator);
	}

//	private void createBottomBanner(Context context) {
//		mBottomBanner = new RelativeLayout(this.getContext());
//		mBottomBanner.setBackgroundResource(R.drawable.stone_bg);
//		RelativeLayout.LayoutParams bottomBannerLP = new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.FILL_PARENT,
//				RelativeLayout.LayoutParams.WRAP_CONTENT);
//		bottomBannerLP.width = LayoutUtil.getNavigationPanelWidth();
//		bottomBannerLP.height = LayoutUtil.getGalleryBottomPanelHeight();
//		bottomBannerLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		this.addView(mBottomBanner, bottomBannerLP);
//	}
}
