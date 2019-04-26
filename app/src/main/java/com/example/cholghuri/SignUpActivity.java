package com.example.cholghuri;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    private Button btnLogin,btnSignup,button3;
    private EditText editText,editText2,editText3;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        editText3=findViewById(R.id.editText3);

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
    String  confpassword=editText3.getText().toString().trim();
    String  email=editText.getText().toString().trim();
  /*  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";*/

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
     /*   if(!email.matches(emailPattern))
        {
            editText.setError("Enter a valid email address");
            editText.requestFocus();
            return;
        }*/
        //checking the validity of the password
        if(password.isEmpty())
        {
            editText2.setError("Enter a password");
            editText2.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editText2.setError("Length must be atleast 6 ");
            editText2.requestFocus();
            return;
        }
        else if(!password.equals(confpassword)){

            editText3.setError("password Not matching");
            editText3.requestFocus();
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
             /* Toast.makeText(SignUpActivity.this, "Already Register", Toast.LENGTH_SHORT).show();*/
              showToast();

          }
      }



  });}
    private void showToast() {
        LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.toast_root));
        TextView toastText=layout.findViewById(R.id.toast_text);
        ImageView toastImg=layout.findViewById(R.id.toast_img);
        toastText.setText("ALready register");
        toastImg.setImageResource(R.drawable.error);

        Toast toast=new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}
