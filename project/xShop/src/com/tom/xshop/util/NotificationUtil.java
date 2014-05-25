package com.tom.xshop.util;

import com.tom.xshop.GalleryActivity;
import com.tom.xshop.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

public class NotificationUtil {

    public static void sendNotification(Context context, String title, String content)
    {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // get system service
        Notification n = new Notification();
        n.icon = R.drawable.like;
        n.tickerText = content;
        n.when = System.currentTimeMillis();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        n.sound = alarmSound;
//        n.flags=Notification.FLAG_ONGOING_EVENT;
        Intent intent = new Intent(context, GalleryActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        n.setLatestEventInfo(context, title, content, pi);
        nm.notify(1, n);
    }
}
