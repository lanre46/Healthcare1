package com.example.myapplication23;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDetailsActivity extends AppCompatActivity {

    // Declare FirebaseFirestore instance
    private FirebaseFirestore db;

    // Declare HashMap, ArrayList, SimpleAdapter, and ListView
    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;

    ListView lst;


    // Declare Button
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Initialize Button and ListView by finding their corresponding views in the layout
        btn = findViewById(R.id.buttonHRDBack);
        lst = findViewById(R.id.listViewHealthResource);

        // Set OnClickListener for the back button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingDetailsActivity.this, HomeActivity.class));
            }
        });

        // Initialize FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();

        // Retrieve username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("UserName", ""); // Retrieve the username from SharedPreferences

        // Check if username is not empty
        if (!username.isEmpty()) { // Check if the username is not empty
            // Load booking and appointment details for the given username
            loadBookingAndAppointmentDetails(username);
            
        } else {

        }


    }

    // Method to load booking and appointment details for a given username
    private void loadBookingAndAppointmentDetails(String username) {
        db.collection("orders")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, String>> list = new ArrayList<>();
                        // Loop through each document in the result
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Create a new order document in Firestore
                            Map<String, String> item = new HashMap<>();
                            String[] data = new String[8]; // Assuming you have 8 fields in your document
                            data[0] = document.getString("name");
                            data[1] = document.getString("address");
                            data[4] = document.getString("date");
                            double price = document.getDouble("price");
                            data[6] = String.valueOf(price);
                            data[7] = document.getString("type");
                            // Populate other fields similarly
                            item.put("line1", data[0]);
                            item.put("line2", data[1]);
                            item.put("line3", "£" + data[6]);
                            if ("medicine".equals(data[7])) { // Check for null-safe comparison
                                item.put("line4", "Date: " + data[4]);
                            } else {
                                item.put("line4", "Date: " + data[4]);
                            }
                            item.put("line5", data[7]);
                            list.add(item);
                        }
                        // Now load appointment details
                        db.collection("appointments")
                                .whereEqualTo("username", username)
                                .whereEqualTo("type", "appointment") // Filter only appointments
                                .get()
                                .addOnCompleteListener(appointmentTask -> {
                                    if (appointmentTask.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : appointmentTask.getResult()) {
                                            Map<String, String> item = new HashMap<>();
                                            String[] data = new String[8]; // Assuming you have 8 fields in your document
                                            data[0] = document.getString("fullname");
                                            data[1] = document.getString("address");
                                            data[4] = document.getString("date");
                                            double price = document.getDouble("fees");
                                            data[6] = String.valueOf(price);
                                            data[7] = document.getString("type");
                                            // Populate other fields similarly
                                            item.put("line1", data[0]);
                                            item.put("line2", data[1]);
                                            item.put("line3", "£" + data[6]);
                                            item.put("line4", "Date: " + data[4]);
                                            item.put("line5", data[7]);
                                            list.add(item);
                                        }
                                        // Set the combined list to the adapter
                                        SimpleAdapter sa = new SimpleAdapter(BookingDetailsActivity.this, list,
                                                R.layout.multi_lines,
                                                new String[]{"line1", "line2", "line3", "line4", "line5"},
                                                new int[]{R.id.line_1, R.id.line_2, R.id.line_3, R.id.line_4, R.id.line_5});
                                        lst.setAdapter(sa);
                                    }
                                });
                    }
                });
    }
}
