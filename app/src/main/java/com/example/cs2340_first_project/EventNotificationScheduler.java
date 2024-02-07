package com.example.cs2340_first_project;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

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
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll();
        }

        Intent intent = new Intent(context, EventNotificationReceiver.class);
        intent.putExtra("eventId", eventId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) eventId, intent, PendingIntent.FLAG_UPDATE_CURRENT);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            for (int i=0; i<7;++i) {
                currentDate = currentDate.plusDays(1);
                if (days[i + currentDate.getDayOfWeek().getValue() - 1]) {
                    LocalDateTime notificationTime = calculateNotificationTime(startTime, currentDate);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.toInstant(ZoneOffset.UTC).toEpochMilli(), pendingIntent);
                }
            }
        }

    }

    private static LocalDateTime calculateNotificationTime(String startTimeString, LocalDate date) {
        if (!(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)) return null;

        LocalTime startTime = LocalTime.parse(startTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime dateTime = LocalDateTime.of(date, startTime);
        return dateTime;
    }

    public static class EventNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int eventId = intent.getIntExtra("eventId", -1);

            if (eventId != -1) {
                showNotification(context, eventId);
            }
        }

        private void showNotification(Context context, int eventId) {
            // Build and show notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager == null) {
                return;
            }

            // Create Notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(context, CalendarDetailActivity.class);
            intent.putExtra("eventId", eventId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) eventId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String eventTitle = Event.findEventByID(eventId).getTitle();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.schedule_icon)
                    .setContentTitle(eventTitle)
                    .setContentText(eventTitle + " is starting soon.")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify((int) eventId, builder.build());


            // set notification for next week

            rescheduleEventNotification(context, eventId);

        }


        public static void rescheduleEventNotification(Context context, int eventId) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, EventNotificationReceiver.class);
            intent.putExtra("eventId", eventId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) eventId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime notificationTime = LocalDateTime.now().plusWeeks(1);
                alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.toInstant(ZoneOffset.UTC).toEpochMilli(), pendingIntent);
            }
            if (alarmManager != null) {
            }
        }
    }
}
