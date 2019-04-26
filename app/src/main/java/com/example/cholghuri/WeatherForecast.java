package com.example.cholghuri;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherForecast extends AppCompatActivity implements OnMapReadyCallback {
    private WeatherForecastResponse weatherForecastResponseList;
    private double lat = 23.7508671;
    private double lon = 90.3913638;
    private String units = "metric";
    private WeatherForecastAdapter weatherForecastAdapter;
    int temp, pressure;
    private RecyclerView weatherForecastRecycler;
    private LocationManager locationManager;

    private double latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        weatherForecastRecycler = findViewById(R.id.weatherForecastRecyclerID);
        setTitle("Weather Forecast");
        myLocation2();
        //getWeatherForecastData();


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

    private void getWeatherForecastData(double lat,double lon) {
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

    /*private void getWeatherForecastData() {
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


    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void myLocation2() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        else {
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        LatLng myCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        getWeatherForecastData(latitude,longitude);
                        //Toast.makeText(WeatherForecast.this, "" + latitude + "lon " + longitude, Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }

    }

}