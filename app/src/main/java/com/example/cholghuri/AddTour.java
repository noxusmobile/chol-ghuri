package com.example.cholghuri;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddTour extends Activity {

    private EditText addTourTitleET, addTourDetailsET, addTourAmountET;
    private Button addTourBTN;
    private FirebaseAuth firebaseauth;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);
        firebaseauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        initialize();
        onclick();
    }



    private void initialize() {



        addTourTitleET = findViewById(R.id.addTourTitleET);
        addTourDetailsET = findViewById(R.id.addTourDetailsET);
        addTourAmountET = findViewById(R.id.addTourAmountET);

        addTourBTN=findViewById(R.id.addTourBTN);




    }


    private void onclick() {

        addTourBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TourTitle = addTourTitleET.getText().toString();
                String TourDetails = addTourDetailsET.getText().toString();
                String temp = addTourAmountET.getText().toString();
                int TourAmount = Integer.valueOf(temp);



                sendTourDataToDatabase(new Tour(TourTitle,TourDetails,TourAmount,1,1));






            }
        });


    }

    private void sendTourDataToDatabase(Tour tour) {


       /* final Map<String,Object> userMap = new HashMap<>();
        userMap.put("TourTitle",tourTitle);
        userMap.put("TourDetails",tourDetails);
        userMap.put("TourAmount",tourAmount);
*/

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Tour");
        String Id = databaseReference.push().getKey();

        tour.setTourID(Id);
        databaseReference.child(Id).setValue(tour).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(AddTour.this, "complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
