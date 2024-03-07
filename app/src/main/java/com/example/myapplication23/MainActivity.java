package com.example.myapplication23;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {


    // Declare TextViews for displaying user profile information
    TextView Username, email, phone;
    // Firebase Authentication and Firestore instances
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    // Current user ID
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews by ids
        phone = findViewById(R.id.profilePhone);
        Username = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);

        // Initialize Firebase Authentication and Firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Get current user's ID
        userID = fAuth.getCurrentUser().getUid();


        // Retrieve user profile information from Firestore
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Populate TextViews with user profile data
                    phone.setText(documentSnapshot.getString("phone"));
                    Username.setText(documentSnapshot.getString("UName"));
                    email.setText(documentSnapshot.getString("email"));
                } else {

                }
            }
        });

    }

    public void logout(View view){
        // Sign out the user from Firebase Authentication
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}