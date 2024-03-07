package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyMedicineActivity extends AppCompatActivity {

    //medicine inputted data
    private String[][] medicines = {

            {"Aspirin" , "" , "" , "" ,"50"},
            {"Ibuprofen" , "" , "" , "" ,"80"},
            {"Prozac" , "" , "" , "" ,"70"},
            {"Lipitor" , "" , "" , "" ,"90"},
            {"Zoloft" , "" , "" , "" ,"60"},
            {"Ventolin" , "" , "" , "" ,"250"},
            {"Tylenol" , "" , "" , "" ,"520"},
            {"Amoxicillin" , "" , "" , "" ,"150"},
            {"Tylenol" , "" , "" , "" ,"30"},

    };

    //medicine details inputted data
    private String[] medicine_details= {
         "Relieves pain, reduces inflammation, and lowers fever\n" +
               "Helps with pain relief and inflammation\n" +
               "Treats bacterial infections",
         "Balances brain chemicals to improve mood and treat depression"   ,
         "lowers cholesterol levels and reduces the risk of heart disease\n"   +
                 "relieves pain\n"+
                 "reduces fever",
          "Treats depression, anxiety , and mental health conditions. \n"  +
                  "relieves pain\n"+
                  "reduces fever",
           "Relieves allergy symptoms like sneezing and itching." ,
           "Reduces stomach acid production and treats acid reflux and ulcers.\n" +
                   "suitable for people with stomach ache",
           "Manages blood sugar levels in people with type 2 diabetes.\n" +
                   "help for patient suffering from diabetes",
           "Reduces inflammation and suppresses the immune system\n"  +
                   "helps in relieving pain",
           "Relieves asthma symptoms by opening up the airways"
    };

    // Declare variables for ListView, Buttons, HashMap, ArrayList, and SimpleAdapter
    HashMap<String,String> item; // HashMap to store each item in the list
    ArrayList<HashMap<String, String>> list; // ArrayList to store all items in the list
    SimpleAdapter sa; // Adapter to populate the ListView
    ListView lst; // ListView to display the list of medicines
    Button buttonBack1, buttonGoToCart; // Buttons for navigation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);

        // Initialize ListView and Buttons by finding their corresponding views in the layout
        lst = findViewById(R.id.listView);
        buttonBack1 = findViewById(R.id.buttonBack1);
        buttonGoToCart = findViewById(R.id.buttonGoToCart);

        // Set OnClickListener for the "Go to Cart" button
        buttonGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyMedicineActivity.this,CartMedicineActivity.class));
            }
        });

        // Set OnClickListener for the "Back" button
        buttonBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyMedicineActivity.this, HomeActivity.class));
            }
        });

        // Initialize list and populate it with medicine data
        list = new ArrayList<>();
        for(int i= 0; i<medicines.length; i++){
            item = new HashMap<>();
            item.put("line1" , medicines[i][0]); // Medicine name
            item.put("line2" , medicines[i][1]); // Medicine description
            item.put("line3" , medicines[i][2]); // Additional details
            item.put("line4" , medicines[i][3]); // Additional details
            item.put("line5" , "Total Price:" + "£" + medicines[i][4]); // Price
            list.add(item); // Add the item to the list
        }

        // Initialize the SimpleAdapter with the context, list data, layout, and view IDs
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[] {"line1" , "line2" , "line3" , "line4" , "line5"},
                new int[] {R.id.line_1, R.id.line_2, R.id.line_3, R.id.line_4, R.id.line_5});
        lst.setAdapter(sa); // Set the adapter for the ListView


        // Set OnItemClickListener for the ListView to handle item clicks
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                // Start BuyMedicineDetailActivity with selected medicine data
                Intent it = new Intent(BuyMedicineActivity.this,BuyMedicineDetailActivity.class);
                it.putExtra("text1" , medicines[i][0]);  //medicine name
                it.putExtra("text2" , medicine_details[i]); //medicine details
                it.putExtra("text3" , medicines[i][4]); //medicine price
                startActivity(it); // Start the activity

            }
        });
    }
}