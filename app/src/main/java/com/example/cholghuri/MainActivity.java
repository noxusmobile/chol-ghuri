package com.example.cholghuri;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private Button btnLogin,btnSignup,button3,locationMapBtn;
    private EditText editText,editText2;
    private TextView textView5;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnSignup=(Button)findViewById(R.id.btnSignup);
        editText=findViewById(R.id.editText);
        editText2=findViewById(R.id.editText2);
        textView5=findViewById(R.id.textView5);
        button3=findViewById(R.id.button3);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }


        });
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetpassIN();
            }
        });
    }

    private void forgetpassIN() {
        Intent intent=new Intent(MainActivity.this,ResetPassword.class);
        startActivity(intent);
        finish();

    }

    private void signIn() {
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
            editText2.setError("Length must be atleast 6 ");
            editText2.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                Intent intent=new Intent(getApplicationContext(),Userprofile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                } else {

                    showToast();
                }
            }



        });}
    private void showToast() {
        LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.toast_root));
        TextView toastText=layout.findViewById(R.id.toast_text);
        ImageView toastImg=layout.findViewById(R.id.toast_img);
        toastText.setText("Failed");
        toastImg.setImageResource(R.drawable.error);

        Toast toast=new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}


