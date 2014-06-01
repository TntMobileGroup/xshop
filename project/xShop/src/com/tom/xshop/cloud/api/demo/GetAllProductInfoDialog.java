package com.tom.xshop.cloud.api.demo;

import android.app.Dialog;
import android.content.Context;

public class GetAllProductInfoDialog extends Dialog {

    public GetAllProductInfoDialog(Context context) {
        super(context);
        initialize(context);
    }

    public GetAllProductInfoDialog(Context context, int theme) {
        super(context, theme);
        initialize(context);
    }

    public GetAllProductInfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize(context);
    }

    private void initialize(Context context)
    {
        createUI(context);
    }
    
    private void createUI(Context context)
    {
        
    }
}
