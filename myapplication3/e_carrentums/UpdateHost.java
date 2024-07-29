package com.tryapp.myapplication3.e_carrentums;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpdateHost <Private> extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int backButtonPresses = 0;
    private Spinner NewPickup_Location;
    private Spinner status_car;
    private EditText Newprice;
    private int imageCounter = 0;
   private EditText newdate;
    private ImageView preview;
    private Button select;
    private Button update;
    private Uri filePath;
    private String Url;
    private EditText NewNumberPhone;
    private String plate;
    private FirebaseStorage storage;
    private boolean updateClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_host);






        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this, login_user.class);
            startActivity(intent);
            finish();
            return;
        }

        String CarId=getIntent().getStringExtra("carId");





// If the user doesn't select a date, date3 will be equal to the current date






        //price
        Newprice = findViewById(R.id.PriceCarupdate);
        NewNumberPhone=findViewById(R.id.NumberPhoneupdate);
        update=findViewById(R.id.imageButton3);
        select=findViewById(R.id.Upload_button);
        preview=findViewById(R.id.imageView_selected);
        String carId = getIntent().getStringExtra("carId");
        showdata(carId);

        NewPickup_Location = findViewById(R.id.PickupUpdate);
        String pickup = getIntent().getStringExtra("pickup");
        List<String> pickupLocations = new ArrayList<>();
        pickupLocations.add("KKTM");
        pickupLocations.add("KKTF");
        pickupLocations.add("KKTPAR");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateHost.this, android.R.layout.simple_spinner_dropdown_item, pickupLocations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NewPickup_Location.setAdapter(adapter);
        NewPickup_Location.setSelection(adapter.getPosition(pickup));

        status_car=findViewById(R.id.update_availability_car);
        String Stats = getIntent().getStringExtra("status");
        List<String> status = new ArrayList<>();
        status.add("Available");
        status.add("Not Available");
        ArrayAdapter<String> availcar = new ArrayAdapter<>(UpdateHost.this, android.R.layout.simple_spinner_dropdown_item, status);
        availcar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_car.setAdapter(availcar);
        status_car.setSelection(availcar.getPosition(Stats));



        update.setClickable(false);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kereta = getIntent().getStringExtra("PlateNumber");
                updatecar(kereta);
                update.setClickable(true);
            }
        });




        select.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateselectimage();
        }
    });


    }



    private void updatecar(String Np) {
        String Pickup= NewPickup_Location.getSelectedItem().toString().trim();
        String price =  Newprice.getText().toString().trim();
        String newavail=status_car.getSelectedItem().toString().trim();

        String phoneNumber = NewNumberPhone.getText().toString().trim();


        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Hosting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Use a single event listener to retrieve the data once
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterate through the snapshots of the children
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Check if the plate number matches the one specified
                    if (childSnapshot.child("PlateNumber").getValue(String.class).equals(Np)) {

                        DatabaseReference avail=childSnapshot.child("Availability").getRef();
                        avail.setValue(newavail);

                        DatabaseReference pickup = childSnapshot.child("PickupCar").getRef();
                        pickup.setValue(Pickup);


                        DatabaseReference hargaBaru = childSnapshot.child("Price").getRef();
                        hargaBaru.setValue(price);


                        DatabaseReference phone = childSnapshot.child("NumberPhone").getRef();
                        phone.setValue(phoneNumber);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        if (filePath==null){
            Toast.makeText(this, "Image Unchanged", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Update detail success", Toast.LENGTH_SHORT).show();
            home();


        }else {
            String Platenumber = getIntent().getStringExtra("PlateNumber");
            uploadimage(Platenumber);
        }



    }

    private void updateselectimage() {
        if (imageCounter >= 1) {
            Toast.makeText(this, "Cannot upload more than 1 image", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        //startActivityForResult(intent, PICK_IMAGE_REQUEST,
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


    private ValueEventListener valueEventListener;

    private void uploadimage(String plateNumber) {
        // Get a reference to the Firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hosting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Add a value event listener to the reference
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterate through the snapshots of the children
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Check if the plate number matches the one specified
                    if (childSnapshot.child("PlateNumber").getValue(String.class).equals(plateNumber)) {
                        // Get the previous image name
                        String previousImageName = plateNumber+ ".jpg";

                        // Get a reference to Firebase Storage
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        // Get a reference to the image in the storage
                        StorageReference imageRef = storageRef.child("images/" + previousImageName);

                        // Get the download URL of the image
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Previous image exists
                                // Delete the previous image before uploading a new one
                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UpdateHost.this, "Delete existing picture", Toast.LENGTH_SHORT).show();

                                        // Remove the image URL from the database
                                        deleteImageUrl(plateNumber);

                                        // Remove the ValueEventListener to prevent loop
                                        reference.removeEventListener(valueEventListener);
                                        // Upload the new image
                                        upload2(plateNumber);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Failed to delete previous image
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Previous image doesn't exist
                                // Upload a new image
                                upload2(plateNumber);

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }



    private void upload2(String Flat) {
        final ProgressDialog progressDialog = new ProgressDialog(UpdateHost.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        DatabaseReference up = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/" + Flat + ".jpg");
        UploadTask uploadTask = imageRef.putFile(filePath);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Upload is " + (int) progress + "% done");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image uploaded successfully
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000); // delay for 1 second

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        String imageUrl = uri.toString();

                        // Add the download URL to the Realtime Database
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hosting")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        // Add a value event listener to the reference
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Iterate through the snapshots of the children
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    // Check if the plate number matches the one specified
                                    if (childSnapshot.child("PlateNumber").getValue(String.class).equals(Flat)) {
                                        Toast.makeText(UpdateHost.this, "Success", Toast.LENGTH_SHORT).show();
                                        // Get a reference to the child node that corresponds to the plate number
                                        DatabaseReference imageUrlRef = childSnapshot.child("ImageUrl").getRef();
                                        // Remove the value at the child node
                                        imageUrlRef.setValue(imageUrl);

                                            home();
                                    }
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                });

            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Upload failed
                progressDialog.dismiss();
                Toast.makeText(UpdateHost.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


        private void showdata( String flat) {

        String pricey=getIntent().getStringExtra("Price");
        String phoney=getIntent().getStringExtra("NumberPhone");
        String Imgurl=getIntent().getStringExtra("ImageURL");

            Newprice.setText(pricey);
            NewNumberPhone.setText(phoney);
            preview.setVisibility(View.VISIBLE);

            preview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get()
                    .load(Imgurl).into(preview);




    }



    private void deleteImageUrl(String plateNumber) {
        // Get a reference to the Firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hosting")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Add a single-shot value event listener to the reference
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Iterate through the snapshots of the children
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // Check if the plate number matches the one specified
                    if (childSnapshot.child("PlateNumber").getValue(String.class).equals(plateNumber)) {
                        // Get a reference to the child node that corresponds to the plate number
                        DatabaseReference imageUrlRef = childSnapshot.child("ImageUrl").getRef();
                        // Remove the value at the child node
                        imageUrlRef.removeValue();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void home(){
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
            Intent homeIntent = new Intent(UpdateHost.this, Host_list.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}