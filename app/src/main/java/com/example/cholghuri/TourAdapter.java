package com.example.cholghuri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String userID;


    private List<Tour> tourList;
    private Context context;

    public TourAdapter(Context context,List<Tour> tourList) {
        this.tourList = tourList;
        this.context = context;
    }


    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_list_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Tour currentTour = tourList.get(i);
        viewHolder.tripNameTV.setText(currentTour.getTourTitle());
        viewHolder.tripDescriptionTV.setText(currentTour.getTourDetails());
        String temp = String.valueOf(currentTour.getTourAmount());
        viewHolder.tripAmountTV.setText(temp);

        viewHolder.addExpenseActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,AddExpense.class);
                intent.putExtra("tourID",currentTour.getTourID());
                intent.putExtra("tourTitle",currentTour.getTourTitle());
                intent.putExtra("tourDetails",currentTour.getTourDetails());
                intent.putExtra("tourAmount",currentTour.getTourAmount());
                intent.putExtra("tourStartDate",currentTour.getTourStratDate());
                intent.putExtra("tourEndDate",currentTour.getTourEndDate());

                context.startActivity(intent);

            }
        });


        /*viewHolder.tripDeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "asdsadsadasd", Toast.LENGTH_SHORT).show();
            }
        });
*/
        viewHolder.tripDeleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserLIst").child(userID).child("TourList");
                      databaseReference.child(currentTour.getTourID()).removeValue();
                    }

                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();

            }
        });

        viewHolder.tripUpdateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,UpdateTour.class);
                intent.putExtra("tourID",currentTour.getTourID());
                intent.putExtra("tourTitle",currentTour.getTourTitle());
                intent.putExtra("tourDetails",currentTour.getTourDetails());
                intent.putExtra("tourAmount",currentTour.getTourAmount());
                intent.putExtra("tourStartDate",currentTour.getTourStratDate());
                intent.putExtra("tourEndDate",currentTour.getTourEndDate());

                context.startActivity(intent);



            }
        });






    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tripNameTV, tripDescriptionTV, tripAmountTV;
        private Button tripDetailsBTN,tripMomentsBTN,tripDeleteBTN,tripUpdateBTN,addExpenseActivityBTN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTV = itemView.findViewById(R.id.tripNameTV);
            tripDescriptionTV = itemView.findViewById(R.id.tripDescriptionTV);
            tripAmountTV = itemView.findViewById(R.id.tripAmountTV);
            tripDetailsBTN = itemView.findViewById(R.id.tripDetailsBTN);
            tripMomentsBTN = itemView.findViewById(R.id.tripMomentsBTN);
            tripDeleteBTN = itemView.findViewById(R.id.tripDeleteBTN);
            tripUpdateBTN=itemView.findViewById(R.id.tripUpdateBTN);
            addExpenseActivityBTN=itemView.findViewById(R.id.addExpenseActivityBTN);

        }
    }


}
