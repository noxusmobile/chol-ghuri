package com.example.cholghuri;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.nio.Buffer;

public class ResetPassword extends AppCompatActivity {
 private EditText resetPassET;
 private Button   resetpassBTN;
 private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initialize();
        resetpassBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetfun();
            }
        });

    }

    private void resetfun() {
        String email=resetPassET.getText().toString().trim();

        if(email.isEmpty())
        {
            resetPassET.setError("Enter an email address");
            resetPassET.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            resetPassET.setError("In valid email");
            resetPassET.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(ResetPassword.this, "Email Send", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ResetPassword.this, "Failed Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        }






    private void initialize() {
        mAuth=FirebaseAuth.getInstance();
        resetpassBTN=findViewById(R.id.resetPassBTN);
        resetPassET=findViewById(R.id.resetPassET);
    }
}
