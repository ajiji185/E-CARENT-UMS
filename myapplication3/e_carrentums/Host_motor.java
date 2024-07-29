package com.tryapp.myapplication3.e_carrentums;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Host_motor extends AppCompatActivity {
    private ArrayList<motorcycle> HostList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HostMotorAdapter adapter;
    private int backButtonPresses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_motor);
    }
}