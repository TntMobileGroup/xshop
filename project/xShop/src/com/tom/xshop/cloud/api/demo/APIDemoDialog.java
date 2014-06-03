package com.tom.xshop.cloud.api.demo;

import com.tom.xshop.cloud.CloudAPIAsyncTask;
import com.tom.xshop.cloud.api.CloudAPIUtil;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class APIDemoDialog extends Dialog {

    public final static String APIDemoLabel = "Cloud API Demo";
    private LinearLayout mContent = null;

    private static APIDemoDialog instance = null;
    
    public static APIDemoDialog getInstance(Context context)
    {
        if (null == instance)
        {
            instance = new APIDemoDialog(context);
        }
        return instance;
    }
    
    public static void destroy()
    {
        instance = null;
    }
    
    public APIDemoDialog(Context context) {
        super(context);
        initialize(context);
    }

    public APIDemoDialog(Context context, int theme) {
        super(context, theme);
        initialize(context);
    }

    public APIDemoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize(context);
    }
    
    private void initialize(Context context)
    {
        createUI(context);
        addListeners();
    }

    private Button mGetAllProductInfoBtn = null;
    private Button mGetAllValidMessagesBtn = null;
    private Button mGetProductListOfCartBtn = null;
    private Button mRequestDiscountBtn = null;
    private Button mGetReplyBtn = null;
    private Button mNegotiationBtn = null;
    private Button mGetDiscountQRCodeBtn = null;
    
    private void createUI(Context context)
    {
        this.setTitle("Web API Demo");
        mContent = new LinearLayout(context);
        mContent.setOrientation(LinearLayout.VERTICAL);
        this.setContentView(mContent);
        
        mGetAllProductInfoBtn = new Button(context);
        mGetAllProductInfoBtn.setText("Get All Product Info");
        mContent.addView(mGetAllProductInfoBtn);
        
        mGetAllValidMessagesBtn = new Button(context);
        mGetAllValidMessagesBtn.setText("Get All Valid Messages");
        mContent.addView(mGetAllValidMessagesBtn);
        
        mGetProductListOfCartBtn = new Button(context);
        mGetProductListOfCartBtn.setText("Get Product List of Cart");
        mContent.addView(mGetProductListOfCartBtn);
        
        mRequestDiscountBtn = new Button(context);
        mRequestDiscountBtn.setText("Request A Discount");
        mContent.addView(mRequestDiscountBtn);
        
        mGetReplyBtn = new Button(context);
        mGetReplyBtn.setText("Get Reply");
        mContent.addView(mGetReplyBtn);
        
        mNegotiationBtn = new Button(context);
        mNegotiationBtn.setText("Negotiation");
        mContent.addView(mNegotiationBtn);
        
        mGetDiscountQRCodeBtn = new Button(context);
        mGetDiscountQRCodeBtn.setText("Get Discount QR Code");
        mContent.addView(mGetDiscountQRCodeBtn);
    }
    
    private void addListeners()
    {
        addListenerForBtn(mGetAllProductInfoBtn, "GetAllProductInfo");
        addListenerForBtn(mGetAllValidMessagesBtn, "GetAllValidMessages");
        addListenerForBtn(mGetProductListOfCartBtn, "GetProductListOfCart");
        addListenerForBtn(mRequestDiscountBtn, "RequestDiscount");
        addListenerForBtn(mGetReplyBtn, "GetReply");
        addListenerForBtn(mNegotiationBtn, "Negotiation");
        addListenerForBtn(mGetDiscountQRCodeBtn, "GetDiscountQRCode");
    }
    
    private void addListenerForBtn(final Button btn, final String op)
    {
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CloudAPIUtil.getInstance().invokeAPI(op, new CloudAPIAsyncTask.ICloudAPITaskListener()
                {

                    @Override
                    public void onFinish(int returnCode, String strResult) {
                        Toast.makeText(btn.getContext(), strResult, Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}
