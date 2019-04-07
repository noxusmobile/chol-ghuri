package com.example.cholghuri;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_tour_list,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Tour currentTour = tourList.get(i);
        viewHolder.tripNameTV.setText(currentTour.getTourTitle());
        viewHolder.tripDescriptionTV.setText(currentTour.getTourDetails());
        String temp = String.valueOf(currentTour.getTourAmount());
        viewHolder.tripAmountTV.setText(temp);

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tripNameTV, tripDescriptionTV, tripAmountTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTV = itemView.findViewById(R.id.tripNameTV);
            tripDescriptionTV = itemView.findViewById(R.id.tripDescriptionTV);
            tripAmountTV = itemView.findViewById(R.id.tripAmountTV);

        }
    }
}
