package com.tryapp.myapplication3.e_carrentums;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Starter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler h= new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                showStarter();
            }
        },1000);

    }

    public void showStarter(){
        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(this, Homepage.class);
            startActivity(intent);
            finish();

        } else{
            Intent i = new Intent(this, login_user.class);
            startActivity(i);
            finish();



        }

    }

}