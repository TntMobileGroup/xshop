
package com.tom.xshop.config;

import java.util.HashMap;

public class PrefConfig {
    public final static String Key_CurClientID = "current_cid";
    public final static String Key_GoodsUUID = "goods_uuid";
    public final static String Key_EventTitle = "event_title";
    public final static String Key_EventMessage = "event_message";

    public static HashMap<String, Object> prefConfigMap = new HashMap<String, Object>();

    public static void init()
    {
        prefConfigMap.put(Key_CurClientID, "");
        prefConfigMap.put(Key_GoodsUUID, "");
        prefConfigMap.put(Key_EventMessage, "");
    }
}
