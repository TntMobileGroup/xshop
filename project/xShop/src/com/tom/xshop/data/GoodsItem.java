
package com.tom.xshop.data;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsItem {
    private final static String THUMB_URL_PREFIX = "http://115.28.107.154/xshop/api/data/thumbs/";
    private final static String FULLIMAGE_URL_PREFIX = "http://115.28.107.154/xshop/api/data/images/";
    private String mId = "";
    private String mUuid = "";
    private String mName = "";
    private String mCategory = "";
    private String mBrand = "";
    private String mModel = "";
    private String mDescription = "";
    private String mThumbUrl = "";
    private String mFullImageUrl = "";
    private String mMessage = "";
    private boolean mLiked = false;
    private boolean mFocus = false;

    private ItemOrderData mOrderData = null;
    
    public GoodsItem(JSONObject jsonObj) throws JSONException
    {
        mId = jsonObj.getString("id");
        mUuid = jsonObj.getString("uuid");
        mName = jsonObj.getString("name");
        mCategory = jsonObj.getString("category");
        mBrand = jsonObj.getString("brand");
        mModel = jsonObj.getString("model");
        mDescription = jsonObj.getString("description");
        mThumbUrl = THUMB_URL_PREFIX + mUuid + ".png";
        mFullImageUrl = FULLIMAGE_URL_PREFIX + mUuid + ".png";
    }
    
    public boolean getFocus()
    {
        return mFocus;
    }
    
    public String getMessage()
    {
        return mMessage;
    }
    
    public String getId()
    {
        return mId;
    }
    
    public String getUuid()
    {
        return mUuid;
    }
    
    public String getName()
    {
        return mName;
    }
    
    public String getCategory()
    {
        return mCategory;
    }
    
    public String getBrand()
    {
        return mBrand;
    }
    
    public String getModel()
    {
        return mModel;
    }
    
    public String getDescription()
    {
        return mDescription;
    }
    
    public String getThumbUrl()
    {
        return mThumbUrl;
    }
    
    public String getFullImageUrl()
    {
        return mFullImageUrl;
    }
    
    public ItemOrderData getItemOrderData() {
    	return mOrderData;
    }
    
    public void like(boolean like)
    {
        mLiked = like;
        if(mOrderData == null) {
        	mOrderData = new ItemOrderData(mUuid);
        }
    }
    
    
    public boolean isLiked()
    {
        return mLiked;
    }
    
    public void setFocus(boolean focus)
    {
        mFocus = focus;
    }
    
    public void setMessage(String message)
    {
        mMessage = message;
    }
    
    
    public boolean isPicked() {
    	if(!isLiked())
    		return false;
    	
    	if(mOrderData == null)
    		return false;
    	
    	return mOrderData.isPicked();
    }
    
    
    public void pick(boolean pick) {
    	if(mOrderData != null) {
    		mOrderData.pick(pick);
    	}
    }
    
    
    public int getOrderNumber() {
    	if(mOrderData != null) {
    		return mOrderData.getOrderNumber();
    	}
    	
    	return 0;
    }
    
    
    public void setOrderNumber(int number) {
    	if(mOrderData != null) {
    		mOrderData.setOrderNumber(number);
    	}
    }
}
