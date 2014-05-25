
package com.tom.xshop.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tom.xshop.data.GlobalData;
import com.tom.xshop.data.GoodsItem;

public class JSONUtil {
    public static void parseGoodsListInfo(String strJSON) {
        try {
            JSONArray jsonArray = new JSONArray(strJSON);
            int length = jsonArray.length();
            ArrayList<GoodsItem> goodsList = GlobalData.getData().getAllGoodsList();
            goodsList.clear();
            for (int i = 0; i < length; ++i) {
                JSONObject jsonItem = (JSONObject) jsonArray.get(i);
                if (null != jsonItem)
                {
                    GoodsItem goods = new GoodsItem(jsonItem);
                    goodsList.add(goods);
                }
            }
            
            GlobalData.getData().generateStructuredData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
