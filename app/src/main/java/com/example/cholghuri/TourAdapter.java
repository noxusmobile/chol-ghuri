package com.example.cholghuri;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {


    private List<Tour> tourList;

    public TourAdapter(List<Tour> tourList) {
        this.tourList = tourList;
    }


    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_list_layout, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Tour currentTour = tourList.get(i);
        viewHolder.tripNameTV.setText(currentTour.getTourTitle());
        viewHolder.tripDescriptionTV.setText(currentTour.getTourDetails());
       String temp = String.valueOf(currentTour.getTourAmount());
       /* viewHolder.tripNameTV.setText("123");
        viewHolder.tripDescriptionTV.setText("678");*/
        viewHolder.tripAmountTV.setText(temp);

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tripNameTV, tripDescriptionTV, tripAmountTV;
        private Button tripDetailsBTN,tripMomentsBTN,tripDeleteBTN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTV = itemView.findViewById(R.id.tripNameTV);
            tripDescriptionTV = itemView.findViewById(R.id.tripDescriptionTV);
            tripAmountTV = itemView.findViewById(R.id.tripAmountTV);
            tripDetailsBTN = itemView.findViewById(R.id.tripDetailsBTN);
            tripMomentsBTN = itemView.findViewById(R.id.tripMomentsBTN);
            tripDeleteBTN = itemView.findViewById(R.id.tripDeleteBTN);

        }
    }
}
