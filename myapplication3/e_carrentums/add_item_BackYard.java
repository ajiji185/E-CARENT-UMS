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

import java.util.Objects;

public class add_item_BackYard extends AppCompatActivity {
    private EditText title;
    private EditText desc;
    private ImageView pic;
    private EditText cont;
    private String url;
    private Button upload_img;
    private Uri filepath;
    private Button additem;
    private int imageCounter = 0;
    int backButtonPresses=0;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_back_yard);

        title=findViewById(R.id.editTextTextPersonName);
        desc=findViewById(R.id.editTextTextMultiLine2);
        pic=findViewById(R.id.imageView31);
        cont=findViewById(R.id.editTextPhone);
        upload_img=findViewById(R.id.button6);
        additem=findViewById(R.id.button9);




        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg();
            }
        });

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            User user=snapshot.getValue(User.class);
                            String matricbk=user.MatricNumber;
                            additem_back(matricbk);

                        }else{
                            Toast.makeText(add_item_BackYard.this, "user not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });



    }

    private void additem_back(String mat) {


        String np=cont.getText().toString().trim();
        String descr=desc.getText().toString().trim();
        String tite=title.getText().toString().trim();

        if(np.isEmpty()){
            cont.setError("provide Contact Number");
            cont.requestFocus();
            return;
        }
        if(tite.isEmpty()){
            title.setError("provide Title");
            title.requestFocus();
            return;
        }


        if (descr.isEmpty()){
            desc.setError("provide some description");
            desc.requestFocus();
            return;
        }

        if(filepath==null){
            Toast.makeText(this, "provide item image", Toast.LENGTH_SHORT).show();
            return;
        }


        String backyardID = FirebaseDatabase.getInstance().getReference("Backyard_Sale").push().getKey();
        BackYard_details bk=new BackYard_details(backyardID,np,mat,url,tite,descr);

        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("Backyard_Sale");
                reference2.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(backyardID).setValue(bk).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(add_item_BackYard.this, "Uploading image..", Toast.LENGTH_SHORT).show();
                                uploadimg_bk(mat,backyardID);
                            }
                        });

    }

    private void uploadimg_bk(String matrik,String uid) {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User us= snapshot.getValue(User.class);
                    String matr=uid;
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    // Create a unique file name for the image
                    String fileName = matr +"(item)"+".jpg";
                    String fileRent ="BackYardSale"+"/";
                    // Create a reference to the file location in storage
                    StorageReference imageRef = storageRef.child("BackYardSale/"+fileName);
                    // Upload the file to storage
                    imageRef.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                    Toast.makeText(add_item_BackYard.this, "add item success", Toast.LENGTH_SHORT).show();
                                    home();
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

    private void home() {

        Intent intent=new Intent(this, Homepage.class);
        startActivity(intent);
        finish();
    }

    private void selectImg() {


        if (imageCounter >= 2) {
                Toast.makeText(this, "Cannot upload more than 1 image", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");

            secondActivityResultLauncher.launch(intent);
        }

    ActivityResultLauncher<Intent> secondActivityResultLauncher = registerForActivityResult(
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