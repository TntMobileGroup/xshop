package com.tom.xshop.ui.navigation;

import java.util.ArrayList;

import com.tom.xshop.cloud.api.demo.APIDemoDialog;
import com.tom.xshop.data.GlobalData;
import com.tom.xshop.ui.control.ControlFactory;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NavigationMenu extends ScrollView {

    private LinearLayout mContent = null;
    
    public NavigationMenu(Context context) {
        super(context);
        createUI(context);
    }

    public void clear() {
        mContent.removeAllViews();
    }
    
    public void createUI(Context context)
    {
        mContent = new LinearLayout(context);
        mContent.setOrientation(LinearLayout.VERTICAL);
        this.addView(mContent);
    }
    
    public void refreshMenu()
    {
        Context context = this.getContext();
        clear();
        ArrayList<String> categories = (ArrayList<String>) GlobalData.getData().getAllCategories().clone();
        categories.add(0, "[Help]");
        categories.add(1, "[Settings]");
        categories.add(2, APIDemoDialog.APIDemoLabel);
        categories.add(3, "[Update]");
        categories.add("[About Us]");
        boolean isFirst = true;
        for (String category : categories)
        {
            if (isFirst)
            {
                isFirst = false;
            }
            else
            {
                mContent.addView(ControlFactory.createHoriSeparatorForLinearLayout(
                        this.getContext()));
            }
            mContent.addView(new NavigationMenuItem(context, category));
        }
    }
}
