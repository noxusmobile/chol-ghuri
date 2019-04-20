package com.example.cholghuri;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TourList extends AppCompatActivity {

    private RecyclerView tourRecycler;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<Tour> tourList;
    private TourAdapter tourAdapter;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);



        initialize();
        initRecyclerView();
        getDataFromDB();
    }


    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tourRecycler = findViewById(R.id.tourRecycler);
        userID = firebaseAuth.getCurrentUser().getUid();
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
                    Toast.makeText(TourList.this, "Empty database", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(TourList.this, "" + databaseError, Toast.LENGTH_SHORT).show();

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

}
