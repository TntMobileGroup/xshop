
package com.tom.xshop;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.Consts;
import com.tom.xshop.config.PrefConfig;
import com.tom.xshop.util.NotificationUtil;
import com.tom.xshop.util.PrefUtil;

public class GexinSdkMsgReceiver extends BroadcastReceiver {

    public static final String Key_IntentToEveryone = "is_to_everyone";
    public static final String Key_IntentToMyType = "is_to_mytype";
    public static final String Key_IntentUserName = "user_name";
    public static final String Key_IntentMessage = "message_body";
    public static final String Key_IntentIsUrgent = "is_urgent";

    public static String cid = "";
    
    @Override
    public void onReceive(final Context context, Intent intent) {
        PrefUtil.init(context);
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(Consts.CMD_ACTION)) {

            case Consts.GET_MSG_DATA:
                try
                {
                    byte[] payload = bundle.getByteArray("payload");

                    if (payload != null) {
                        String dataStr = new String(payload, "UTF-8");
                        StringTokenizer tokens = new StringTokenizer(dataStr, "|");
                        if (tokens.countTokens() < 4)
                        {
                            return;
                        }
                        String versionStr = tokens.nextToken();
                        double version = Double.parseDouble(versionStr);
                        if (version < 1.0)
                        {
                            return;
                        }
                        String title = tokens.nextToken();
                        String content = tokens.nextToken();
                        String goodsUUID = tokens.nextToken();
                        
                        PrefUtil.putStringValue(PrefConfig.Key_GoodsUUID, goodsUUID);
                        PrefUtil.putStringValue(PrefConfig.Key_EventMessage, content);
                        
                        NotificationUtil.sendNotification(context, title, content);
                    }
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
            case Consts.GET_CLIENTID:
                cid = bundle.getString("clientid");
                String lastCID = PrefUtil.getStringValue(PrefConfig.Key_CurClientID);
                if (lastCID.length() < 1 && cid.length() > 0)
                {
//                    MainActivity app = MainActivity.getApp();
//                    if (null != app)
//                    {
//                        app.setSystemTipText("Registration completed");
//                    }
                }
                PrefUtil.putStringValue(PrefConfig.Key_CurClientID, cid);
                break;

            case Consts.THIRDPART_FEEDBACK:
//                String appid = bundle.getString("appid");
//                String taskid = bundle.getString("taskid");
//                String actionid = bundle.getString("actionid");
//                String result = bundle.getString("result");
//                long timestamp = bundle.getLong("timestamp");
//
//                Log.d("GexinSdkDemo", "appid:" + appid);
//                Log.d("GexinSdkDemo", "taskid:" + taskid);
//                Log.d("GexinSdkDemo", "actionid:" + actionid);
//                Log.d("GexinSdkDemo", "result:" + result);
//                Log.d("GexinSdkDemo", "timestamp:" + timestamp);
                break;
            default:
                break;
        }
    }
}
