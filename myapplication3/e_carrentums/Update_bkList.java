package com.tryapp.myapplication3.e_carrentums;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

public class Update_bkList extends AppCompatActivity {

    private EditText title;
    private EditText desc;
    private ImageView pic;
    private EditText cont;
    private String url;
    private Button upload_img;
    private Uri filepath;
    private final int imageCounter = 0;
    int backButtonPresses=0;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bk_list);

        desc=findViewById(R.id.editTextTextMulti_bk);
        title=findViewById(R.id.editTextText_title);
        pic=findViewById(R.id.imageView_bk);
        cont=findViewById(R.id.editTextPhone_bk);
        upload_img = findViewById(R.id.button_bk_upload);
        Button save=findViewById(R.id.button_bk);


        String photo=getIntent().getStringExtra("gambar");
        String Nph=getIntent().getStringExtra("phone");
        String tite=getIntent().getStringExtra("tajuk");
        String description=getIntent().getStringExtra("description");
        String uid=getIntent().getStringExtra("Uid");
        String matrik=getIntent().getStringExtra("matrik");

        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateselectimage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatek(uid,matrik);

            }


        });
       showdata(photo,Nph,tite,description);
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
                    filepath =data.getData();
                    pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    pic.setImageURI(filepath);




                }
            });

    private void uploadimage(String uid,String matrik) {

        Toast.makeText(this, "deleting existing image...", Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Create a unique file name for the image
        String fileName = uid +"(item)"+".jpg";
        String fileRent ="BackYardSale"+"/";
        // Create a reference to the file location in storage
        StorageReference imageRef = storageRef.child("BackYardSale/"+fileName);
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Update_bkList.this, "uploading new updated image", Toast.LENGTH_SHORT).show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef2 = storage.getReference();
                // Create a unique file name for the image
                String fileName = uid +"(item)"+".jpg";
                // Create a reference to the file location in storage
                StorageReference image2 = storageRef2.child("BackYardSale/"+fileName);
                image2.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String image = uri.toString();
                                // Add the download URL to the Realtime Database
                                FirebaseDatabase.getInstance().getReference("Backyard_Sale")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(uid)
                                        .child("Image_backyard")
                                        .setValue(image);
                                Toast.makeText(Update_bkList.this, "Success", Toast.LENGTH_SHORT).show();





                            } });
                    }
                });

            }
        });


    }

    private void updatek(String uid, String matrik) {
        String des=desc.getText().toString().trim();
        String tite=title.getText().toString().trim();
        String Contact=cont.getText().toString().trim();



        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Backyard_Sale")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                snapshot.child("contact").getRef().setValue(Contact);
                snapshot.child("title").getRef().setValue(tite);
                snapshot.child("description").getRef().setValue(des).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Update_bkList.this, "Details Updated", Toast.LENGTH_SHORT).show();
                        if (filepath==null){
                            Toast.makeText(Update_bkList.this, "No updated image", Toast.LENGTH_SHORT).show();
                        }else {
                            uploadimage(uid,matrik);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showdata(String phot,String phon,String tjuk, String descri) {

        pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(phot).error(R.drawable.indicator_drawable).into(pic);
        cont.setText(phon);
        title.setText(tjuk);
        desc.setText(descri);


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
            Intent homeIntent = new Intent(this, BackYard_list.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }
}