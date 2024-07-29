package com.tryapp.myapplication3.e_carrentums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;

public class Dummy extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<Dummylist>List;
    DummyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        recyclerView=(RecyclerView) findViewById(R.id.dumyView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List=new ArrayList<>();




            List.add(new Dummylist("Azizi"));
        List.add(new Dummylist("Jane"));
            setAdapter();

    }

    private void setAdapter() {
     adapter=new DummyAdapter(this,List);
     recyclerView.setAdapter(adapter);
    }

    private void setUserinfo() {


    }
}