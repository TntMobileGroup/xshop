/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tom.xshop.gallery.ui;


import java.util.ArrayList;

import com.tom.xshop.R;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.gallery.util.ImageCache;
import com.tom.xshop.gallery.util.ImageFetcher;
import com.tom.xshop.order.OrderManager;
import com.tom.xshop.ui.thirdparty.SlidingUpPanel.SlidingUpPanelLayout;
import com.tom.xshop.ui.thirdparty.SlidingUpPanel.SlidingUpPanelLayout.PanelSlideListener;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Demonstration of using ListFragment to show a list of items
 * from a canned array.
 */
public class ImageListActivity extends FragmentActivity {
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_ORDER = "extra_order";
    private static ArrayList<GoodsItem> mGoodsList = null;
    private static ImageFetcher mImageFetcher;
    
    private SlidingUpPanelLayout mLayout;
    private static final String TAG = ImageListActivity.class.getName();
    public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";
    
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }
	
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	ArrayList<GoodsItem> goodsList = GlobalData.getData().getGoodsListByCategory(GlobalData.getData().getCurCategory());
    	mGoodsList = (ArrayList<GoodsItem>) goodsList.clone();
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        for(int index = 0; index < mGoodsList.size(); index++) {
        	mGoodsList.get(index).pick(false);
        }
        
        boolean orderLast = this.getIntent().getBooleanExtra(EXTRA_ORDER, false);
        if(orderLast) {
        	
        	int orderItem = this.getIntent().getIntExtra(EXTRA_IMAGE, -1);
        	if((mGoodsList.size() > orderItem) && orderItem >= 0) {
        		mGoodsList.get(orderItem).pick(true);
        	}
        }
        
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
        

        // Create the list fragment and add it as our sole content.
        if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
            ArrayListFragment list = new ArrayListFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, list).commit();
        }
        
        
        boolean actionBarHidden = savedInstanceState != null && savedInstanceState.getBoolean(SAVED_STATE_ACTION_BAR_HIDDEN, false);
        if (actionBarHidden) {
            int actionBarHeight = getActionBarHeight();
            setActionBarTranslation(-actionBarHeight);//will "hide" an ActionBar
        }
        
        this.getActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.actionbar_bkg));
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_STATE_ACTION_BAR_HIDDEN, mLayout.isPanelExpanded());
    }
    
    private int getActionBarHeight(){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void setActionBarTranslation(float y) {
        // Figure out the actionbar height
        int actionBarHeight = getActionBarHeight();
        // A hack to add the translation to the action bar
        ViewGroup content = ((ViewGroup) findViewById(android.R.id.content).getParent());
        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);
            if (child.getId() != android.R.id.content) {
                if (y <= -actionBarHeight) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        child.setTranslationY(y);
                    } else {
                        com.tom.xshop.ui.thirdparty.SlidingUpPanel.AnimatorProxy.wrap(child).setTranslationY(y);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null && mLayout.isPanelExpanded() || mLayout.isPanelAnchored()) {
            mLayout.collapsePanel();
        } else {
            super.onBackPressed();
        }
    }

    public  class ArrayListFragment extends ListFragment {

    	private int orderItem = -1;
    	private ListView mOrderList = null;
    	
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            
            setListAdapter(new ListAdapter());
            
            if(mOrderList != null) {
            	 //TODO: to be replaced.
            	mOrderList.setAdapter(null);
            }
            
            mOrderList.setAdapter(OrderManager.Instance().getInquiryListAdapter());
            orderItem = this.getActivity().getIntent().getIntExtra(EXTRA_IMAGE, -1);
            if(orderItem < 0)
          	orderItem = 0;
                                
            this.setSelection(orderItem);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
        
        
        @Override  
        public View onCreateView(LayoutInflater inflater, ViewGroup container,  
                    Bundle savedInstanceState) {  
                super.onCreateView(inflater, container, savedInstanceState);  
                View view = inflater.inflate(R.layout.image_list_fragment, container, false);  
                
                mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
                mLayout.setPanelSlideListener(new PanelSlideListener() {
                    @Override
                    public void onPanelSlide(View panel, float slideOffset) {
                        Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                        setActionBarTranslation(mLayout.getCurrentParalaxOffset());
                    }

                    @Override
                    public void onPanelExpanded(View panel) {
                        Log.i(TAG, "onPanelExpanded");

                    }

                    @Override
                    public void onPanelCollapsed(View panel) {
                        Log.i(TAG, "onPanelCollapsed");

                    }

                    @Override
                    public void onPanelAnchored(View panel) {
                        Log.i(TAG, "onPanelAnchored");

                    }
                });
                
                
                TextView t = (TextView) view.findViewById(R.id.name);
                t.setText(Html.fromHtml(getString(R.string.hello)));
                Button f = (Button) view.findViewById(R.id.follow);
                f.setText(Html.fromHtml(getString(R.string.follow)));
                f.setMovementMethod(LinkMovementMethod.getInstance());
                f.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
                        startActivity(i);
                    }
                });
                
                
                mOrderList = (ListView) view.findViewById(R.id.goods);
//                GridLayout grid = (GridLayout) view.findViewById(R.id.goods);
//                grid.setBackgroundColor(Color.GREEN);
//                orderItem = this.getActivity().getIntent().getIntExtra(EXTRA_IMAGE, -1);
//                if(orderItem < 0)
//                	orderItem = 0;
//                
//                if(mGoodsList.size() > orderItem)
//                	grid.addView(getGridItemView(orderItem));
                
                return view;  
        }
        
        
//        private View getGridItemView(int position) {
//        	
//            GoodsItemDetailView itemView = new GoodsItemDetailView(this.getActivity());
//            itemView.setUIMode(GoodsItemDetailView.MODE_ORDER);
//            SlideViewAdapter adapter = new SlideViewAdapter(position);
//            
//            GoodsItem data = mGoodsList.get(position);
//            itemView.setDataWithAdapter(data, adapter);
//            
//            return itemView;
//        }
    }
    
    public  class ListAdapter extends BaseAdapter {
    	 
        public ListAdapter() {
            super();
        }
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mGoodsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mGoodsList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
            GoodsItemDetailView itemView = null;
            
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                itemView = new GoodsItemDetailView(parent.getContext());
            } else { // Otherwise re-use the converted view
                itemView = (GoodsItemDetailView) convertView;
            }
    
            itemView.setUIMode(GoodsItemDetailView.MODE_PICK);
            
            GoodsItem data = mGoodsList.get(position);
            SlideViewAdapter adapter = new SlideViewAdapter(data.getUuid());
            itemView.setDataWithAdapter(data, adapter);
            
            
            return itemView;
		}
    	
    }
    
    
    
    public static class SlideViewAdapter extends PagerAdapter {

    	private GoodsItem data;
        public SlideViewAdapter(String itemId) {
            super();
            
            data = GlobalData.getData().getGoodsItem(itemId);
        }

        @Override
        public int getCount() {
            return 1;
        }
     
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RecyclingImageView) object);
        }
         
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        	
          RecyclingImageView itemView = new RecyclingImageView(container.getContext());
          ((ViewPager) container).addView(itemView);
          
          if(position == 0)
          {
              String url = data.getFullImageUrl();
              mImageFetcher.loadImage(url, itemView);
          }

          return itemView;
        }
         
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RecyclingImageView) object);
      
        }
        

    	
    }
}
