package com.tryapp.myapplication3.e_carrentums;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class register_motorcycle extends AppCompatActivity {

    private Spinner Pickup_Location;
    private Spinner Availability;
    private EditText price;
    private EditText MotorName;
    private Spinner MotorTransmission;
    private EditText MotorMatric;
    private EditText MotorPlate;
    private EditText NumberPhone;
    private String date3;
    private CalendarView date2;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private int backButtonPresses = 0;
    private ImageView preview;
    private Button select;
    private Uri filePath;
    private int imageCounter = 0;
    private String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_motorcycle);



        mAuth= FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        //back button
        ImageButton back=findViewById(R.id.Back_fromHo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home();

            }
        });





        //save button
        Button signup =findViewById(R.id.imageButton3Motor);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                registerCar();



            }
        });

        //select image button

        select = findViewById(R.id.UploadMotor_button);
        select.setOnClickListener(v -> {



            selectImage();


        });



        //transmission
        MotorTransmission=findViewById(R.id.TransmissionMotor);
        ArrayAdapter<CharSequence>transmission=ArrayAdapter.createFromResource(this,R.array.Transmission, android.R.layout.simple_spinner_dropdown_item);
        transmission.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MotorTransmission.setAdapter(transmission);
        MotorTransmission.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        //Pickup
        Pickup_Location=findViewById(R.id.PickupMotor);
        ArrayAdapter<CharSequence>pickup_l=ArrayAdapter.createFromResource(this,R.array.Pickup, android.R.layout.simple_spinner_dropdown_item);
        pickup_l.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Pickup_Location.setAdapter(pickup_l);
        Pickup_Location.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        //price
        price = findViewById(R.id.PriceMotor);

        //availability

        Availability=findViewById(R.id.Availability_Motor_spin);
        ArrayAdapter<CharSequence>available=ArrayAdapter.createFromResource(this,R.array.Availability, android.R.layout.simple_spinner_dropdown_item);
        available.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Availability.setAdapter(available);
        Availability.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);



        NumberPhone=findViewById(R.id.NumberPhoneMotor);

        preview=findViewById(R.id.imageView_Motorselected);
        MotorName=findViewById(R.id.MotorName);
        MotorPlate=findViewById(R.id.motorPlateNumber);




    }




    //back Button
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

    //register class
    private void registerCar() {


        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                assert user != null;
                String etMatricCar=user.MatricNumber;
                String Image= Url;


                String etTransmission = MotorTransmission.getSelectedItem().toString().trim();
                String etPickup= Pickup_Location.getSelectedItem().toString().trim();
                String etAvailability= Availability.getSelectedItem().toString().trim();
                String etPrice = price.getText().toString().trim();
                String etDate= date3;
                String etNumberPhone=NumberPhone.getText().toString().trim();
                String etCarName=MotorName.getText().toString().trim();
                String etPlate=MotorPlate.getText().toString().trim();



                if(etPlate.isEmpty()){
                    MotorPlate.setError("Provide Car plate Number");
                    MotorPlate.requestFocus();
                    return;
                }

                if(etCarName.isEmpty()){
                    MotorName.setError("Provide Car Name");
                    MotorName.requestFocus();
                    return;
                }

                if ( etPrice.isEmpty()){
                    price.setError("Provide Rental Pricing");
                    price.requestFocus();

                    return;
                }

                String newPrice="RM "+etPrice+" per day";







                if (etNumberPhone.isEmpty()){
                    NumberPhone.setError("Please provide phone Number");
                    NumberPhone.requestFocus();
                    return;
                }


                if (filePath==null){
                    NullOnFilePath();

                }


                String carId = FirebaseDatabase.getInstance().getReference("Hosting motor").push().getKey();
                motor car = new motor(userId, carId,etCarName,etTransmission,newPrice,etAvailability,etMatricCar,etPickup,etNumberPhone,Image,etPlate);
                FirebaseDatabase.getInstance().getReference("Hosting motor")
                        .child(userId)
                        .child(carId)
                        .setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    uploadImage(carId);




                                } else {
                                    Toast.makeText(register_motorcycle.this, "Registration Have Failed", Toast.LENGTH_LONG).show();
                                    return;

                                }


                            }
                        });







            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(register_motorcycle.this, "Error on Database", Toast.LENGTH_SHORT).show();

            }
        });



    }










    // Select image from device

    private void selectImage() {
        if (imageCounter >= 1) {
            Toast.makeText(this, "Cannot upload more than 1 image", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){

                @Override
                public void onActivityResult(ActivityResult  activityResult ) {
                    if (activityResult.getResultCode() == Activity.RESULT_CANCELED) {
                        return;
                    }
                    Intent data=activityResult.getData();
                    filePath =data.getData();
                    preview.setVisibility(View.VISIBLE);
                    preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    preview.setImageURI(filePath);



                }
            });





    // Upload image to Firebase
    private void uploadImage(final String carModel) {
        String etcarplate = MotorPlate.getText().toString().trim();
        final DatabaseReference imageUrlRef = FirebaseDatabase.getInstance().getReference("Hosting motor")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageUrlRef.orderByChild("ImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User has previously uploaded at least one car
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (snapshot.exists()) {
                                Toast.makeText(register_motorcycle.this, "Uploading...", Toast.LENGTH_LONG).show();
                                String matricNumber = user.MatricNumber;

                                // Get a reference to the storage location
                                StorageReference storageRef = storage.getReference();
                                // Create a unique file name for the image
                                String fileName = etcarplate + ".jpg";
                                // Create a reference to the file location in storage
                                StorageReference imageRef = storageRef.child("images/" + fileName);
                                // Upload the file to storage
                                imageRef.putFile(filePath)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get the download URL of the image
                                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Toast.makeText(register_motorcycle.this, "Register Hosting Success", Toast.LENGTH_LONG).show();

                                                        // Get the download URL
                                                        String imageUrl = uri.toString();

                                                        // Add the download URL to the Realtime Database
                                                        FirebaseDatabase.getInstance().getReference("Hosting motor")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .child(carModel)
                                                                .child("ImageUrl")
                                                                .setValue(imageUrl);
                                                        home();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(register_motorcycle.this, "Error", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                });
                                            }
                                        });
                            }else {
                                // Display an error message to the user
                                errorhandler2();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error

                        }
                    });





                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // User has uploaded an image
                errorhandler();
            }

        });
    }

    private void home() {
        Intent intent=new Intent(this,Homepage.class);
        startActivity(intent);
        finish();
    }
    private void NullOnFilePath(){
        Toast.makeText(this, "Select Car Image", Toast.LENGTH_SHORT).show();
        return;
    }
    private void errorhandler(){
        Toast.makeText(this,"Already upload image",Toast.LENGTH_LONG).show();
        return;
    }
    private void errorhandler2(){
        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(this,Homepage.class);
        startActivity(intent);
        finish();
    }

    private void error(){
        Toast.makeText(this,"no Uploaded Photo",Toast.LENGTH_LONG).show();
    }



}