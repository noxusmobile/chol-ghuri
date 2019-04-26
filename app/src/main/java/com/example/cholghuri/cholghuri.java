package com.example.cholghuri;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class cholghuri extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
