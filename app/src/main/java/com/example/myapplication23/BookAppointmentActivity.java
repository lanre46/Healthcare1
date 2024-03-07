package com.example.myapplication23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import java.util.Map;

public class BookAppointmentActivity extends AppCompatActivity {

    // Declare EditText, TextView, Button, and FirebaseFirestore
    EditText e1, e2, e3, e4;
    TextView tv;

    // Declare MaterialDatePicker and MaterialTimePicker
    private MaterialDatePicker<Long> materialDatePicker;
    private MaterialTimePicker materialTimePicker;

    private Button dateButton, timeButton, buttonBook, buttonBack;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();

        // Initialize EditText, TextView, Button, and Material Design components by finding their corresponding views in the layout
        tv = findViewById(R.id.textViewHRDetails);
        e1 = findViewById(R.id.FullbookingName);
        e2 = findViewById(R.id.Addressbooking);
        e3 = findViewById(R.id.Emailbooking);
        e4 = findViewById(R.id.phonebooking);
        dateButton = findViewById(R.id.buttonDate);
        timeButton = findViewById(R.id.buttonTime);
        buttonBook = findViewById(R.id.buybooking);
        buttonBack = findViewById(R.id.buttonBack);

        // Set EditTexts to be non-editable
        e1.setKeyListener(null);
        e2.setKeyListener(null);
        e3.setKeyListener(null);
        e4.setKeyListener(null);

        Intent it = getIntent();
        String title = it.getStringExtra("text1");
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String number = it.getStringExtra("text4");
        String fees = it.getStringExtra("text5");

        // Set text for TextView and EditTexts
        tv.setText(title);
        e1.setText(fullname);
        e2.setText(address);
        e3.setText(number);
        e4.setText("fees:" + fees + "/-");



        // Initialize Date Picker
        initDatePicker();
        // Set OnClickListener for Date Button
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        // Time Picker
        initTimePicker();
        // Set OnClickListener for Time Button
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTimePicker.show(getSupportFragmentManager(), "MATERIAL_TIME_PICKER");
            }
        });

        // OnClickListener for booking Button
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("UserName", "");

                // Create a Map for appointment details
                Map<String, Object> appointment = new HashMap<>();
                appointment.put("username", username);
                appointment.put("doctor", title);
                appointment.put("fullname", fullname);
                appointment.put("address", address);
                appointment.put("number", number);
                appointment.put("date", dateButton.getText().toString());
                appointment.put("time", timeButton.getText().toString());

                // Schedule a reminder for the appointment
                scheduleReminder();

                // Extract fees and set it in the appointment
                String strippedFees = fees.replaceAll("[^\\d.]", "");

                float feesValue = Float.parseFloat(strippedFees);

                // Add the appointment to Firestore
                appointment.put("fees", feesValue);
                appointment.put("type", "appointment");

                db.collection("appointments")
                        .add(appointment)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Show success message and navigate to HomeActivity
                                Snackbar.make(findViewById(android.R.id.content), "Your appointment has been booked successfully", Snackbar.LENGTH_LONG).show();
                                startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Show failure message
                                Snackbar.make(findViewById(android.R.id.content), "Failed to book appointment", Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });
        // OnClickListener for back Button

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //take you to FindDoctorActivity
                startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class));
            }
        });
    }

    // Method to initialize Date Picker
    private void initDatePicker() {
        materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateButton.setText(sdf.format(calendar.getTime()));
        });
    }

    // Method to initialize Time Picker
    private void initTimePicker() {
        materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(dialog -> {
            int hour = materialTimePicker.getHour();
            int minute = materialTimePicker.getMinute();
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        });
    }

    // Method to schedule a reminder for the appointment
    private void scheduleReminder() {
        // Get year, month, day, hour, and minute from the selected date and time
        String[] dateParts = dateButton.getText().toString().split("/");
        String[] timeParts = timeButton.getText().toString().split(":");

        int year = Integer.parseInt(dateParts[2]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Months are zero-based
        int dayOfMonth = Integer.parseInt(dateParts[0]);
        int hourOfDay = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Calculate the reminder time in milliseconds
        long reminderTimeInMillis = getReminderTimeInMillis(year, month, dayOfMonth, hourOfDay, minute);

        // Set the alarm for the reminder
        setReminderAlarm(reminderTimeInMillis);
    }

    // Method to set the reminder alarm
    private void setReminderAlarm(long reminderTimeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            // Create an intent for the reminder and set it to trigger at the specified time
            Intent intent = new Intent(this, AppointmentReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimeInMillis, pendingIntent);
        }
    }

    // Method to calculate the reminder time in milliseconds

    private long getReminderTimeInMillis(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
}