package com.tom.xshop.order;

import java.util.ArrayList;

import com.tom.xshop.data.GlobalData;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class OrderManager {

	private static OrderManager mInstance = null;
	private ArrayList<Order> mOrderListOfToday = new ArrayList<Order>();
	private InquiryListAdapter mInquiryAdapter = new InquiryListAdapter();
	
	private Order  mCurOrder = new Order();
	
	private OrderManager() {
		
		mOrderListOfToday.add(mCurOrder);
	}

	public static OrderManager Instance() {
		if (mInstance == null)
			mInstance = new OrderManager();

		return mInstance;
	}
	
	
	public void notifyOrderDataChanged(int msg, String itemId) {
		switch (msg) {
		case Order.ORDER_GOODSITEM_ADD:
			handleItemAdded(itemId);
			break;
		case Order.ORDER_GOODSITEM_REMOVE:
			handleItemRemoved(itemId);
			break;
		case Order.ORDER_GOODSITEM_ORDERNUMBER_CHANGED:
			handleItemOrderNumberChanged(itemId);
			break;
		default:
			if(mInquiryAdapter != null)
				mInquiryAdapter.notifyDataSetChanged();
		}
	}
	
	
	private void handleItemAdded(String itemId) {
		if(GlobalData.getData().getGoodsItem(itemId) != null)
			mCurOrder.orderGoodItem(GlobalData.getData().getGoodsItem(itemId));
		
		if(mInquiryAdapter != null)
			mInquiryAdapter.notifyDataSetChanged();
	}
	
	private void handleItemRemoved(String itemId) {
		if(GlobalData.getData().getGoodsItem(itemId) != null)
			mCurOrder.cancelOrderGoodsItem(GlobalData.getData().getGoodsItem(itemId));
		
		if(mInquiryAdapter != null)
			mInquiryAdapter.notifyDataSetChanged();
	}
	
	private void handleItemOrderNumberChanged(String itemId) {
		if(GlobalData.getData().getGoodsItem(itemId) != null)
			mCurOrder.changeOrderItemNumber(GlobalData.getData().getGoodsItem(itemId));
		
		if(mInquiryAdapter != null)
			mInquiryAdapter.notifyDataSetChanged();
	}
	
	
//	public BaseAdapter getOrderListAdapter() {
//		
//	}
	public InquiryListAdapter getInquiryListAdapter() {
		return mInquiryAdapter;
	}

	public class InquiryListAdapter extends BaseAdapter {

		public InquiryListAdapter() {
			super();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mOrderListOfToday.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mOrderListOfToday.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			OrderView itemView = null;

			if (convertView == null) { // if it's not recycled, instantiate and
										// initialize
				itemView = new OrderView(parent.getContext());
			} else { // Otherwise re-use the converted view
				itemView = (OrderView) convertView;
			}
			
			
			itemView.updateView(mOrderListOfToday.get(position));

			return itemView;
		}

	}
}
