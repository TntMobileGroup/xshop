package com.tom.xshop.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;

import com.tom.xshop.MainActivity;
import com.tom.xshop.R;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.gallery.ui.GoodsItemDetailView;
import com.tom.xshop.gallery.ui.RecyclingImageView;
import com.tom.xshop.gallery.util.ImageFetcher;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class StickyListViewAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;

    private int[] mSectionIndices;
    private Object[] mSectionLetters;
    private LayoutInflater mInflater;
    
    private ArrayList<String> mCategories = null;
    private ArrayList<GoodsItem> mGoodsList = null;
    
    public StickyListViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mGoodsList = GlobalData.getData().getAllGoodsList();
        mCategories = GlobalData.getData().getAllCategories();
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        sectionIndices.add(0);
        
    	int count = mCategories.size();
    	for(int i = 0; i < count - 1; i++) {
    		int itemCount =  GlobalData.getData().getGoodsListByCategory(mCategories.get(i)).size();
    		sectionIndices.add(itemCount);
    	}
    	
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Object[] getSectionLetters() {
    	
        return mCategories.toArray();
    }

    @Override
    public int getCount() {
        return mGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsItemDetailView itemView = null;
        
        if (convertView == null) { // if it's not recycled, instantiate and initialize
            itemView = new GoodsItemDetailView(parent.getContext());
        } else { // Otherwise re-use the converted view
            itemView = (GoodsItemDetailView) convertView;
        }

        itemView.setUIMode(GoodsItemDetailView.MODE_SHOW);
        SlideViewAdapter adapter = new SlideViewAdapter(position);
        
        GoodsItem data = mGoodsList.get(position);
        itemView.setDataWithAdapter(data, adapter);
        
        return itemView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        CharSequence headerChar = mGoodsList.get(position).getCategory();
        holder.text.setText(headerChar);

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return mGoodsList.get(position).getCategory().charAt(0);
    	//return position;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
//        mCountries = new String[0];
//        mSectionIndices = new int[0];
//        mSectionLetters = new Character[0];
//        notifyDataSetChanged();
    }

    public void restore() {
    	mGoodsList = GlobalData.getData().getGoodsListByCategory(GlobalData.getData().getCurCategory());
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
    
    
    
    public  class SlideViewAdapter extends PagerAdapter {

    	private int mPosition;
    	//private GoodsItem data;
    	private ImageFetcher mImageFetcher;
        public SlideViewAdapter(int position) {
            super();
            
            mPosition = position;
            //data = mGoodsList.get(mPosition);
            mImageFetcher = MainActivity.getInstance().getImageFetcher();
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
