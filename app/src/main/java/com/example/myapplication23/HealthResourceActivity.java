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

public class HealthResourceActivity extends AppCompatActivity {

    //Health resources inputted data
    private String[][] health_resource =

            {

                    {"Eating Daily" , ""  , "" ,"" , "Click for More Details"},
                    {"Quit Smoking" , ""  , "" ,"" , "Click for More Details"},
                    {"Covid 19 Help" , ""  , "" ,"" ,"Click for More Details"},
                    {"Walking Daily" , ""  , "" ,"" ,"Click for More Details"},
                    {"Exercising" , ""  , "" ,"" ,   "Click for More Details"},

            };

    //images for health resources
    private int[] images = {
            R.drawable.food1,
            R.drawable.smoking,
            R.drawable.covid19,
            R.drawable.walking,
            R.drawable.exercise1,

    };

    // HashMap to hold health resource item details
    HashMap<String, String> item;
    // ArrayList to store health resource items
    ArrayList<HashMap<String, String>> list;
    // SimpleAdapter for the ListView
    SimpleAdapter sa;

    // Views
    Button btnBack;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_resource);

        //initialize views by id
        lst = findViewById(R.id.listViewHealthResource);
        btnBack = findViewById(R.id.buttonHRDBack);

        //OnClickListener for back button, when clicked it takes you back to homeactivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthResourceActivity.this, HomeActivity.class));
            }
        });

        // Initialize list and populate it with health resource items
        list = new ArrayList();
        for(int i = 0; i<health_resource.length; i++) {
            item = new HashMap<String, String>();
            item.put("line1" , health_resource[i][0]);
            item.put("line2" , health_resource[i][1]);
            item.put("line3" , health_resource[i][2]);
            item.put("line4" , health_resource[i][3]);
            item.put("line5" , health_resource[i][4]);
            list.add(item);
        }
        //simple adapter to bind data to listview
        sa = new SimpleAdapter(this,list,
                R.layout.multi_lines,
                new String[]{"line1","line2","line3","line4","line5"},
                new int[]{R.id.line_1,R.id.line_2, R.id.line_3, R.id.line_4, R.id.line_5}
        );
        lst.setAdapter(sa);

        // OnItemClickListener for ListView to handle item clicks
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Create intent to navigate to HealthResourceDetailsActivity and pass data
                Intent it = new Intent(HealthResourceActivity.this, HealthResourceDetailsActivity.class);
                it.putExtra("text1" , health_resource[i][0]);
                it.putExtra("text2" , images[i]);
                startActivity(it);
            }
        }) ;




    }
}