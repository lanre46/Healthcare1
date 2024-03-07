package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.google.android.material.datepicker.MaterialDatePicker;
import java.util.Locale;

public class CartMedicineActivity extends AppCompatActivity {

    //declare the variables
    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    TextView tvTotal;

    private MaterialDatePicker<Long> materialDatePicker;
    ListView lst;



    private Button dateButton, btnCheckout, btnBack;

    private FirebaseFirestore db;
    private String[][] medicines = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_medicine);

        //initialize to their id
        dateButton = findViewById(R.id.buttonCartDate);
        btnCheckout = findViewById(R.id.buttonCartCheckout);
        btnBack  = findViewById(R.id.buttonGoBack2);
        lst = findViewById(R.id.listViewCart);
        tvTotal = findViewById(R.id.textViewTotalPrice);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve username from SharedPreferences
        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("UserName", "").toString();

        // Initialize list to hold cart items
        list = new ArrayList<>();

        // Initialize adapter to populate list view
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_1, R.id.line_2, R.id.line_3, R.id.line_4, R.id.line_5});

        lst.setAdapter(sa);


        //OnClickListener for Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartMedicineActivity.this,BuyMedicineActivity.class));
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BuyMedicineBookingActivity with total price and selected date as extras
                  Intent it = new Intent(CartMedicineActivity.this, BuyMedicineBookingActivity.class);
                  it.putExtra("price" , tvTotal.getText());
                  it.putExtra("date" , dateButton.getText());
                  startActivity(it);

                clearCart(username);
                list.clear();
                sa.notifyDataSetChanged();
                tvTotal.setText("Total Price: 0");
            }
        });

        // Date Picker
        initDatePicker();

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });
        // Load cart data from Firestore
        loadCartData(username);
    }

    // Method to load cart data from Firestore
    private void loadCartData(String username) {
        db.collection("user_cart")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        list.clear(); // Clear previous data
                        float totalAmount = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            // Create item map for each document
                            Map<String, String> item = new HashMap<>();
                            item.put("line1", document.getString("product"));
                            item.put("line2", "use twice daily");
                            item.put("line3", "Noon");
                            item.put("line4", "Night");
                            item.put("line5", "Price: " +  "£" + document.getDouble("price"));
                            list.add(item);
                            // Calculate total price
                            totalAmount += document.getDouble("price");
                        }
                        // Set total price on TextView
                        tvTotal.setText("Total Price: " + totalAmount);
                        sa.notifyDataSetChanged(); // Notify the adapter that the dataset has changed
                    } else {

                    }
                });
    }

    // Method to initialize date picker
    private void initDatePicker(){
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

    // Method to clear cart items from Firestore
    private void clearCart(String username) {
        db.collection("user_cart")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    } else {
                        // Handle errors here
                    }
                });
    }
}