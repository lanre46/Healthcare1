package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Display welcome message using Snackbar
        String username = getUsernameFromSharedPreferences();
        Snackbar.make(findViewById(android.R.id.content), "Welcome to Appointment App, " + username + "!", Snackbar.LENGTH_SHORT).show();





        // Set onClickListeners for CardViews
        CardView logout = findViewById(R.id.cardLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSharedPreferences();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });

        //if you click on any of the cardView, it takes you to their respective pages.

        //find and book doctor cardView
        CardView findDoctor = findViewById(R.id.cardFind_Bookdoctor);
        findDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FindDoctorActivity.class);
                startActivity(intent);
            }
        });

        //buy medicine cardView
        CardView buyMedicine = findViewById(R.id.cardBuyMedicine);
        buyMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BuyMedicineActivity.class);
                startActivity(intent);
            }
        });

        //booking details cardView
        CardView bookingDetails = findViewById(R.id.cardBookingDetails);
        bookingDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BookingDetailsActivity.class);
                startActivity(intent);
            }
        });

        //Health Resource cardView
        CardView HealthResources = findViewById(R.id.cardHealthResources);
        HealthResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HealthResourceActivity.class);
                startActivity(intent);
            }
        });

        //Feedback cardView
        CardView Feedback = findViewById(R.id.cardFeedback);
        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });



    }



    /**
     * Retrieves the username from SharedPreferences.
     * @return The username retrieved from SharedPreferences.
     */
    private String getUsernameFromSharedPreferences() {
        // Retrieve username from SharedPreferences
        return getSharedPreferences("shared_prefs", MODE_PRIVATE).getString("UserName", "");
    }

    /**
     * Clears SharedPreferences.
     */

    private void clearSharedPreferences() {
        // Clear SharedPreferences
        getSharedPreferences("shared_prefs", MODE_PRIVATE).edit().clear().apply();
    }
}