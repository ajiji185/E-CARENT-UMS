package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_user extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private EditText login_email;
    private EditText login_password;
    private ImageButton viewpasss;
    private int Buttonpress=0;

    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        progressBar = findViewById(R.id.progressBar);
        login_email=findViewById(R.id.Login_Email);
        login_password=findViewById(R.id.login_password);
        viewpasss=findViewById(R.id.imageButton);



        viewpasss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });










        //initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() !=null){
            finish();
            return;
        }

        //login btn
        Button login_button = findViewById(R.id.Login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                authenticateUser();

            }
        });

        TextView switchRegister = findViewById(R.id.switchRegister);
            switchRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressBar.setVisibility(View.VISIBLE);
                    switchRegister();
                }
            });

    }

    private void authenticateUser(){


        String email= login_email.getText().toString();
        String password= login_password.getText().toString();

        if(email.isEmpty()|| password.isEmpty()){
            Toast.makeText(this,"fill all fields",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           showMainActivity();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(login_user.this,"Authentication failed",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

    }
    @Override
    public void onBackPressed() {

        handleBackButtonPress();

    }
    private void handleBackButtonPress() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit E-Carent");
        //alert dialog
        builder.setMessage("Are you sure want to exit?")
                .setCancelable(true)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

    }

    private void showMainActivity(){
        Intent intent = new Intent(this,Homepage.class);
        startActivity(intent);
        finish();
    }

    private void switchRegister(){
        Intent intent = new Intent(this,register.class);
        startActivity(intent);
        finish();
        progressBar.setVisibility(View.VISIBLE);
    }

}