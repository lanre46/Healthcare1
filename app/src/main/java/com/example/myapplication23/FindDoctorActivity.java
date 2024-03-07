package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FindDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        // Initialize Back CardView and when clicked goes back to homeactivity
        CardView back = findViewById(R.id.cardBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindDoctorActivity.this, HomeActivity.class));
            }
        });

        // Initialize SearchView
        SearchView searchView = findViewById(R.id.searchView);  // Find the SearchView widget in the layout
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search here
                // shows a toast with the search query
                Toast.makeText(FindDoctorActivity.this, "Search: " + query, Toast.LENGTH_SHORT).show();// Display a toast message with the search query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list of doctors based on the search query
                filterDoctorList(newText); // Call a method to filter the list of doctors based on the new text
                return true; // Indicate that the event has been handled
            }
        });

        // Initialize CardViews for doctors
        initializeDoctorCardViews(""); // Initialize the CardViews to display doctor information
    }

    private void initializeDoctorCardViews(String query) {
        // Initialize Dentist CardView
        CardView dentist = findViewById(R.id.cardDentist);
        dentist.setVisibility(query.isEmpty() || "dentist".contains(query) ? View.VISIBLE : View.GONE);
        dentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Dentist");
            }
        });

        // Initialize Surgeon CardView
        CardView surgeon = findViewById(R.id.cardSurgeon);
        surgeon.setVisibility(query.isEmpty() || "surgeon".contains(query) ? View.VISIBLE : View.GONE);
        surgeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Surgeon");
            }
        });

        // Initialize Optician CardView
        CardView optician = findViewById(R.id.cardOptician);
        optician.setVisibility(query.isEmpty() || "optician".contains(query) ? View.VISIBLE : View.GONE);
        optician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Optician");
            }
        });

        // Initialize Cardiologist CardView
        CardView cardiologist = findViewById(R.id.cardCardiologist);
        cardiologist.setVisibility(query.isEmpty() || "cardiologist".contains(query) ? View.VISIBLE : View.GONE);
        cardiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Cardiologist");
            }
        });

        // Initialize Physician CardView
        CardView physician = findViewById(R.id.cardPhysician);
        physician.setVisibility(query.isEmpty() || "physician".contains(query) ? View.VISIBLE : View.GONE);
        physician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Physician");
            }
        });

        CardView back = findViewById(R.id.cardBack);
        back.setVisibility(query.isEmpty() || "back".contains(query) ? View.VISIBLE : View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindDoctorActivity.this, HomeActivity.class));
            }
        });
    }

    private void filterDoctorList(String query) {
        // Convert the query to lowercase for case-insensitive search
        String lowercaseQuery = query.toLowerCase();

        // Initialize CardViews for doctors
        initializeDoctorCardViews(lowercaseQuery);
    }

    // Method to start DoctorDetailActivity with the specified doctor type
    private void startDoctorDetailActivity(String doctorType) {
        Intent intent = new Intent(FindDoctorActivity.this, DoctorDetailActivity.class);
        intent.putExtra("title", doctorType);
        startActivity(intent);
    }
}