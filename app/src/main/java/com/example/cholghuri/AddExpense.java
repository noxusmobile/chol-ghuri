package com.example.cholghuri;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddExpense extends AppCompatActivity {


    private TextInputEditText addExpenseTitleET, addExpenseDetailsET, addExpenseAmountET;
    private Button addExpenseBTN;
    private FirebaseAuth firebaseauth;
    private FirebaseDatabase firebaseDatabase;
    private String userID;
    private String tourID;
    private String expenseID;
    private int budget;
    private int totalExpense = 0;
    private int temp;
    private int ExpenseAmount;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        setTitle("Add Expense");

        firebaseauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        userID = firebaseauth.getCurrentUser().getUid();

        initialize();
        onclick();
        validate();
        check_expense();
/*
      Toast.makeText(this, ""+tourID, Toast.LENGTH_SHORT).show();
*/
    }

    private void check_expense() {
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 budget = dataSnapshot.child("tourAmount").getValue(Integer.class);

/*
                Toast.makeText(AddExpense.this, ""+budget, Toast.LENGTH_SHORT).show();
*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       databaseReference.child("ExpenseList").orderByChild("tourID").equalTo(tourID).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for(DataSnapshot data: dataSnapshot.getChildren()){

                   totalExpense += data.child("expenseAmount").getValue(Integer.class);


               }


               //totalExpense=totalExpense+dataSnapshot.child("expenseAmount").getValue(Integer.class);;

               //Toast.makeText(AddExpense.this, ""+totalExpense, Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


    }

    private void validate() {


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


                if(ExpenseTitle.isEmpty())
                {
                    addExpenseTitleET.setError("Enter Expense title");
                    addExpenseTitleET.requestFocus();
                    return;
                }
                if( ExpenseDetails.isEmpty())
                {
                    addExpenseDetailsET.setError("Enter Tour Details");
                    addExpenseDetailsET.requestFocus();
                    return;
                }
                if( temp.isEmpty())
                {
                    addExpenseAmountET.setError("Enter Tour Budget");
                    addExpenseAmountET.requestFocus();
                    return;
                }
                else{
                    ExpenseAmount = Integer.valueOf(temp);
                int tempExpense = totalExpense;

                tempExpense = tempExpense + ExpenseAmount;
                if (tempExpense >= budget) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddExpense.this);
                    alert.setTitle("Add Expense");
                    alert.setMessage("Expense is exciding Budget.");
                    alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }

                else {
                    sendExpenseDataToDatabase(new Expense(ExpenseTitle, ExpenseDetails, ExpenseAmount));
                }

            }}
        });


    }

    private void sendExpenseDataToDatabase(Expense expense) {





        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList").child(tourID).child("ExpenseList");
        String Id = databaseReference.push().getKey();

        expenseID = Id;

        expense.setExpenseID(Id);
        expense.setTourID(tourID);
        databaseReference.child(Id).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {



                    Toast.makeText(AddExpense.this, "Expense Added", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }



}