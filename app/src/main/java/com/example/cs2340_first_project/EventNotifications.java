package com.example.cs2340_first_project;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class EventNotifications extends BroadcastReceiver {

    public static final int notificationID = 1;
    public static final String channelID = "Events";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.schedule_icon)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification);
    }
}
