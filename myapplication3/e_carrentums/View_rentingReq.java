package com.tryapp.myapplication3.e_carrentums;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

public class View_rentingReq extends AppCompatActivity {
    private TextView carplate;
    private TextView Name;
    private TextView contactOw;
    private TextView status;
    private Button cancel;
    private Button contactown;
    private int backButtonPresses = 0;
    private static final String CHANNEL_ID = "Rental Request Channel";
    private static final int NOTIFICATION_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_renting_req);

        carplate = findViewById(R.id.textView47);
        Name = findViewById(R.id.textView49);
        status = findViewById(R.id.textView52);

        cancel = findViewById(R.id.button4);
        contactown =findViewById(R.id.button15);
        contactown.setVisibility(View.GONE);

        contactown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(View_rentingReq.this, chatSession.class);
                startActivity(homeIntent);
                finish();

            }
        });


        String uid = getIntent().getStringExtra("UID");




        showdata(uid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(View_rentingReq.this);
                builder.setTitle("Cancel Order");
                builder.setMessage("Are you sure want Cancel order?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteDetails();
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
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Renting").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(View_rentingReq.this, "Order Canceled", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void deleteimage() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Renting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    RentingDetails rent=snapshot.getValue(RentingDetails.class);
                    String palate=rent.getCustomerMatric();

                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    String file=palate+"/";
                    String imageic=palate+"(IC)"+".jpg";
                    String imageLC=palate+"(Licence)"+".jpg";
                    StorageReference fileRef = storageRef.child("Renting/"+imageic);
                    fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully

                            StorageReference ref= storageRef.child("Renting/"+imageLC);
                            ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Renting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RentingDetails details = snapshot.getValue(RentingDetails.class);
                if (snapshot.exists()) {
                    String carplate2 = details.getOwCar();
                    String custName = details.getCustomerName();
                    String status2 = details.getStatusRequest();
                    String contact = details.getOwnerNumber();
                    String verify = "Accepted";
                    String reject="Rejected";
                    contactOw = findViewById(R.id.ownerno);



                    carplate.setText(carplate2);
                    Name.setText(custName);
                    status.setText(status2);
                    if (status2.equals(verify)) {
                        contactOw.setText(contact);
                        showNotification("Rental Request Accepted", "Your rental request has been accepted.");
                        cancel.setVisibility(View.GONE);
                        contactown.setVisibility(View.GONE);

                    }
                    if (status2.equals(reject)) {
                        showNotification("Rental Request Rejected", "Your rental request has been rejected.");
                        cancel.setText("Rent next Car");
                        cancel.setVisibility(View.VISIBLE);

                    }
                } else {
                    Toast.makeText(View_rentingReq.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.no_order);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void showNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.e_carent_ums_1)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        Intent intent = new Intent(this, Homepage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);

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