package com.tryapp.myapplication3.e_carrentums;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Backyard_itemview extends AppCompatActivity {
    private ArrayList<BackYard_details> backList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Backyard_itemviewAdapter adapter;
    private int backButtonPresses = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backyard_itemview);


        recyclerView = findViewById(R.id.Recycler_bk_itemviewAll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        EditText editText=findViewById(R.id.editTextTextPersonName3);



        backList=new ArrayList<>();

        retrieveDataFromFirebase();
        setadapter();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });
    }

    private void filter(String text){
        ArrayList<BackYard_details>filteredList=new ArrayList<>();
        for(BackYard_details item: backList){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void retrieveDataFromFirebase() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Backyard_Sale");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        for (DataSnapshot carSnapshot : userSnapshot.getChildren()) {
                            BackYard_details itembk = carSnapshot.getValue(BackYard_details.class);
                            if (itembk != null) {
                                // Do something with the user ID and car object
                                backList.add(itembk);

                            }
                        }
                    }
                }
                else {
                    setContentView(R.layout.noitemadd);
                    Toast.makeText(Backyard_itemview.this, "No data found", Toast.LENGTH_SHORT).show();

                    return;
                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setadapter() {
        adapter=new Backyard_itemviewAdapter(backList,this);
        recyclerView.setAdapter(adapter);
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