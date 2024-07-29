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

public class BackYard_list extends AppCompatActivity {
    private ArrayList<BackYard_details> bkList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Backyard_listAdapter adapter;
    private int backButtonPresses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_yard_list);



        recyclerView = findViewById(R.id.bk_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        bkList=new ArrayList<>();

        retrieveDataFromFirebase();
        setadapter();
    }

    private void setadapter() {
        adapter=new Backyard_listAdapter(bkList,this);
        recyclerView.setAdapter(adapter);


    }

    private void retrieveDataFromFirebase() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Backyard_Sale")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                        BackYard_details det = carSnapshot.getValue(BackYard_details.class);
                        if (det != null) {
                            // Do something with the user ID and car object
                            bkList.add(det);

                        }


                    }

                } else {
                    Toast.makeText(BackYard_list.this, "No data found", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.noitemadd);
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}