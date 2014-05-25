package com.tom.xshop.gallery.ui;

import com.tom.xshop.R;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.UIConfig;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GoodsItemView extends RelativeLayout {
    private RecyclingImageView mThumbImageView = null;
    private RelativeLayout mBottomPanel = null;
    private TextView mMessageTextView = null;
    private TextView mNameTextView = null;
    private TextView mBrandModelTextView = null;
    private ImageView mLikeImageView = null;

    private GoodsItem mData = null;

    public GoodsItemView(Context context) {
        super(context);
        createUI(context);
        addListener();
    }
    
    private void focus(boolean focus)
    {
        this.setBackgroundColor(focus ? Color.GREEN : Color.TRANSPARENT);
    }

    private void createUI(Context context)
    {
        //this.setBackgroundColor(Color.GREEN);
        int margin = DensityAdaptor.getDensityIndependentValue(5);
        mThumbImageView = new RecyclingImageView(context);
        mThumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RelativeLayout.LayoutParams imageLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imageLP.setMargins(margin, margin, margin, margin);
        this.addView(mThumbImageView, imageLP);
        
        mMessageTextView = new TextView(context);
        mMessageTextView.setTextSize(UIConfig.getTitleTextSize());
        mMessageTextView.setTextColor(Color.YELLOW);
        mMessageTextView.setBackgroundColor(Color.RED);
        RelativeLayout.LayoutParams msgLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        msgLP.setMargins(margin, margin, margin, margin);
        this.addView(mMessageTextView, msgLP);
        
        mBottomPanel = new RelativeLayout(context);
        mBottomPanel.setBackgroundResource(R.drawable.hori_bar_wood);
        RelativeLayout.LayoutParams btmPanelLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                DensityAdaptor.getDensityIndependentValue(40));
        btmPanelLP.leftMargin = btmPanelLP.rightMargin = btmPanelLP.bottomMargin = margin;
        btmPanelLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.addView(mBottomPanel, btmPanelLP);
        
        mNameTextView = new TextView(context);
        mNameTextView.setEllipsize(TruncateAt.END);
        mNameTextView.setTextSize(16.0f);
        mNameTextView.setTextColor(Color.WHITE);
//        mNameTextView.setText("Test");
        RelativeLayout.LayoutParams nameLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        nameLP.leftMargin = margin;
        nameLP.rightMargin = DensityAdaptor.getDensityIndependentValue(45);
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
        
        mLikeImageView = new ImageView(context);
        mLikeImageView.setImageResource(R.drawable.unlike);
        RelativeLayout.LayoutParams likeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        likeLP.rightMargin = margin;
        likeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        likeLP.addRule(RelativeLayout.CENTER_VERTICAL);
        mBottomPanel.addView(mLikeImageView, likeLP);
    }
    
    public void setData(GoodsItem data)
    {
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
        
        mMessageTextView.setText(mData.getMessage());
    }
    
    public RecyclingImageView getImageView()
    {
        return mThumbImageView;
    }
    
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
                ImageGridFragment.updateFavoriteAdaptor();
            }
        });
    }

//    private void addListener()
//    {
//        GlobalData.getData().setCurCategory(mData.getCategory());
//        final Intent i = new Intent(this.getContext(), ImageDetailActivity.class);
//        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
//        if (Utils.hasJellyBean()) {
//            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
//            // show plus the thumbnail image in GridView is cropped. so using
//            // makeScaleUpAnimation() instead.
////            ActivityOptions options =
////                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
//            getActivity().startActivity(i);//, options.toBundle());
//        } else {
//            startActivity(i);
//        }
//    }
}
