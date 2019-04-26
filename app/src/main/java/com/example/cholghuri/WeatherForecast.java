package com.example.cholghuri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cholghuri.weather.WeatherForecastResponse;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecast extends AppCompatActivity {
    private WeatherForecastResponse weatherForecastResponseList;
    private double lat=23.7508671;
    private double lon=90.3913638;
    private String units="metric";
    private WeatherForecastAdapter weatherForecastAdapter;
    int temp,pressure;
    private RecyclerView weatherForecastRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        weatherForecastRecycler=findViewById(R.id.weatherForecastRecyclerID);
        setTitle("Weather Forecast");
        getWeatherForecastData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
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

    private void getWeatherForecastData() {
        Weather_service weatherService =RetrofitClass.getRetrofitInstance().create(Weather_service.class);
        String url = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",lat,lon,units,getResources().getString(R.string.weather_key));
        Call<WeatherForecastResponse> weatherForecastResponseCall=weatherService.getWeatherForecastData(url);
        weatherForecastResponseCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if(response.code()==200){
                    weatherForecastResponseList=response.body();
                    weatherForecastAdapter=new WeatherForecastAdapter(weatherForecastResponseList,WeatherForecast.this);
                    weatherForecastRecycler.setLayoutManager(new LinearLayoutManager(WeatherForecast.this));
                    weatherForecastRecycler.setAdapter(weatherForecastAdapter);

                    // Toast.makeText(MainActivity.this,String.valueOf(weatherForecastResponseList.getList().size()), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                Toast.makeText(WeatherForecast.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });












    }
}
