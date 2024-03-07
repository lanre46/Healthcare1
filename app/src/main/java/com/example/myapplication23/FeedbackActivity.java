package com.example.myapplication23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    // Declare variables for UI elements and Firestore database
    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutMessage;
    private TextInputLayout inputLayoutPhone;
    private TextInputLayout inputLayoutEmail;
    private Button backButton;
    private Button sendFeedbackButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);



        inputLayoutName = findViewById(R.id.inputLayoutName);
        inputLayoutMessage = findViewById(R.id.inputLayoutMessage);
        inputLayoutPhone = findViewById(R.id.inputLayoutPhone);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        backButton = findViewById(R.id.button2);
        sendFeedbackButton = findViewById(R.id.button3);

        db = FirebaseFirestore.getInstance(); // Get instance of Firestore database


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedbackActivity.this,HomeActivity.class));
            }
        });

        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the EditText from TextInputLayout
                EditText inputName = inputLayoutName.getEditText();
                EditText inputMessage = inputLayoutMessage.getEditText();
                EditText inputPhone = inputLayoutPhone.getEditText();
                EditText inputEmail = inputLayoutEmail.getEditText();

                // Check if any of the TextInputLayout objects are null
                if (inputName == null || inputMessage == null || inputPhone == null || inputEmail == null) {
                    Snackbar.make(v, "Something went wrong. Please try again.", Snackbar.LENGTH_SHORT).show();
                    return; // Exit the method if any TextInputLayout is null
                }

                // Get text from EditText fields
                String name = inputName.getText().toString().trim();
                String message = inputMessage.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();

                // Check if all input fields are filled
                if (!name.isEmpty() && !message.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
                    sendFeedbackToFirestore(name, message, phone, email);
                } else {
                    //if all fileds are not inputted, messages pops up
                    Snackbar.make(v, "Please fill in all fields", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void sendFeedbackToFirestore(String name, String message, String phone, String email) {
        // Create a map to store feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("name", name);
        feedbackData.put("message", message);
        feedbackData.put("phone", phone);
        feedbackData.put("email", email);

        // Add the feedback to Firestore
        db.collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Feedback added successfully
                        Snackbar.make(sendFeedbackButton, "Feedback sent successfully", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while adding feedback
                        Snackbar.make(sendFeedbackButton, "Failed to send feedback. Please try again.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}