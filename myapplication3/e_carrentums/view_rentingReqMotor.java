package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class view_rentingReqMotor extends AppCompatActivity {
    private TextView carplate;
    private TextView Name;
    private TextView contactOw;
    private TextView status;
    private Button cancel;
    private Button contact;
    private int backButtonPresses = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_renting_req_motor);
        carplate = findViewById(R.id.textView47motor);
        Name = findViewById(R.id.textView49motor);
        status = findViewById(R.id.textView52motor);

        cancel = findViewById(R.id.button4motor);
        contact=findViewById(R.id.button15motor);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(view_rentingReqMotor.this, chatSession.class);
                startActivity(homeIntent);
                finish();

            }
        });


        String uid = getIntent().getStringExtra("UID");




        showdata(uid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view_rentingReqMotor.this);
                builder.setTitle("Cancel Order");
                builder.setMessage("Are you sure want Cancel order?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteimage();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



            }
        });


    }
    private void deleteDetails() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Renting motor").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(view_rentingReqMotor.this, "Order Canceled", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void deleteimage() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Renting motor")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    RentingDetailsMotor rent=snapshot.getValue(RentingDetailsMotor.class);
                    String palate=rent.getCustomerMatricm();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    String file=palate+"/";
                    String imageic=palate+"(IC)"+".jpg";
                    String imageLC=palate+"(Licence)"+".jpg";
                    StorageReference fileRef = storageRef.child("Renting motor/"+imageic);
                    fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully

                            StorageReference ref= storageRef.child("Renting motor/"+imageLC);
                            ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    deleteDetails();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful delete
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void showdata(String getID) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Renting motor")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RentingDetailsMotor details = snapshot.getValue(RentingDetailsMotor.class);
                if (snapshot.exists()) {
                    String carplate2 = details.getOwmotorm();
                    String custName = details.getCustomerNamem();
                    String status2 = details.getStatusRequestm();
                    String contact = details.getOwnerNumberm();
                    String verify = "Accepted";
                    String reject="Rejected";
                    contactOw = findViewById(R.id.ownerno);

                    carplate.setText(carplate2);
                    Name.setText(custName);
                    status.setText(status2);
                    if (status2.equals(verify)) {
                        contactOw.setText(contact);
                        cancel.setVisibility(View.GONE);

                    }
                    if (status2.equals(reject)) {
                        Toast.makeText(view_rentingReqMotor.this, "REQUEST REJECTED", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(view_rentingReqMotor.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.no_order);
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
            finish();

        }
        super.onBackPressed();
    }


}