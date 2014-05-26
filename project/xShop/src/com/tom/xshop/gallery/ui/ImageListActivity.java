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

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Demonstration of using ListFragment to show a list of items
 * from a canned array.
 */
public class ImageListActivity extends FragmentActivity {
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";
    private static ArrayList<GoodsItem> mGoodsList = null;
    private static ImageFetcher mImageFetcher;
    
    
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	mGoodsList = GlobalData.getData().getGoodsListByCategory(GlobalData.getData().getCurCategory());
        super.onCreate(savedInstanceState);
        
        
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
        
    }

    public  class ArrayListFragment extends ListFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            
            setListAdapter(new ListAdapter());
            
            int extraCurrentItem = this.getActivity().getIntent().getIntExtra(EXTRA_IMAGE, -1);
            this.setSelection(extraCurrentItem);
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
                return view;  
        } 
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
    
            SlideViewAdapter adapter = new SlideViewAdapter(position);
            
            GoodsItem data = mGoodsList.get(position);
            itemView.setDataWithAdapter(data, adapter);
            
            return itemView;
		}
    	
    }
    
    
    
    public  class SlideViewAdapter extends PagerAdapter {

    	private int mPosition;
    	private GoodsItem data;
        public SlideViewAdapter(int position) {
            super();
            
            mPosition = position;
            data = mGoodsList.get(mPosition);
        }

        @Override
        public int getCount() {
            return 2;
        }
     
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RecyclingImageView) object);
        }
         
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        	
          RecyclingImageView itemView = new RecyclingImageView(container.getContext());
          ((ViewPager) container).addView(itemView);
          
          if(position == 1)
          {
              GoodsItem data = mGoodsList.get(position);
              String url = data.getFullImageUrl();
              mImageFetcher.loadImage(url, itemView);
          }
          else
          {
          GoodsItem data = mGoodsList.get(mPosition);
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
