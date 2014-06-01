package com.tom.xshop.cloud.api.demo;

import android.app.Dialog;
import android.content.Context;
import android.widget.RelativeLayout;

public class APIDemoDialog extends Dialog {

    private RelativeLayout mContent = null;

    private static APIDemoDialog instance = null;
    
    public static APIDemoDialog getInstance(Context context)
    {
        if (null == instance)
        {
            instance = new APIDemoDialog(context);
        }
        return instance;
    }
    
    public APIDemoDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public APIDemoDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public APIDemoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public void createUI(Context context)
    {
        mContent = new RelativeLayout(context);
        //mContent.c
    }
}
