package com.tom.xshop.ui.navigation;

import com.tom.xshop.GalleryActivity;
import com.tom.xshop.R;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.LayoutUtil;
import com.tom.xshop.util.UIConfig;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationMenuItem extends RelativeLayout {

    private ImageView mIcon = null;
    private TextView mTextView = null;
    private String mText = "";
    private View mOverlayView = null;
    
    public NavigationMenuItem(Context context, String text) {
        super(context);
        mText = text;
        createUI(context);
        addListeners();
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mOverlayView.setVisibility(View.VISIBLE);
                break;
            default:
                mOverlayView.setVisibility(View.GONE);
                break;
        }
        return super.onTouchEvent(event);
    }


    private void createUI(Context context)
    {
        LinearLayout.LayoutParams itemLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                DensityAdaptor.getDensityIndependentValue(50));
        this.setLayoutParams(itemLP);
        
        mOverlayView = new View(context);
        mOverlayView.setBackgroundColor(Color.WHITE);
        RelativeLayout.LayoutParams overlayLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mOverlayView.setAlpha(0.5f);
        mOverlayView.setVisibility(View.GONE);
        this.addView(mOverlayView, overlayLP);
        
        int largeMargin = LayoutUtil.getLargeMargin();
        mIcon = new ImageView(context);
        
        //[TODO] hardcode
        if (mText.startsWith("["))
        {
            mIcon.setImageResource(R.drawable.question);
        }
        else
        {
            mIcon.setImageResource(R.drawable.like);
        }
        int size = DensityAdaptor.getDensityIndependentValue(32);
        RelativeLayout.LayoutParams iconLP = new RelativeLayout.LayoutParams(size, size);
        iconLP.leftMargin = largeMargin;
        iconLP.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(mIcon, iconLP);
        
        mTextView = new TextView(context);
        mTextView.setText(mText);
        mTextView.setTextColor(UIConfig.getLightTextColor());
        mTextView.setTextSize(UIConfig.getTitleTextSize());
        
        RelativeLayout.LayoutParams textLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textLP.leftMargin = largeMargin * 2 + size;
        textLP.addRule(RelativeLayout.CENTER_VERTICAL);
        this.addView(mTextView, textLP);
    }
    
    private void addListeners()
    {
        this.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mText.startsWith("["))
                {
                    Toast.makeText(v.getContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    GalleryActivity.getInstance().toggleNavigationPanel();
                    GalleryActivity.getInstance().getSlidingGalleryView().scrollToCategory(mText, false);
                }
            }
        });
    }
}
