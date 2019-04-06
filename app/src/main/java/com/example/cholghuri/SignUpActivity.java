package com.example.cholghuri;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    private Button btnLogin,btnSignup,button3;
    private EditText editText,editText2;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Signup");
        mAuth = FirebaseAuth.getInstance();
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);

        button3=findViewById(R.id.button3);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

button3.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        userregister();
    }
});
    }

    private void userregister() {
    String  password=editText2.getText().toString().trim();
    String  email=editText.getText().toString().trim();

        if(email.isEmpty())
        {
            editText.setError("Enter an email address");
            editText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editText.setError("Enter a valid email address");
            editText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            editText2.setError("Enter a password");
            editText2.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editText2.setError("Length must be atleast ^ ");
            editText2.requestFocus();
            return;
        }
  mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()){
              finish();
              Intent intent=new Intent(getApplicationContext(),Userprofile.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
          } else {
              Toast.makeText(SignUpActivity.this, "Already Register", Toast.LENGTH_SHORT).show();

          }
      }



    });}
}
