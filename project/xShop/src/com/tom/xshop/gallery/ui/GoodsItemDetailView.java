package com.tom.xshop.gallery.ui;



import com.tom.xshop.R;
import com.tom.xshop.config.PrefConfig;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.util.DensityAdaptor;
import com.tom.xshop.util.PrefUtil;
import com.tom.xshop.util.UIConfig;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class GoodsItemDetailView extends LinearLayout {
	
	public final static int MODE_SHOW = 0;
	public final static int MODE_PICK = 1;
	public final static int MODE_ORDER = 2;
	
	private int mUIMode = MODE_SHOW;
	private RelativeLayout mGoodsContainer = null;
	private ViewPager mItemImages = null;
    private RelativeLayout mBottomPanel = null;
    private TextView mMessageTextView = null;
    private TextView mNameTextView = null;
    private TextView mBrandModelTextView = null;
    private ImageView mLikeImageView = null;
    
    private CheckBox  mEnablePrice  = null;
    private LinearLayout mChangeGoodsNum = null;
    private ImageView mMinusButton = null;
    private ImageView mAddButton   = null;
    private EditText  mCurNumber   = null;
    private TextView  mOrderNumber = null;
    

    private GoodsItem mData = null;

    public GoodsItemDetailView(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        
        createUI(context);
        addListener();
        
        setUIMode(MODE_SHOW);
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
        mGoodsContainer = new RelativeLayout(this.getContext());
        mItemImages = new ViewPager(this.getContext());
        mGoodsContainer.addView(mItemImages, new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    
        LinearLayout.LayoutParams imageLP = new LinearLayout.LayoutParams(expWidth,
        		expWidth *2 /3);
        imageLP.setMargins(pmargin, pmargin, pmargin, pmargin);
        this.addView(mGoodsContainer, imageLP);
        

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
        
  
        addPriceModeUI();
    }
    
    
    private void addPriceModeUI() {
    	mEnablePrice = new CheckBox(this.getContext());
    	mEnablePrice.setChecked(false);
    	
    	int margin = DensityAdaptor.getDensityIndependentValue(10);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = margin;
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        
        mGoodsContainer.addView(mEnablePrice, lp);
        
        mChangeGoodsNum = new LinearLayout(this.getContext());
        mMinusButton = new ImageView(this.getContext());
        mAddButton   = new ImageView(this.getContext());
        mCurNumber   = new EditText(this.getContext());
        
        mAddButton.setImageResource(R.drawable.goods_number_plus);
        mMinusButton.setImageResource(R.drawable.goods_number_minus);
        mCurNumber.setText(Integer.toString(1));
        mCurNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpp.gravity = Gravity.CENTER_VERTICAL;
        mChangeGoodsNum.addView(mMinusButton, lpp);
        mChangeGoodsNum.addView(mCurNumber, lpp);
        mChangeGoodsNum.addView(mAddButton, lpp);

        mOrderNumber = new TextView(this.getContext());
        RelativeLayout.LayoutParams likeLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        likeLP.rightMargin = margin;
        likeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        likeLP.addRule(RelativeLayout.CENTER_VERTICAL);
        mBottomPanel.addView(mOrderNumber, likeLP);
        
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
        									 RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        mBottomPanel.addView(mChangeGoodsNum, lp);  
        
        mEnablePrice.setVisibility(View.INVISIBLE);
        mChangeGoodsNum.setVisibility(View.INVISIBLE);
        mOrderNumber.setVisibility(View.INVISIBLE);

    }
    
    
    public void setDataWithAdapter(GoodsItem data, PagerAdapter adapter)
    {
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
        
        mEnablePrice.setChecked(mData.isPicked());
        mCurNumber.setText(Integer.toString(mData.getOrderNumber()));
       
        mOrderNumber.setText(mData.getOrderNumber() + "¼þ");
    }
    
    
	public void setUIMode(int mode) {
		mUIMode = mode;

		switch (mUIMode) {
		case MODE_SHOW: {
			mLikeImageView.setVisibility(View.VISIBLE);
			mChangeGoodsNum.setVisibility(View.INVISIBLE);
			mEnablePrice.setVisibility(View.INVISIBLE);
			mOrderNumber.setVisibility(View.INVISIBLE);
		}
			break;
		case MODE_PICK: {
			mLikeImageView.setVisibility(View.VISIBLE);
			mChangeGoodsNum.setVisibility(View.INVISIBLE);
			mEnablePrice.setVisibility(View.VISIBLE);
			mOrderNumber.setVisibility(View.INVISIBLE);
		}
			break;
		case MODE_ORDER: {
			mOrderNumber.setVisibility(View.VISIBLE);
			mLikeImageView.setVisibility(View.INVISIBLE);
			mChangeGoodsNum.setVisibility(View.INVISIBLE);
			mEnablePrice.setVisibility(View.INVISIBLE);

		}
			break;

		}
	}
    
//    public RecyclingImageView getImageView()
//    {
//        return mThumbImageView;
//    }
	
	private void likeIt() {
        if (!mData.isLiked())
        {
        	mLikeImageView.setImageResource(R.drawable.like);
        	mData.like(!mData.isLiked());
        	GlobalData.getData().updateLikedList(true);
        	PrefUtil.putStringValue(PrefConfig.KEY_LastLikeGoods, mData.getId());
        }
	}
	
	private void cancelLikeIt() {
        if (mData.isLiked())
        {
        	mLikeImageView.setImageResource(R.drawable.unlike);
        	mData.like(!mData.isLiked());
        	GlobalData.getData().updateLikedList(true);
        	PrefUtil.putStringValue(PrefConfig.KEY_LastLikeGoods, "");
        }
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

                if (mData.isLiked())
                {
                	cancelLikeIt();
                }
                else
                {
                	likeIt();
                }
            }
        });
        
        
		mEnablePrice.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (MODE_PICK == mUIMode) {
					mData.pick(arg1);

					if (arg1) {
						
						likeIt();
						
						mChangeGoodsNum.setVisibility(View.VISIBLE);
						mLikeImageView.setVisibility(View.INVISIBLE);
					} else {
						mChangeGoodsNum.setVisibility(View.INVISIBLE);
						mLikeImageView.setVisibility(View.VISIBLE);
					}
				}
			}
		});
        
        mAddButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				 mData.setOrderNumber(mData.getOrderNumber() + 1);
				 mCurNumber.setText(Integer.toString(mData.getOrderNumber()));
				
			}
        	
        });
        
        mMinusButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				 int number = mData.getOrderNumber() -1;
				 if(number < 0)
					 number = 0;
				 
				 mData.setOrderNumber(number);
				 mCurNumber.setText(Integer.toString(mData.getOrderNumber()));
			}
        	
        });
    }
    

}
