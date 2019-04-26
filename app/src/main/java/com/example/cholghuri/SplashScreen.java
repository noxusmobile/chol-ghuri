package com.example.cholghuri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressId;
 private int progressbar = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*For full screen*/
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        progressId = findViewById(R.id.progressId);
        /*Create Thread  its allows us change progress bar time to time*/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
              startThread();
              landingpage();
            }
        });
        thread.start();
    }

    private void landingpage() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startThread() {
        for(progressbar=20;progressbar<=100;progressbar=progressbar+20){
            try {
                Thread.sleep(1000);
                progressId.setProgress(progressbar);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}