package com.tom.xshop.gallery.ui;



import com.tom.xshop.R;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.UIConfig;

import android.content.Context;
import android.graphics.Color;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GoodsItemDetailView extends LinearLayout {
	
	
	private ViewPager mItemImages = null;
    private RelativeLayout mBottomPanel = null;
    private TextView mMessageTextView = null;
    private TextView mNameTextView = null;
    private TextView mBrandModelTextView = null;
    private ImageView mLikeImageView = null;
    

    private GoodsItem mData = null;

    public GoodsItemDetailView(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        
        createUI(context);
        addListener();
    }
    
    private void focus(boolean focus)
    {
       // this.setBackgroundColor(focus ? Color.GREEN : Color.TRANSPARENT);
    }

    private void createUI(Context context)
    {
        //this.setBackgroundColor(Color.GREEN);
    	this.setBackgroundResource(R.drawable.popup_list_bg);
    	
        int pmargin = DensityAdaptor.getDensityIndependentValue(5);
        this.setPadding(pmargin, pmargin, pmargin, pmargin);

        int width = DensityAdaptor.getScreenWidth();
        int height = DensityAdaptor.getScreenHeight();
        int expWidth = height < width ? height : width;
        mItemImages = new ViewPager(this.getContext());
        
        LinearLayout.LayoutParams imageLP = new LinearLayout.LayoutParams(expWidth,
        		expWidth *2 /3);
        imageLP.setMargins(pmargin, pmargin, pmargin, pmargin);
        this.addView(mItemImages, imageLP);
        

        int margin = DensityAdaptor.getDensityIndependentValue(5);
        ImageView seperator = new ImageView(context);
        seperator.setImageResource(R.drawable.hori_separator);
        seperator.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);

        this.addView(seperator, lp);
        
        mMessageTextView = new TextView(context);
        mMessageTextView.setTextSize(UIConfig.getTitleTextSize());
        mMessageTextView.setTextColor(Color.YELLOW);
        mMessageTextView.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams msgLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);
        msgLP.setMargins(margin, margin, margin, margin);
        mMessageTextView.setVisibility(View.GONE);
        this.addView(mMessageTextView, msgLP);
        
        
        
        mBottomPanel = new RelativeLayout(context);
        mBottomPanel.setId(1);
        //mBottomPanel.setBackgroundResource(R.drawable.hori_bar_wood);
        RelativeLayout.LayoutParams btmPanelLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                DensityAdaptor.getDensityIndependentValue(40));
        btmPanelLP.leftMargin = btmPanelLP.rightMargin = btmPanelLP.bottomMargin = margin;
        btmPanelLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mBottomPanel, btmPanelLP);
        
        mNameTextView = new TextView(context);
        mNameTextView.setEllipsize(TruncateAt.END);
        mNameTextView.setTextSize(16.0f);
        mNameTextView.setTextColor(0xff3d4245);
//        mNameTextView.setText("Test");
        RelativeLayout.LayoutParams nameLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameLP.leftMargin = margin;
        nameLP.rightMargin = DensityAdaptor.getDensityIndependentValue(45);
        nameLP.addRule(RelativeLayout.CENTER_VERTICAL);
        mBottomPanel.addView(mNameTextView, nameLP);
        
        mBrandModelTextView = new TextView(context);
        mBrandModelTextView.setTextSize(14.0f);
        mBrandModelTextView.setTextColor(Color.WHITE);
//        mBrandModelTextView.setText("AAAAAAAaaaaaaaaa");
        RelativeLayout.LayoutParams bmLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        bmLP.leftMargin = margin;
        bmLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mBottomPanel.addView(mBrandModelTextView, bmLP);
        mBrandModelTextView.setVisibility(View.GONE);
        
        mLikeImageView = new ImageView(context);
        mLikeImageView.setImageResource(R.drawable.unlike);
        RelativeLayout.LayoutParams likeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        likeLP.rightMargin = margin;
        likeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        likeLP.addRule(RelativeLayout.CENTER_VERTICAL);
        mBottomPanel.addView(mLikeImageView, likeLP);    

    }
    
    public void setDataWithAdapter(GoodsItem data, PagerAdapter adapter)
    {
    	//mItemImages.removeAllViews();
    	mItemImages.setAdapter(adapter);
    	mItemImages.requestLayout();
    	//adapter.notifyDataSetChanged();
    	
        mData = data;
        mNameTextView.setText(data.getName());
        mBrandModelTextView.setText(data.getBrand() + "/" + data.getModel());
        
        if (mData.isLiked())
        {
            mLikeImageView.setImageResource(R.drawable.like);
        }
        else
        {
            mLikeImageView.setImageResource(R.drawable.unlike);
        }
        
        focus(mData.getFocus());
        
        if(mData.getMessage() != null)
        mMessageTextView.setText(mData.getMessage());
       
    }
    
//    public RecyclingImageView getImageView()
//    {
//        return mThumbImageView;
//    }
    
    private void addListener()
    {
        mBottomPanel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
        
        mLikeImageView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mData.isLiked())
                {
                    mLikeImageView.setImageResource(R.drawable.unlike);
                }
                else
                {
                    mLikeImageView.setImageResource(R.drawable.like);
                }
                mData.like(!mData.isLiked());
                
                GlobalData.getData().updateLikedList(true);
                //ImageGridFragment.updateFavoriteAdaptor();
            }
        });
    }
    

}
