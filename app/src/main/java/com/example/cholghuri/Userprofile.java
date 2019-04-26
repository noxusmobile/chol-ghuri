package com.example.cholghuri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cholghuri.weather.WeatherForecastResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Userprofile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Button signoutBtn;
    private WeatherForecastResponse weatherForecastResponseList;
    private TextView tempTV,windTV,humidityTV,pressureTV,weatherTypeTV;
    private ImageView weatherIconIV;
    private double lat=23.7508671;
    private double lon=90.3913638;
    int temp,pressure;
    private String units="metric";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        setTitle("Home");


        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        signoutBtn=findViewById(R.id.signoutBtn);
       /* signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signoutfun();
            }


        });*/

        tempTV=findViewById(R.id.tempTVID);
        windTV=findViewById(R.id.windTVID);
        humidityTV=findViewById(R.id.humidityTVID);
        pressureTV=findViewById(R.id.pressureTVID);
        weatherTypeTV=findViewById(R.id.weatherTypeTVID);

        weatherIconIV=findViewById(R.id.weatherIconIVID);
        getWeatherUpdate();

    }
    private void getWeatherUpdate() {
        Weather_service weatherService= RetrofitClass.getRetrofitInstance().create(Weather_service.class);
        String url = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",lat,lon,units,getResources().getString(R.string.weather_key));
        Call<WeatherForecastResponse> weatherForecastResponseCall=weatherService.getWeatherForecastData(url);
        weatherForecastResponseCall.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if(response.code()==200){
                    weatherForecastResponseList=response.body();
                    weatherTypeTV.setText(weatherForecastResponseList.getList().get(0).weather.get(0).getDescription());
                    temp=(int)Math.round(weatherForecastResponseList.getList().get(0).getMain().getTemp());
                    tempTV.setText(temp+" °C");

                    Picasso.with(Userprofile.this).load(new StringBuilder("https://openweathermap.org/img/w/")
                            .append(weatherForecastResponseList.getList().get(0).weather.get(0).getIcon())
                            .append(".png").toString()).into(weatherIconIV);

                    windTV.setText(weatherForecastResponseList.getList().get(0).wind.getSpeed()+ " m/s");
                    humidityTV.setText(weatherForecastResponseList.getList().get(0).getMain().getHumidity()+" %");
                    //converting pressure from double to int
                    pressure=(int)Math.round(weatherForecastResponseList.getList().get(0).getMain().getPressure());
                    pressureTV.setText(pressure+" hPa");

                }

            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                Toast.makeText(Userprofile.this,t.getMessage(), Toast.LENGTH_SHORT).show();

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

   /* private void signoutfun() {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);

        startActivity(intent);
        finish();
    }*/

    public void nextActivity(View view) {
        Intent intent=new Intent(Userprofile.this,TourList.class);
        startActivity(intent);
    }

    public void weatherForecast(View view) {
        Intent intent= new Intent(Userprofile.this,WeatherForecast.class);
        startActivity(intent);
    }
}
