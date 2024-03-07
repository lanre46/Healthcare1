package com.example.myapplication23;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    // Declare EditTexts, Button, and TextView
    EditText mUsername, mEmail, mPhone,  mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;

    // Firebase Authentication and Firestore instances
    FirebaseAuth fAuth;

    FirebaseFirestore fStore;
    // User ID
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize EditTexts, Button, and TextView by ID
        mUsername = findViewById(R.id.FullbookingName);
        mPassword = findViewById(R.id.Emailbooking);
        mEmail = findViewById(R.id.Addressbooking);
        mPhone = findViewById(R.id.phonebooking);
        mRegisterBtn = findViewById(R.id.buybooking);
        mLoginBtn = findViewById(R.id.createText);
        // Initialize Firebase Authentication and Firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Check if the user is already logged in
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        // Set onClickListener for the Register Button
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditTexts
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String UserName  = mUsername.getText().toString();
                String phone = mPhone.getText().toString();
                // Validate user input
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be >=6 characters");
                    return;
                }
                // register the user in firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Display success message
                            Toast.makeText(RegisterActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                            // Get the user ID of the newly registered user
                            userID = fAuth.getCurrentUser().getUid();
                            // Create a document reference for the user in Firestore
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            // Create a HashMap to store user data
                            Map<String,Object> user = new HashMap<>();
                            user.put("UName", UserName );
                            user.put("email", email);
                            user.put("phone" , phone);
                            // Set user data in Firestore
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                }
                            });
                            // Redirect to MainActivity after successful registration
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            //if the registration fails, display error message
                            Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }
}
