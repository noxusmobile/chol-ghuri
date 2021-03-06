package com.example.cholghuri;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourDetails extends AppCompatActivity {

    private TextView TourTitleTV,TourDetailsTV,TourAmountTV,fromDateTourTV,toDateTourTV;
    private Button addExpenseActivityBTN,addMemoriesBTN,viewMemoriesBTN;

    private long selectedDateinFromMS,selectedDateinToMS;
    private RecyclerView expenseRecycler;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;
    private String userID;
    private String tourID;
    private String expenseID;
    SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMMM yyyy");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        setTitle("Tour Details");


        initialize();
        onclick();
        getDataFromIntent();
        initRecyclerView();
        getDataFromDB();
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
private void onclick() {



        viewMemoriesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourDetails.this, ViewMemories.class);
                intent.putExtra("tourID",tourID);
                startActivity(intent);

            }
        });

        addMemoriesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TourDetails.this, AddMemories.class);
                intent.putExtra("tourID",tourID);
                startActivity(intent);

            }
        });
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        tourID = intent.getStringExtra("tourID");


        String tourTitle = intent.getStringExtra("tourTitle");
        String tourDetails = intent.getStringExtra("tourDetails");
        int tourAmount = intent.getIntExtra("tourAmount",0);
        long tourStartDate = intent.getLongExtra("tourStartDate",-1);
        long tourEndDate = intent.getLongExtra("tourEndDate",-1);



        String temp = String.valueOf(tourAmount);

        TourTitleTV.setText(tourTitle);
        TourDetailsTV.setText(tourDetails);
        TourAmountTV.setText(temp);
        selectedDateinFromMS = tourStartDate;
        selectedDateinToMS = tourEndDate;



        long temp1 =  tourStartDate;
        Date startDate = new Date(temp1);
        fromDateTourTV.setText(dateSDF.format(startDate));

        long temp2=  tourEndDate;
        Date endDate = new Date(temp2);
        toDateTourTV.setText(dateSDF.format(endDate));

    }

    private void initialize() {
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        expenseRecycler = findViewById(R.id.expenseRecycler);
        userID = firebaseAuth.getCurrentUser().getUid();
        expenseList = new ArrayList<>();

        TourTitleTV = findViewById(R.id.TourTitleTV);
        TourDetailsTV = findViewById(R.id.TourDetailsTV);
        TourAmountTV = findViewById(R.id.TourAmountTV);
        fromDateTourTV = findViewById(R.id.fromDateTourTV);
        toDateTourTV = findViewById(R.id.toDateTourTV);


/*
        addExpenseActivityBTN=findViewById(R.id.addExpenseActivityBTN);
*/
        addMemoriesBTN=findViewById(R.id.addMemoriesBTN);
        viewMemoriesBTN=findViewById(R.id.viewMemoriesBTN);

    }

    private void initRecyclerView() {
        expenseRecycler.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(TourDetails.this, expenseList,tourID);
        expenseRecycler.setAdapter(expenseAdapter);


    }

    private void getDataFromDB() {

        DatabaseReference tourDB = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID).child("ExpenseList");
        // DatabaseReference tourDB = firebaseDatabase.getReference();

        tourDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    expenseList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Expense expense = data.getValue(Expense.class);
                        expenseList.add(expense);
                        expenseAdapter.notifyDataSetChanged();

                    }

                } else {
                    expenseList.clear();
                    expenseAdapter.notifyDataSetChanged();
                    Toast.makeText(TourDetails.this, "Empty Expense List", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(TourDetails.this, ""+databaseError, Toast.LENGTH_SHORT).show();

            }
        });
      /* tourDB.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   Toast.makeText(TourList.this, "exists", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });*/
    }

    public void addExpense(View view) {

        Intent intent = new Intent(TourDetails.this, AddExpense.class);
        intent.putExtra("tourID",tourID);
        startActivity(intent);
    }
}
