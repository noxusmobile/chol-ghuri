/*
package com.example.cholghuri;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Session extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser !=null){
            Intent intent=new Intent(Session.this,Userprofile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
    }
}
*/
