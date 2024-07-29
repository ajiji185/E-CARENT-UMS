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

public class customer_list extends AppCompatActivity {
    private ArrayList<RentingDetails> Cust_List = new ArrayList<>();
    private RecyclerView recyclerView;
    private Cust_adapter adapter;
    private int backButtonPresses = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        recyclerView=findViewById(R.id.Cust_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Cust_List=new ArrayList<>();

        retrieveDataFirebase();
        setadapter();


    }

    private void setadapter() {
        adapter=new Cust_adapter(Cust_List,this);
        recyclerView.setAdapter(adapter);
    }

    private void retrieveDataFirebase() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user= snapshot.getValue(User.class);
                String matrik=user.MatricNumber;

                DatabaseReference Cust=FirebaseDatabase.getInstance().getReference("Renting");
                Cust.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if (childSnapshot.child("ownerMatric").getValue(String.class).equals(matrik)) {
                                    RentingDetails cust_listed = childSnapshot.getValue(RentingDetails.class);

                                    if (cust_listed != null) {
                                        Cust_List.add(cust_listed);
                                    }
                                    adapter.notifyDataSetChanged();


                                }else{
                                    setContentView(R.layout.no_cust);
                                    return;
                                }


                            }
                        }else {
                            Toast.makeText(customer_list.this, "No Data at Renting", Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.no_cust);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void home() {
        Intent intent=new Intent(this,Homepage.class);
        startActivity(intent);
        finish();
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