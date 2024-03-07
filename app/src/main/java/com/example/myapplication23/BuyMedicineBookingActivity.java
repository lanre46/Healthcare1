package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BuyMedicineBookingActivity extends AppCompatActivity {

    //declare variables for EditText and Button
    EditText edname, edaddress, edphone,edemail;
    Button btnBook;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_booking);

        //Initialize the EditText and button by id

        edname = findViewById(R.id.FullbookingName);
        edaddress = findViewById(R.id.Addressbooking);
        edphone = findViewById(R.id.phonebooking);
        edemail = findViewById(R.id.Emailbooking);
        btnBook = findViewById(R.id.buybooking);

        // Initialize Firebase Firestore

        db = FirebaseFirestore.getInstance();

        // Retrieve data from the intent

        Intent intent = getIntent();
        String[] price = intent.getStringExtra("price").split(":");
        String date = intent.getStringExtra("date");


        // Set click listener for the book button
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username from SharedPreferences
                SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedpreferences.getString("UserName", "");

                // Create a new order document in Firestore
                Map<String, Object> order = new HashMap<>();
                order.put("username", username);
                order.put("name", edname.getText().toString());
                order.put("address", edaddress.getText().toString());
                order.put("phone", edphone.getText().toString());
                order.put("email", edemail.getText().toString());
                order.put("date", date);
                order.put("price", Float.parseFloat(price[1])); // Assuming price[1] contains the price value
                order.put("type", "medicines");


                // Add the order to Firestore
                db.collection("orders")
                        .add(order)
                        .addOnSuccessListener(documentReference -> {
                            // Booking successful
                            Toast.makeText(getApplicationContext(), "Booking successful", Toast.LENGTH_LONG).show();
                            // Remove items from cart (if necessary)
                            // Redirect to HomeActivity
                            startActivity(new Intent(BuyMedicineBookingActivity.this, HomeActivity.class));
                        })
                        .addOnFailureListener(e -> {
                            // Handle errors
                            Toast.makeText(getApplicationContext(), "Failed to book", Toast.LENGTH_LONG).show();
                        });

            }
        });
    }
}