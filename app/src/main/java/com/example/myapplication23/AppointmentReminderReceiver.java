package com.example.myapplication23;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppointmentReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        // Show a notification reminding the user about their appointment
        NotificationHelper.showNotification(context, "Appointment Reminder", "Don't forget your appointment!");
    }
}