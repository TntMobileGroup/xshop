
package com.tom.xshop.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.tom.xshop.util.CacheUtil;

public class GlobalData {
    public static final String FavoriteCategory = "Favorite";
    private String mCurCategory = "";
    private ArrayList<GoodsItem> mAllGoodsList = null;
    private ArrayList<String> mAllCategories = null;
    private HashMap<String, ArrayList<GoodsItem>> mStructuredData = null;
    private static GlobalData _instance = null;
    
    private ArrayList<GoodsItem> mLikedGoodsList = null;
    private GoodsDataChangeListener mLikedGoodsListener = null;

    public static GlobalData getData()
    {
        if (null == _instance)
        {
            _instance = new GlobalData();
        }
        return _instance;
    }

    public GlobalData()
    {
        mAllGoodsList = new ArrayList<GoodsItem>();
        mLikedGoodsList = new ArrayList<GoodsItem>();
        mStructuredData = new HashMap<String, ArrayList<GoodsItem>>();
        mAllCategories = new ArrayList<String>();
    }
    
    public ArrayList<GoodsItem> getAllGoodsList()
    {
        return mAllGoodsList;
    }
    
    public void setCurCategory(String category)
    {
        mCurCategory = category;
    }
    
    public String getCurCategory()
    {
        return mCurCategory;
    }
    
    public void generateStructuredData()
    {
        mStructuredData.clear();
        mAllCategories.clear();
        for (GoodsItem item : mAllGoodsList)
        {
            String category = item.getCategory();
            if (mStructuredData.containsKey(category))
            {
                mStructuredData.get(category).add(item);
                continue;
            }
            ArrayList<GoodsItem> list = new ArrayList<GoodsItem>();
            mStructuredData.put(category, list);
            list.add(item);
            mAllCategories.add(category);
        }
        mAllCategories.add(FavoriteCategory);
        mStructuredData.put(FavoriteCategory, mLikedGoodsList);

        String likedData = CacheUtil.loadCachedData(CacheUtil.Key_liked);
        updateFavoriteData(likedData);
    }

    public ArrayList<String> getAllCategories()
    {
        return mAllCategories;
    }
    
    public ArrayList<GoodsItem> getGoodsListByCategory(String category)
    {
        return mStructuredData.get(category);
    }
    
    public void updateLikedList(boolean cache)
    {
        String data = "";
        mLikedGoodsList.clear();
        for (GoodsItem item : mAllGoodsList)
        {
            if (item.isLiked())
            {
                mLikedGoodsList.add(item);
                data += String.valueOf(item.getId()) + ",";
            }
        }
        if (cache)
        {
            CacheUtil.cache(CacheUtil.Key_liked, data);
        }
        
        if(mLikedGoodsListener != null) {
        	mLikedGoodsListener.notifyDataSetChanged();
        }
    }
    
    public void setFavorGoodsDataChangeListener(GoodsDataChangeListener listener) {
    	mLikedGoodsListener = listener;
    }
    
    public String getCategoryByGoods(String uuid)
    {
        for (GoodsItem item : mAllGoodsList)
        {
            if (item.getUuid().equalsIgnoreCase(uuid))
            {
                return item.getCategory();
            }
        }
        return "";
    }
    
    public void updateFocusData(String uuid, String message)
    {
        for (GoodsItem item : mAllGoodsList)
        {
            if (item.getUuid().equalsIgnoreCase(uuid))
            {
                item.setFocus(true);
                item.setMessage(message);
            }
        }
    }
    
    public ArrayList<GoodsItem> getLikedList()
    {
        return getGoodsListByCategory(FavoriteCategory);
    }
    
    
    public GoodsItem getGoodsItem(String uuid) {
        for (GoodsItem item : mAllGoodsList)
        {
            if (item.getUuid().equalsIgnoreCase(uuid))
            {
            	return item;
            }
         }
        
        return null;
    }
//    public String getFavoriteData()
//    {
//        String data = "";
//        for (GoodsItem item : mAllGoodsList)
//        {
//            if (item.isLiked())
//            {
//                data += String.valueOf(item.getId()) + ",";
//            }
//        }
//        return data;
//    }
    
    public void updateFavoriteData(String data)
    {
        StringTokenizer tokens = new StringTokenizer(data, ",");
        ArrayList<String> table = new ArrayList<String>();
        while (tokens.hasMoreTokens())
        {
            String id = tokens.nextToken();
            table.add(id);
        }
        
        int count = mAllGoodsList.size();
        for (int i = 0; i < count; ++i)
        {
            GoodsItem item = mAllGoodsList.get(i);
            if (table.contains(item.getId()))
            {
                item.like(true);
            }
        }
        updateLikedList(false);
    }
}
