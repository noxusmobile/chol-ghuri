package com.example.cholghuri;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class UpdateTour extends AppCompatActivity {

    private TextInputEditText addTourTitleET, addTourDetailsET, addTourAmountET;
    private Button updateTourBTN,fromDateTourBTN ,toDateTourBTN;
    private FirebaseAuth firebaseauth;
    private FirebaseDatabase firebaseDatabase;
    SimpleDateFormat dateAndtimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMMM yyyy");
    private  long selectedDateinFromMS,selectedDateinToMS;
    private Calendar calendar;
    private String userID;
    private  String tourID;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Update Tour");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour);
        firebaseauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();

        userID = firebaseauth.getCurrentUser().getUid();

        initialize();
        onclick();
        getdata();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.signoutId){
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }if (item.getItemId()==R.id.resetPass){
            Intent intent=new Intent(getApplicationContext(),ResetPassword.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId()==R.id.homeId){
            Intent intent=new Intent(getApplicationContext(),Userprofile.class);
            startActivity(intent);
            finish();}
        return super.onOptionsItemSelected(item);
    }
    private void getdata() {


        Intent intent = getIntent();
        tourID = intent.getStringExtra("tourID");


        String tourTitle = intent.getStringExtra("tourTitle");
        String tourDetails = intent.getStringExtra("tourDetails");
        int tourAmount = intent.getIntExtra("tourAmount",0);
        long tourStartDate = intent.getLongExtra("tourStartDate",-1);
        long tourEndDate = intent.getLongExtra("tourEndDate",-1);



        String temp = String.valueOf(tourAmount);

        addTourTitleET.setText(tourTitle);
        addTourDetailsET.setText(tourDetails);
        addTourAmountET.setText(temp);
        selectedDateinFromMS = tourStartDate;


        long temp1 =  tourStartDate;
        Date startDate = new Date(temp1);
        fromDateTourBTN.setText(dateSDF.format(startDate));

        long temp2=  tourEndDate;
        Date endDate = new Date(temp2);
        toDateTourBTN.setText(dateSDF.format(endDate));






    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialize() {


        //  SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy/MM/dd");

        addTourTitleET = findViewById(R.id.addTourTitleET);
        addTourDetailsET = findViewById(R.id.addTourDetailsET);
        addTourAmountET = findViewById(R.id.addTourAmountET);
        fromDateTourBTN = findViewById(R.id.fromDateTourBTN);
        toDateTourBTN = findViewById(R.id.toDateTourBTN);


        updateTourBTN=findViewById(R.id.updateTourBTN);




    }


    private void onclick() {

        updateTourBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TourTitle = addTourTitleET.getText().toString();
                String TourDetails = addTourDetailsET.getText().toString();
                String temp = addTourAmountET.getText().toString();



                if(TourTitle.isEmpty())
                {
                    addTourTitleET.setError("Enter Tour title");
                    addTourTitleET.requestFocus();
                    return;
                }
                if( TourDetails.isEmpty())
                {
                    addTourDetailsET.setError("Enter Tour Details");
                    addTourDetailsET.requestFocus();
                    return;
                }
                if( temp.isEmpty())
                {
                    addTourAmountET.setError("Enter Tour Budget");
                    addTourAmountET.requestFocus();
                    return;
                }
                if(selectedDateinFromMS == 0)
                {
                    Toast.makeText(UpdateTour.this, "Enter Tour start date", Toast.LENGTH_SHORT).show();
                }
                if(selectedDateinToMS == 0 )
                {
                    Toast.makeText(UpdateTour.this, "Enter Tour end date", Toast.LENGTH_SHORT).show();

                }
                else {
                    int TourAmount = Integer.valueOf(temp);
                    sendTourDataToDatabase(new Tour(TourTitle, TourDetails, TourAmount, selectedDateinFromMS, selectedDateinToMS));
                }


            }
        });

        fromDateTourBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opendatepickerFrom();
            }
        });

        toDateTourBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opendatepickerTo();
            }
        });


    }


    /*=======Validation function under construction============*/
/*
    private boolean validate(String tourTitle, String tourDetails, String tourAmount, double selectedDateinFromMS, double selectedDateinToMS) {

        if(!tourTitle.contains("")&&!tourDetails.contains("")&&!tourAmount.contains("").!tou){

            return true;


        }
        else{

            return false;

        }


    }
*/

    /*=======Validation function under construction============*/

    private void sendTourDataToDatabase(Tour tour) {


       /* final Map<String,Object> userMap = new HashMap<>();
        userMap.put("TourTitle",tourTitle);
        userMap.put("TourDetails",tourDetails);
        userMap.put("TourAmount",tourAmount);
*/




        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID);


        tour.setTourID(tourID);
        databaseReference.setValue(tour).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(UpdateTour.this, "Tour Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }


    private void opendatepickerFrom() {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String selectDate = year + "/" + month + "/" + dayOfMonth + " 00:00:00";
                Date date = null;

                try {
                    date = dateAndtimeSDF.parse(selectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                selectedDateinFromMS = date.getTime();

                //Toast.makeText(getApplicationContext(), ""+selectedDateinFromMS, Toast.LENGTH_SHORT).show();

                fromDateTourBTN.setText(dateSDF.format(date));

            }


        };
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);

        datePickerDialog.show();

    }


    private void opendatepickerTo() {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String selectDate = year + "/" + month + "/" + dayOfMonth + " 23:59:59";
                Date date = null;

                try {
                    date = dateAndtimeSDF.parse(selectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                selectedDateinToMS = date.getTime();

                //Toast.makeText(getApplicationContext(), ""+selectedDateinToMS, Toast.LENGTH_SHORT).show();

                toDateTourBTN.setText(dateSDF.format(date));

            }


        };


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        // datePickerDialog.getDatePicker().setMinDate(selectedDateinFromMS);
        datePickerDialog.show();


    }
}
