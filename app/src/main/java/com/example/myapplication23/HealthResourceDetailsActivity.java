package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthResourceDetailsActivity extends AppCompatActivity {


    TextView tv1;
    ImageView img;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_resource_details);
        // Initialize Views
        btnBack = findViewById(R.id.buttonHRDBack);
        tv1 = findViewById(R.id.textViewHRDetails);
        img = findViewById(R.id.imageViewHRD);

        // Get intent from previous activity
        Intent intent = getIntent();

        // Set text for TextView from intent extra
        tv1.setText(intent.getStringExtra("text1"));

        // Get bundle from intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

            // Get resource ID from bundle and set image resource for ImageView
            int resId = bundle.getInt("text2");
            img.setImageResource(resId);
        }

        //OnClickListener for the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HealthResourceActivity
                startActivity(new Intent(HealthResourceDetailsActivity.this,HealthResourceActivity.class));
            }
        });
    }
}