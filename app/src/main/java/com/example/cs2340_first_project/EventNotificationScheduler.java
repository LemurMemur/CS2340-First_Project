package com.example.cs2340_first_project;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class EventNotificationScheduler {

    private static final String CHANNEL_ID = "event_notification_channel";
    private static final String CHANNEL_NAME = "Event Notifications";

    public static void scheduleEventNotification(Context context, String startTime, boolean[] days, Event event) {
        int eventId = event.getID();
        String eventTitle = event.getTitle();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            LocalDateTime currDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm")));
            for (int i=0; i<7; ++i) {
                if (days[(i + currDateTime.getDayOfWeek().getValue() - 1)%7]) {
                    scheduleNotification(context, eventTitle, eventTitle + " soon!", currDateTime.plusMinutes(-60));
                }
            }
        }


    }
    @RequiresPermission(value = "android.permission.SCHEDULE_EXACT_ALARM", conditional = true)
    public static void scheduleNotification(Context context, String title, String message, LocalDateTime time) {
        Intent intent = new Intent(context, Notification.class);
        intent.putExtra(EventNotifications.titleExtra, title);
        intent.putExtra(EventNotifications.messageExtra, message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                EventNotifications.notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager)  context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        time.toInstant(ZoneOffset.UTC).toEpochMilli(),
                        pendingIntent
                );
            }
        }


    }
}
