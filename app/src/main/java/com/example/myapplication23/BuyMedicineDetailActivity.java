package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BuyMedicineDetailActivity extends AppCompatActivity {

    //declaare variables for EditText, TextView and button
    TextView tvMedicineName, tvTotalPrice;
    EditText edDetails;
    Button btnBack, btnAddToCart;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_detail);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize EditTexts, Button, and TextView by ID

        tvMedicineName = findViewById(R.id.textViewBMMedicine);
        edDetails = findViewById(R.id.editTextMedicine);
        edDetails.setKeyListener(null);
        tvTotalPrice = findViewById(R.id.textViewTotalPrice);
        btnBack = findViewById(R.id.buttonGoBack1);
        btnAddToCart = findViewById(R.id.buttonAddToCart);

        // Get intent data from previous activity
        Intent intent = getIntent();
        tvMedicineName.setText(intent.getStringExtra("text1")); // Set medicine name
        edDetails.setText(intent.getStringExtra("text2")); // Set medicine details
        tvTotalPrice.setText("Total Price: £" + intent.getStringExtra("text3")); // Set total price

        // Set click listener for back button to return to BuyMedicineActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyMedicineDetailActivity.this, BuyMedicineActivity.class));
            }
        });

        // Set click listener for Add to Cart button
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username from SharedPreferences
                SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedpreferences.getString("UserName", "").toString();
                // Extract product details from intent
                String product = tvMedicineName.getText().toString();
                float price = Float.parseFloat(intent.getStringExtra("text3"));
                // Check if the product is already in the user's cart
                checkProductInCart(username, product, price);
            }
        });
    }

    private void checkProductInCart(String username, String product, float price) {
        db.collection("user_cart")
                .whereEqualTo("username", username)
                .whereEqualTo("product", product)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Product already exists in the user's cart
                            Log.d("TAG", "Product already in cart");
                            // You can display a message or take any other action here
                            showToast("Product already in cart");
                        } else {
                            // Product does not exist in the user's cart
                            Log.d("TAG", "Product not in cart, adding...");
                            // Add product to Firestore
                            addProductToCart(username, product, price);
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });

    }

    private void addProductToCart(String username, String product, float price) {
        // Create a map to represent the cart item
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("username", username);
        cartItem.put("product", product);
        cartItem.put("price", price);

        // Add the cart item to Firestore
        db.collection("user_cart")
                .add(cartItem)
                .addOnSuccessListener(documentReference -> {
                    Log.d("TAG", "Product added to cart with ID: " + documentReference.getId());
                    // You can display a success message or take any other action here
                    showToast("Product added to cart");
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding document", e);
                    // Handle errors here
                });
    }

    //display toast message.
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}