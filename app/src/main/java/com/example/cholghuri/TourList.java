package com.example.cholghuri;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourList extends AppCompatActivity {
    private Date date = null;
    private RecyclerView tourRecycler;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<Tour> tourList;
    private TourAdapter tourAdapter;
    private String userID;
    private long fromDateInMS = 0,toDateInMS = 0;
    private Button dateFrom , dateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        setTitle("Tour List");


        initialize();
        initRecyclerView();
        onclick();
        getDataFromDB();
    }

    private void onclick() {

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tourRecycler = findViewById(R.id.tourRecycler);
        userID = firebaseAuth.getCurrentUser().getUid();

        dateFrom = findViewById(R.id.dateFrom);
        dateTo = findViewById(R.id.dateTo);

        tourList = new ArrayList<>();
    }


    private void initRecyclerView() {
        tourRecycler.setLayoutManager(new LinearLayoutManager(this));
        tourAdapter = new TourAdapter(TourList.this, tourList);
        tourRecycler.setAdapter(tourAdapter);
    }


    public void addTour(View view) {

        Intent intent = new Intent(this, AddTour.class);
        startActivity(intent);
    }

    private void getDataFromDB() {

        DatabaseReference tourDB = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList");
        // DatabaseReference tourDB = firebaseDatabase.getReference();

        tourDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    tourList.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Tour tour = data.getValue(Tour.class);
                        tourList.add(tour);
                        tourAdapter.notifyDataSetChanged();

                    }

                } else {
                    Toast.makeText(TourList.this, "Empty Tour List", Toast.LENGTH_SHORT).show();
                    tourAdapter.notifyDataSetChanged();

                }
                tourAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(TourList.this, "" + databaseError, Toast.LENGTH_SHORT).show();

            }
        });



                //7999999999999999

    }
    private void filterByDate(long fromDateInMS,long toDateInMS){

            if(toDateInMS < fromDateInMS)
            {
                Toast.makeText(this, "From date can not be earlier than To date", Toast.LENGTH_SHORT).show();
            }
        else {

            if (toDateInMS == 0){
                toDateInMS = 7999999999999999l;
            }
            else{
                toDateInMS = toDateInMS;
            }

            DatabaseReference tourDB = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList");


                tourDB.orderByChild("tourStratDate").startAt(fromDateInMS).endAt(toDateInMS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.exists()) {
                            tourList.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Tour tour = data.getValue(Tour.class);
                                tourList.add(tour);
                                tourAdapter.notifyDataSetChanged();

                            }

                        } else {
                            Toast.makeText(TourList.this, "Empty Tour List", Toast.LENGTH_SHORT).show();
                            tourAdapter.notifyDataSetChanged();

                        }
                        tourAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(TourList.this, "" + databaseError, Toast.LENGTH_SHORT).show();

                    }
                });



            }




    }

   /* private void openDatePickerFrom() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String selectedDate = year + "/" + month + "/" + dayOfMonth + " 00:00:00";
                try {
                    date = dateAndTimeSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                selectedFromDateinMS = date.getTime();
                //msDate=selectedDateinMS;
                dateFromBtn.setText(dateSDF.format(date));

                if (selectType.equals("")) {
                    expenseList.clear();
                    showDataFromDate();
                    customExpenseAdapter.notifyDataSetChanged();

                }
                else {
                    expenseList.clear();
                    showDataFromDateSpinner();
                    customExpenseAdapter.notifyDataSetChanged();
                }
            }

        };
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);

        datePickerDialog.show();



    }*/

}
