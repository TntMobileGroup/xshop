package com.tom.xshop.data;

public class ItemOrderData {
	private boolean isPicked = false;
	private int     orderNumber = 1;
	
	
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
	
}
