package com.tom.xshop.order;

import java.util.ArrayList;

import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.data.ItemOrderData;


public class Order {
	
	public static final int ORDER_GOODSITEM_ADD = 0;
	public static final int ORDER_GOODSITEM_REMOVE = 1;
	public static final int ORDER_GOODSITEM_ORDERNUMBER_CHANGED = 2;
	
	public static final int ORDER_GOODITEM_PICKING = 6;
	public static final int ORDER_INQUIRY_SEND = 7;
	public static final int ORDER_INQUIRY_RESEND = 8;	
	public static final int ORDER_INQUIRY_SELLER_FEEDBACK_WAIT     = 9;
	public static final int ORDER_INQUIRY_SELLER_FEEDBACK_RECEIVED = 10;
	
	public static final int ORDER_OFFER_SEND     = 11;
	public static final int ORDER_OFFER_RESEND   = 12;
	public static final int ORDER_OFFER_SELLER_FEEDBACK_WAIT     = 13;
	public static final int ORDER_OFFER_SELLER_FEEDBACK_RECEIVED = 14;
	
	
	int mOrderID; // Order ID should be obtained from server.
	int mOrderStatus;
	
	String mOrderDate;
	String mOrderTime;
	
	
	
	private ArrayList<ItemOrderData> mList = null;
	private OrderFeedback mOrderFeedback = null;
	
	
	public Order() {
		
		init();
	}
	
	
	private void init() {
		mList = new ArrayList<ItemOrderData>();
		mOrderFeedback = new OrderFeedback();
	}
	
	public ArrayList<ItemOrderData> getList() {
		return mList;
	}
	
	public void orderGoodItem(GoodsItem item) {
		if(hasItem(item))
			return;
		
		mList.add(new ItemOrderData(item.getItemOrderData()));
		
	}
	
	
	public void cancelOrderGoodsItem(GoodsItem item) {
		if(!hasItem(item))
			return;
		
		int index = 0;
		for(; index < mList.size(); index++) {
			if(mList.get(index).getID().equalsIgnoreCase(item.getUuid())){
				break;
			}
		}
		
		mList.remove(index);
	}
	
	
	public void changeOrderItemNumber(GoodsItem item) {
		if(!hasItem(item))
			return;
		
		int index = 0;
		for(; index < mList.size(); index++) {
			if(mList.get(index).getID().equalsIgnoreCase(item.getUuid())){
				break;
			}
		}
		
		ItemOrderData data = mList.get(index);
		data.setOrderNumber(item.getOrderNumber());
	}
	
	
	private boolean hasItem(GoodsItem item) {
		boolean has = false; 
		for(int index = 0; index < mList.size(); index++) {
			if(mList.get(index).getID().equalsIgnoreCase(item.getUuid())){
				has = true;
				break;
			}
		}
		
		return has;
	}
//	@Override
//	public boolean equals (Object o) {
//		
//	}
}
