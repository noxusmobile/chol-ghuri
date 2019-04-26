package com.example.cholghuri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.cholghuri.Common.Common;
import com.example.cholghuri.weather.WeatherForecastResponse;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {
    int pressure;
    WeatherForecast weatherForecast= new WeatherForecast();
    WeatherForecastResponse weatherForecastResponse;
    Context context;

    public WeatherForecastAdapter(WeatherForecastResponse weatherForecastResponse, Context context) {
        this.weatherForecastResponse = weatherForecastResponse;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_weather_forecast,viewGroup,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.with(context).load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResponse.list.get(i).weather.get(0).getIcon())
                .append(".png").toString()).into(viewHolder.imageForecastIV);




        viewHolder.dateTimeTV.setText(new StringBuilder(Common.convertUnixTodate(weatherForecastResponse.list.get(i).dt)));
        viewHolder.weatherDescriptionTV.setText(new StringBuilder(weatherForecastResponse.list.get(i).weather.get(0).getDescription()));

        viewHolder.tempForecastTV.setText(new StringBuilder(String.valueOf(weatherForecastResponse
                .list.get(i).main.getTemp())).append(" °C"));
        viewHolder.windForecastTV.setText(new StringBuilder(String.valueOf(weatherForecastResponse
                .list.get(i).wind.getSpeed())).append(" m/s"));

        pressure=(int)Math.round(weatherForecastResponse
                .list.get(i).main.getPressure());
        viewHolder.pressureForecastTV.setText(new StringBuilder(String.valueOf(pressure)).append(" hPa"));
        viewHolder.humidityForecastTV.setText(new StringBuilder(String.valueOf(weatherForecastResponse
                .list.get(i).main.getHumidity())).append(" %"));




    }

    @Override
    public int getItemCount() {
        return weatherForecastResponse.list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeTV,tempForecastTV,windForecastTV,pressureForecastTV,humidityForecastTV,weatherDescriptionTV;
        ImageView imageForecastIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeTV=itemView.findViewById(R.id.dateTimeTVID);
            weatherDescriptionTV=itemView.findViewById(R.id.weatherDescriptionTVID);
            tempForecastTV=itemView.findViewById(R.id.tempForcastTVID);
            windForecastTV=itemView.findViewById(R.id.windForecastTVID);
            pressureForecastTV=itemView.findViewById(R.id.pressureForcastTVID);
            humidityForecastTV=itemView.findViewById(R.id.humidityForecastTVID);
            imageForecastIV=itemView.findViewById(R.id.imageForecastIVID);
        }
    }
}
