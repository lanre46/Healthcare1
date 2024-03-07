package com.example.myapplication23;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    // Notification channel ID and name
    private static final String CHANNEL_ID = "appointment_reminder_channel";
    private static final CharSequence CHANNEL_NAME = "Appointment Reminder";

    public static void showNotification(Context context, String title, String message) {
        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications_fill0_wght400_grad0_opsz24) //set notification icon
                .setContentTitle(title) //set notification title
                .setContentText(message) //set notification message
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set notification priority

        // Get the notification manager system service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Check if device is running Android Oreo (API 26) or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create a notification channel
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            //display the notification
            notificationManager.notify(0, builder.build());
        }
    }
}
