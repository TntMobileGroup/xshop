package com.tom.xshop.data;

public class ItemOrderData {
	private String   goodsID = null;
	private boolean isPicked = false;
	private int     orderNumber = 1;
	
	public ItemOrderData(String id) {
		goodsID = id;
	}
	
	
	public ItemOrderData(ItemOrderData src) {
		goodsID = new String(src.getID());
		isPicked = src.isPicked();
		orderNumber = src.getOrderNumber();
	}
	
	public void pick(boolean pick) {
		isPicked = pick;
	}
	
	public boolean isPicked() {
		return isPicked;
	}
	
	
	public void setOrderNumber(int number) {
		orderNumber = number;
		if(orderNumber < 0)
			orderNumber = 0;
	}
	
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	
	public String getID() {
		return goodsID;
	}
	
	
	public void setID(String id) {
		goodsID = id;
	}
	
	
}
