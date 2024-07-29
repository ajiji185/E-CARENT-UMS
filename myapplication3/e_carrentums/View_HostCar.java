package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class View_HostCar extends AppCompatActivity {


    private TextView ShowBrand;
    private TextView ShowCarName;
    private TextView ShowDate;
    private String Url;
    private ImageView CarImage;
    private FirebaseDatabase db;
    private FirebaseStorage storage;
    private Button update;
    private Button Delete;
    private int backButtonPresses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_host_car);










        //initialise
        Delete=findViewById(R.id.DeleteCar_btn);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(View_HostCar.this);
                builder.setTitle("Delete Car");
                builder.setMessage("Are you sure want to Stop Hosting?")
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

        update=findViewById(R.id.UpdateCar_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(View_HostCar.this);
                builder.setTitle("Update Car");
                builder.setMessage("Are you want to update your Hosting?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateView();

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



        //firebase initialize



        Toast.makeText(View_HostCar.this, "Retrieving Data", Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hosting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Car ar= snapshot.getValue(Car.class);
                if(ar != null){


                    Car car = snapshot.getValue(Car.class);
                    ShowBrand= findViewById(R.id.Show_Brand);
                    ShowCarName=findViewById(R.id.Show_carName);
                    ShowDate=findViewById(R.id.Show_lastDate);
                    CarImage=findViewById(R.id.Show_image);

                    CarImage.setScaleType(ImageView.ScaleType.CENTER_CROP);


                    ShowBrand.setText( car.Brand);
                    ShowCarName.setText( car.CarName);
                    ShowDate.setText(car.Availability);
                    Url=car.ImageUrl;

                    Picasso.get()
                            .load(Url)
                            .error(R.drawable.car_image4)
                            .into(CarImage);










                }else{

                    noviewCar();
                    finish();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(View_HostCar.this, "DataBase Error", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void updateView() {
        Intent intent=new Intent(View_HostCar.this,UpdateHost.class);
        startActivity(intent);
        finish();


    }


    private void deleteDetails(){
        FirebaseDatabase.getInstance().getReference("Hosting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(View_HostCar.this, "Deleting...", Toast.LENGTH_LONG).show();

                        if(task.isSuccessful()){
                            Toast.makeText(View_HostCar.this, "details deleted", Toast.LENGTH_SHORT).show();
                            deleteimage();
                        }else {
                            Toast.makeText(View_HostCar.this, "Failed delete details", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }

    private void deleteimage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hosting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User ker = snapshot.getValue(User.class);
                    if(ker!=null){
                        Url=ker.MatricNumber+".jpg";
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();

                        StorageReference imageRef = storageRef.child("images/" + Url);

                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Toast.makeText(View_HostCar.this, "Success Deleting your Car", Toast.LENGTH_LONG).show();
                                // Image successfully deleted
                                finish();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to delete image
                                Toast.makeText(View_HostCar.this, "Failed to delete image", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });

                    }








            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });




    }

    private void noviewCar() {
        Intent homeIntent = new Intent(this, nocar.class);
        startActivity(homeIntent);
        finish();
    }


    private void home() {
        Intent homeIntent = new Intent(this, Homepage.class);
        startActivity(homeIntent);
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