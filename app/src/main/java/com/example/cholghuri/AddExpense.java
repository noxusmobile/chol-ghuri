package com.example.cholghuri;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity {


    private EditText addExpenseTitleET, addExpenseDetailsET, addExpenseAmountET;
    private Button addExpenseBTN;
    private FirebaseAuth firebaseauth;
    private FirebaseDatabase firebaseDatabase;
    private String userID;
    private String tourID;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        firebaseauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userID = firebaseauth.getCurrentUser().getUid();

        initialize();
        onclick();

        Toast.makeText(this, ""+tourID, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {

        addExpenseTitleET =findViewById(R.id.addExpenseTitleET);
        addExpenseDetailsET =findViewById(R.id.addExpenseDetailsET);
        addExpenseAmountET =findViewById(R.id.addExpenseAmountET);
        addExpenseBTN = findViewById(R.id.addExpenseBTN);

        Intent intent = getIntent();
        userID = firebaseauth.getCurrentUser().getUid();
        tourID = intent.getStringExtra("tourID");

    }

    private void onclick() {

        addExpenseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ExpenseTitle = addExpenseTitleET.getText().toString();
                String ExpenseDetails = addExpenseDetailsET.getText().toString();
                String temp = addExpenseAmountET.getText().toString();
                int ExpenseAmount = Integer.valueOf(temp);

                sendExpenseDataToDatabase(new Expense(ExpenseTitle,ExpenseDetails,ExpenseAmount));


            }
        });


    }

    private void sendExpenseDataToDatabase(Expense expense) {


        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID).child("ExpenseList");
        String Id = databaseReference.push().getKey();

        expense.setExpenseID(Id);
        databaseReference.child(Id).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    //Toast.makeText(AddTour.this, "complete", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}