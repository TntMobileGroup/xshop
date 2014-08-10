package com.tom.xshop.order;

public class OrderFeedback {
	
	public  float OrigPrice = 0.0f;
	public  float DiscountPrice = 0.0f;
	private float Discount = 1.0f;
	
	String Msg = null;
	public float getDiscount() {
		if(OrigPrice > 0)
			return DiscountPrice / OrigPrice;
			
		return 1.0f;
	}
	
	public String getMsg() {
		return Msg;
	}
}
