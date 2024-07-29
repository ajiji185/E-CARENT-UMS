package com.tryapp.myapplication3.e_carrentums;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Host_list extends AppCompatActivity {
    private ArrayList<Car> HostList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HostAdapter adapter;
    private int backButtonPresses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_list);

        recyclerView=findViewById(R.id.Host_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        HostList=new ArrayList<>();

        retrieveDataFirebase();
        setadapter();


    }

    private void setadapter() {
        adapter=new HostAdapter(HostList,this);
        recyclerView.setAdapter(adapter);
    }

    private void retrieveDataFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hosting").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                        Car car = carSnapshot.getValue(Car.class);
                        if (car != null) {
                            // Do something with the user ID and car object
                            HostList.add(car);

                        }


                    }

                } else {
                    Toast.makeText(Host_list.this, "No data found", Toast.LENGTH_SHORT).show();
                    Emptycar();
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Host_list.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        backButtonPresses++;
        handleBackButtonPress();
        super.onBackPressed();
    }
    private void handleBackButtonPress() {
        if (backButtonPresses == 1) {
            // redirect to home page
            Intent homeIntent = new Intent(this, Homepage.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }

    private void Emptycar(){
        Intent homeIntent = new Intent(this, nocar.class);
        startActivity(homeIntent);
        finish();
    }
}