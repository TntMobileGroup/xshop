package com.tom.xshop.order;

import java.util.ArrayList;

import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;
import com.tom.xshop.data.ItemOrderData;
import com.tom.xshop.gallery.ui.GoodsItemDetailView;
import com.tom.xshop.gallery.ui.ImageListActivity.SlideViewAdapter;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class OrderView extends LinearLayout {

	private GridLayout mOrderGridLayout = null;
	private ScrollView mGridContainer = null;
	private InquiryView mInquiry = null;

	public OrderView(Context context) {
		super(context);

		initUI();
	}

	private void initUI() {
		mGridContainer = new ScrollView(this.getContext());
		mOrderGridLayout = new GridLayout(this.getContext());

		mInquiry = new InquiryView(this.getContext());

		mGridContainer.addView(mOrderGridLayout);
		this.addView(mGridContainer);
	}
	
	
	public void updateView(Order order) {
		ArrayList<ItemOrderData> list = order.getList();
		
		for(int index = 0; index < list.size(); index++) {
			mOrderGridLayout.addView(getGridItemView(list.get(index).getID()));
		}
	}
	
	
    private View getGridItemView(String itemId) {
    	
        GoodsItemDetailView itemView = new GoodsItemDetailView(this.getContext());
        itemView.setUIMode(GoodsItemDetailView.MODE_ORDER);
        SlideViewAdapter adapter = new SlideViewAdapter(itemId);
//        
        GoodsItem data = GlobalData.getData().getGoodsItem(itemId);
        itemView.setDataWithAdapter(data, adapter);
        
        return itemView;
    }

	private class InquiryView extends GridLayout {

		public InquiryView(Context context) {
			super(context);
			// TODO: implement inquiry view
		}

	}
}
