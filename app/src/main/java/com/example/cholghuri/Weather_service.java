package com.example.cholghuri;

import com.example.cholghuri.weather.WeatherForecastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;



public interface Weather_service {
    @GET
    Call<WeatherForecastResponse> getWeatherForecastData(@Url String url);

}
