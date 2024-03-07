package com.example.myapplication23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailActivity extends AppCompatActivity {
   //doctor details inputted data
    private String[][] doctor_details1 =

            {

                    {"Doctor: ED Bell " , "Address : Clifton,Nottingham" , "Exp : 5yrs" , "Mobile No:07776567455", "£200"},
                    {"Doctor: Toby Aros " , "Address : Beeston,Nottingham" , "Exp : 10yrs" , "Mobile No:07786567455", "£500"},
                    {"Doctor: Dami Hope " , "Address : Arboretum,Nottingham" , "Exp : 8yrs" , "Mobile No:07796567455" , "£800"},
                    {"Doctor: Lebron James " , "Address : Dunkirk,Nottingham" , "Exp : 6yrs" , "Mobile No:07576567485" , "£600"},
                    {"Doctor: Rubi Rose" , "Address : Sherwood,Nottingham" , "Exp : 9yrs" , "Mobile No:07675567455", "£300"}
            };
    private String[][] doctor_details2 =

            {

                    {"Doctor: ED Bell " , "Address : Clifton,Nottingham" , "Exp : 5yrs" , "Mobile No:07776567455", "£200"},
                    {"Doctor: Toby Aros " , "Address : Beeston,Nottingham" , "Exp : 10yrs" , "Mobile No:07786567455", "£500"},
                    {"Doctor: Dami Hope " , "Address : Arboretum,Nottingham" , "Exp : 8yrs" , "Mobile No:07796567455" , "£800"},
                    {"Doctor: Lebron James " , "Address : Dunkirk,Nottingham" , "Exp : 6yrs" , "Mobile No:07576567485" , "£600"},
                    {"Doctor: Rubi Rose" , "Address : Sherwood,Nottingham" , "Exp : 9yrs" , "Mobile No:07675567455", "£300"}
            };
    private String[][] doctor_details3 =

            {

                    {"Doctor: ED Bell " , "Address : Clifton,Nottingham" , "Exp : 5yrs" , "Mobile No:07776567455", "£200"},
                    {"Doctor: Toby Aros " , "Address : Beeston,Nottingham" , "Exp : 10yrs" , "Mobile No:07786567455", "£500"},
                    {"Doctor: Dami Hope " , "Address : Arboretum,Nottingham" , "Exp : 8yrs" , "Mobile No:07796567455" , "£800"},
                    {"Doctor: Lebron James " , "Address : Dunkirk,Nottingham" , "Exp : 6yrs" , "Mobile No:07576567485" , "£600"},
                    {"Doctor: Rubi Rose" , "Address : Sherwood,Nottingham" , "Exp : 9yrs" , "Mobile No:07675567455", "£300"}
            };
    private String[][] doctor_details4 =

            {

                    {"Doctor: ED Bell " , "Address : Clifton,Nottingham" , "Exp : 5yrs" , "Mobile No:07776567455", "£200"},
                    {"Doctor: Toby Aros " , "Address : Beeston,Nottingham" , "Exp : 10yrs" , "Mobile No:07786567455", "£500"},
                    {"Doctor: Dami Hope " , "Address : Arboretum,Nottingham" , "Exp : 8yrs" , "Mobile No:07796567455" , "£800"},
                    {"Doctor: Lebron James " , "Address : Dunkirk,Nottingham" , "Exp : 6yrs" , "Mobile No:07576567485" , "£600"},
                    {"Doctor: Rubi Rose" , "Address : Sherwood,Nottingham" , "Exp : 9yrs" , "Mobile No:07675567455", "£300"}
            };
    private String[][] doctor_details5 =

            {

                    {"Doctor: ED Bell " , "Address : Clifton,Nottingham" , "Exp : 5yrs" , "Mobile No:07776567455", "£200"},
                    {"Doctor: Toby Aros " , "Address : Beeston,Nottingham" , "Exp : 10yrs" , "Mobile No:07786567455", "£500"},
                    {"Doctor: Dami Hope " , "Address : Arboretum,Nottingham" , "Exp : 8yrs" , "Mobile No:07796567455" , "£800"},
                    {"Doctor: Lebron James " , "Address : Dunkirk,Nottingham" , "Exp : 6yrs" , "Mobile No:07576567485" , "£600"},
                    {"Doctor: Rubi Rose" , "Address : Sherwood,Nottingham" , "Exp : 9yrs" , "Mobile No:07675567455", "£300"}
            };

    //declare variables for elements and doctor details
    TextView tv;
    Button btn;

    String[][] doctor_details = {};
    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);


        // Find the TextView and Button views by their respective IDs from the layout
        tv = findViewById(R.id.textViewHealthTitle);
        btn = findViewById(R.id.buttonHRDBack);

        // Retrieve the intent that started this activity
        Intent it = getIntent();
        // Get the string extra "title" from the intent, which represents the doctor type
        String title = it.getStringExtra("title");
        // Set the text of the TextView to the doctor type
        tv.setText(title);

        // Set the doctor details array based on the doctor type
        if(title.compareTo("Dentist")==0)
            doctor_details= doctor_details1;
        else if(title.compareTo("Surgeon")==0)
            doctor_details= doctor_details2;
        else if(title.compareTo("Optician")==0)
            doctor_details= doctor_details3;
        else if(title.compareTo("Cardiologist")==0)
            doctor_details= doctor_details4;
        else if(title.compareTo("Physician")==0)
            doctor_details= doctor_details5;

        // Set a click listener for the back button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the FindDoctorActivity when the back button is clicked
                startActivity(new Intent(DoctorDetailActivity.this, FindDoctorActivity.class));
            }
        });

       // Initialize a new ArrayList to hold the doctor details
        list = new ArrayList();
        // Loop through the doctor_details array to populate the list
        for(int i = 0; i<doctor_details.length;i++) {
            // Initialize a new HashMap to hold the details of each doctor
            item = new HashMap<String, String>();
            // Put each detail of the doctor into the HashMap
            item.put("line1", doctor_details[i][0]); // Doctor's name
            item.put("line2", doctor_details[i][1]); // Doctor's address
            item.put("line3", doctor_details[i][2]); // Doctor's experience
            item.put("line4", doctor_details[i][3]); // Doctor's phone number
            // Concatenate the consultation fee with a string and put it in the HashMap
            item.put("line5", "Consultation fee:" + doctor_details[i][4] + "/-");
            // Add the HashMap to the ArrayList
            list.add(item);
        }


            sa = new SimpleAdapter(this,list,
                    R.layout.multi_lines,
                    new String[]{"line1","line2","line3","line4","line5"},
                    new int[]{R.id.line_1,R.id.line_2, R.id.line_3, R.id.line_4, R.id.line_5}
                    );
            ListView lst = findViewById(R.id.listViewHealthResource);
            lst.setAdapter(sa);

            // Set click listener for list items
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // Start BookAppointmentActivity with doctor details
                    Intent it = new Intent(DoctorDetailActivity.this,BookAppointmentActivity.class);
                    it.putExtra("text1" , title);
                    it.putExtra("text2" , doctor_details[i][0]);
                    it.putExtra("text3" , doctor_details[i][1]);
                    it.putExtra("text4" , doctor_details[i][3]);
                    it.putExtra("text5" , doctor_details[i][4]);
                    startActivity(it);
                }
            });

    }
}