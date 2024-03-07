package com.example.myapplication23;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn, forgotTextLink;

    FirebaseAuth fAuth; // Firebase Authentication object
    FirebaseFirestore fStore; // Firestore object for database access

    // Variables for biometric authentication
    private int biometricFailedAttempts = 0;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication and Firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Find views by their respective IDs
        mEmail = findViewById(R.id.Addressbooking);
        mPassword = findViewById(R.id.Emailbooking);
        mLoginBtn = findViewById(R.id.buybooking);
        mCreateBtn = findViewById(R.id.createText);
        forgotTextLink = findViewById(R.id.forgotPassword);

        // Set click listener for login button
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input for email and password
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;  //return error message if field is empty
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return; //return error message if field is empty after inputting email
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be >=6 characters");
                    return; //if you enter a password that's less than 6 , returns error message
                }

                // Authenticate the user using Firebase Authentication
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Retrieve username from Firestore and save it to shared preferences
                            String userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        // If user document exists, retrieve username
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String username = document.getString("UName");
                                            saveUsernameToSharedPreferences(username);  // Save username to shared preferences
                                            Toast.makeText(LoginActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();  // Display success message
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class)); //redirects the user to HomeActivity
                                        } else {
                                            // If user document does not exist, display error message
                                            Toast.makeText(LoginActivity.this, "User document does not exist.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error getting user document: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            //if authentication fails, then you display this message.
                            Toast.makeText(LoginActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Set click listener for create account button
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        // Initialize biometric authentication setup
        setupBiometricAuthentication();

        // Set click listener for forgot password text link
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create dialog for password reset
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email to receive reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset link has been sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error! Reset link not been sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }

    // Method to handle biometric authentication success
    private void biometricSuccess() {
        // Display a Snackbar indicating successful biometric authentication
        Snackbar.make(findViewById(android.R.id.content), "Biometric authentication successful", Snackbar.LENGTH_SHORT).show();
        // Create an intent to navigate to the HomeActivity
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        //starts the home activity
        startActivity(intent);
    }

    // Method to setup biometric authentication
    private void setupBiometricAuthentication() {
        // Initialize BiometricManager to check the availability of biometric authentication
        BiometricManager biometricManager = BiometricManager.from(this);
        // Switch statement to handle different scenarios of biometric authentication availability
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
               // If biometric authentication is available and successful, the initBiometricPrompt() method is called to initialize the biometric prompt for authentication.
                initBiometricPrompt();
                break;
          //  If biometric authentication is not available due to reasons such as no hardware support (BIOMETRIC_ERROR_NO_HARDWARE), hardware unavailable (BIOMETRIC_ERROR_HW_UNAVAILABLE), or no enrolled biometrics (BIOMETRIC_ERROR_NONE_ENROLLED), a toast message is displayed to notify the user.
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Biometric authentication is not available, display a message
                showToast("Biometric authentication not available");
                break;
        }
    }

    // Method to initialize biometric prompt
    private void initBiometricPrompt() {
        // Create a BiometricPrompt object with authentication callbacks
        biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this), new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Display an error message if authentication fails
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    showToast("Authentication Error: " + errString);
                }
            }

            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Call biometricSuccess() method if authentication is successful
                biometricSuccess();
            }

            @Override // Indicates that this method overrides a superclass method
            public void onAuthenticationFailed() { // Method called when biometric authentication fails
                super.onAuthenticationFailed(); // Call the superclass method to ensure default behavior is executed

                // Increment the failed attempts counter and handle accordingly
                biometricFailedAttempts++; // Increment the counter for failed attempts

                // Check if the number of failed attempts exceeds a threshold
                if (biometricFailedAttempts >= 3) { // If there have been too many failed attempts
                    showToast("Too many failed attempts. Closing application."); // Show a message indicating too many failed attempts
                    finishAndRemoveTask(); // Close the application
                } else { // If the number of failed attempts is below the threshold
                    showToast("Authentication Failed. Attempt " + biometricFailedAttempts + "/3"); // Show a message indicating authentication failure and the attempt count
                }
            }
        });
        // Configure the prompt info for biometric authentication
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for My App") // Set title of the biometric prompt
                .setSubtitle("Log in using your biometric credential") // Set subtitle of the biometric prompt
                .setNegativeButtonText("Use account password") // Set text of the negative button
                .build(); // Build the PromptInfo object with the configured settings

        // Find the fingerprint button and set its click listener
        Button biometricLoginButton = findViewById(R.id.biometricLoginButton);
        biometricLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show biometric prompt when fingerprint button is clicked
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    // Method to save username to shared preferences
    private void saveUsernameToSharedPreferences(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserName", username);
        editor.apply();
    }

    // Method to display toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}