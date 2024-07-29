package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView tvName, tvMatricNumber;
    ProgressBar progressBar;
    AlertDialog.Builder builder;
    private int backButtonPresses = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //skip login


        progressBar = findViewById(R.id.progressBar3);
        //firebase initialize

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this, login_user.class);
            startActivity(intent);
            finish();
            return;
        }

        tvName = findViewById(R.id.showName);
        tvMatricNumber = findViewById(R.id.showMatric);

        Button editprofile = findViewById(R.id.edit_profile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUser();
            }
        });


        Button logout= findViewById(R.id.logut_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                logoutUser();

            }
        });



        ImageButton homepage= findViewById(R.id.back);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homepage();
            }
        });


        Button deleteAcc= findViewById(R.id.delete_acc);
        builder= new AlertDialog.Builder(this);
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //alert title
                builder.setTitle("Account delete Alert ");
                //alert dialog
                builder.setMessage("Are you sure want to delete this account?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteAcc();
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            }
                        }).show();




            }
        });

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    tvName.setText( user.FullName);
                    tvMatricNumber.setText( user.MatricNumber);
                }
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
        }
        super.onBackPressed();
    }
    private void editUser() {
        Intent intent = new Intent(this, Edit_profile.class);
        startActivity(intent);
        finish();
    }

    private void homepage() {
        Intent intent = new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }


    private void deleteAcc() {
        DatabaseReference hostingRef = FirebaseDatabase.getInstance().getReference().child("Hosting").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        hostingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    Toast.makeText(MainActivity.this, "Delete all Car first", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    DatabaseReference rentingRef = FirebaseDatabase.getInstance().getReference().child("Renting").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    rentingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {
                                Toast.makeText(MainActivity.this, "Clear all renting first", Toast.LENGTH_SHORT).show();
                                return;
                            }else {
                                DatabaseReference backyardRef = FirebaseDatabase.getInstance().getReference().child("Backyard_Sale").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                backyardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists() && snapshot.hasChildren()) {
                                            Toast.makeText(MainActivity.this, "Clear all items first", Toast.LENGTH_SHORT).show();
                                            return;
                                        }else {
                                            FirebaseDatabase.getInstance().getReference().child("users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(MainActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                                                    logoutUser();
                                                                }
                                                            });

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void  logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, login_user.class);
        startActivity(intent);
        finish();
        progressBar.setVisibility(View.GONE);
    }
}