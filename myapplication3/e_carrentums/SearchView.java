package com.tryapp.myapplication3.e_carrentums;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchView extends AppCompatActivity {
    private ArrayList<Car> rentingList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchViewAdapter adapter;
    private SearchView searchView;
    private Button KKTM;
    private Button KKTF;
    private Button KKTPAR;

    private int backButtonPresses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        recyclerView = findViewById(R.id.s_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        EditText editText=findViewById(R.id.editTextTextPersonName2);
        KKTM=findViewById(R.id.button12);
        KKTPAR=findViewById(R.id.button13);
        KKTF= findViewById(R.id.button14);


        rentingList=new ArrayList<>();


        retrieveDataFromFirebase();
        setadapter();

        KKTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "KKTM";
                filter(text);
            }
        });
        KKTPAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "KKTPAR";
                filter(text);
            }
        });
        KKTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "KKTF";
                filter(text);
            }
        });

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
        ArrayList<Car>filteredList=new ArrayList<>();
        for(Car item: rentingList){
            if(item.getTempatAmbilKereta().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
    private void setadapter() {
        adapter=new SearchViewAdapter(rentingList,this);
        recyclerView.setAdapter(adapter);
    }



    private void retrieveDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hosting");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (snapshot.hasChildren()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        if (userId.equals(currentUserId)) {
                            // Skip data of the current user

                            continue;
                        }
                        for (DataSnapshot carSnapshot : userSnapshot.getChildren()) {

                            Car car = carSnapshot.getValue(Car.class);
                            if (car != null && !car.getStatus_kereta().equals("Not Available")&& !car.getStatus_kereta().equals("Requested")&&!car.getStatus_kereta().equals("Rented") ) {
                                // Do something with the user ID and car object
                                rentingList.add(car);

                            }
                        }
                    }
                }
                else {
                    setContentView(R.layout.noregistercar);
                    Toast.makeText(SearchView.this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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