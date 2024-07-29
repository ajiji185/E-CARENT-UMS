package com.tryapp.myapplication3.e_carrentums;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class request_motor extends AppCompatActivity {
    int backButtonPresses=0;
    private ImageView ic;
    private ImageView Licence;
    private Button UpIc;
    private Button UpLesen;
    private TextView owner;
    private TextView Customer;
    private TextView CustomerMatrik;
    private CalendarView calendar1;
    private CalendarView calendar2;
    private Button Save;
    private RadioButton confirm;
    private Uri filePath;
    private Uri filePath2;
    private int imageCounter = 0;
    private EditText Customer_number;
    private String startDate;
    private String endDate;
    private String UrlForIc;
    private String UrlForLc;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_motor);

        String getmatrikow = getIntent().getStringExtra("owner Matric");
        String getplatow = getIntent().getStringExtra("Owner Plat");
        String getNpow = getIntent().getStringExtra("Owner Phone");


        owner=findViewById(R.id.Request_OwnerNumberMatricmotor);
        Customer=findViewById(R.id.Request_Namemotor);
        CustomerMatrik=findViewById(R.id.Request_MatricNumbermotor);
        ic=findViewById(R.id.imageView_ICmotor);
        Licence=findViewById(R.id.imageView_Licencemotor);
        UpIc=findViewById(R.id.Upload_ICmotor);
        UpLesen=findViewById(R.id.Upload_Licencemotor);
        Save=findViewById(R.id.send_reqmotor);
        confirm=findViewById(R.id.radioButton_Confrimationmotor);
        Customer_number=findViewById(R.id.et_CustomerNumberphonemotor);
        calendar1=findViewById(R.id.calendarViewmotor);
        calendar2=findViewById(R.id.calendarView2motor);



        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Store the selected date in a variable
                startDate = String.format("%02d/%02d/%04d", dayOfMonth, month+1, year);
            }
        });
        calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Store the selected date in a variable
                endDate = String.format("%02d/%02d/%04d", dayOfMonth, month+1, year);
            }
        });



// If the user doesn't select a date, date3 will be equal to the current date
        if (startDate == null) {
            startDate = String.format("%02d/%02d/%04d", day, month+1, year);
            calendar1.setDate(calendar.getTimeInMillis(), false, true);
        }
        if (endDate == null) {
            endDate = String.format("%02d/%02d/%04d", day, month+1, year);
            calendar2.setDate(calendar.getTimeInMillis(), false, true);
        }




        showdata(getmatrikow,getplatow,getNpow);

        //ic image
        UpIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        UpLesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selsectImage2();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = confirm.isChecked();
                if (isChecked) {
                    Toast.makeText(request_motor.this, "requesting..", Toast.LENGTH_SHORT).show();


                    // The radio button is checked
                    registerRenter(getmatrikow,getplatow,getNpow);
                } else {
                    Toast.makeText(request_motor.this, "Please confirm your Document are True", Toast.LENGTH_SHORT).show();
                    // The radio button is not checked

                    confirm.requestFocus();
                    confirm.setTextColor(Color.RED);
                    return;
                }

            }
        });


    }

    private void registerRenter(String owMat,String CarPlate,String owNp) {

        String cusNp=Customer_number.getText().toString().trim();

        if(cusNp.isEmpty()){
            Customer_number.setError("Provide Phone Number");
            Customer_number.requestFocus();
            return;
        }

        if(filePath==null){
            Toast.makeText(this, "Provide IC ", Toast.LENGTH_SHORT).show();
            return;
        }

        if(filePath2==null){
            Toast.makeText(this, "Provide Licence", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        String UID=currentUser.getUid();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(UID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                User user=snapshot.getValue(User.class);
                String getmatrik = getIntent().getStringExtra("owner Matric");
                String Hostid = getIntent().getStringExtra("hostid");
                String userid = getIntent().getStringExtra("userid");
                String current_user= FirebaseAuth.getInstance().getCurrentUser().getUid();


                String CustMat=user.MatricNumber;
                String CustName=user.FullName;
                String Imageic= UrlForIc;
                String ImageLc=UrlForLc;
                String Status="Requesting";
                String startrent= startDate;
                String endrent=endDate;
                RentingDetailsMotor rent=new RentingDetailsMotor(Hostid,userid,current_user,getmatrik, owNp,CarPlate, CustMat, CustName, cusNp,Imageic , ImageLc, Status,startrent,endrent);
                Toast.makeText(request_motor.this, "Uploading...", Toast.LENGTH_SHORT).show();
                FirebaseAuth auth= FirebaseAuth.getInstance();
                FirebaseUser currentUser= auth.getCurrentUser();
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Renting motor").child(currentUser.getUid());
                reference.setValue(rent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(request_motor.this, "Upload Details Success", Toast.LENGTH_SHORT).show();

                        changeStatusHost();
                        UploadImage(CustMat,CarPlate,UID);




                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }

    private void UploadImage(String RenterMatric, String ownercar,String Uid) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Toast.makeText(this, "Uploading Ic...", Toast.LENGTH_SHORT).show();

        // Get a reference to the storage location
        StorageReference storageRef = storage.getReference();
        // Create a unique file name for the image
        String fileName = RenterMatric +"(IC)"+".jpg";
        String fileRent =ownercar+"/";
        // Create a reference to the file location in storage
        StorageReference imageRef = storageRef.child("Renting motor/" + fileName);
        // Upload the file to storage
        imageRef.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrlIC = uri.toString();
                        String RentID = FirebaseDatabase.getInstance().getReference("Renting motor").getKey();
                        // Add the download URL to the Realtime Database
                        FirebaseDatabase.getInstance().getReference("Renting motor")
                                .child(Uid)
                                .child("ICImageUrl")
                                .setValue(imageUrlIC);
                    }
                });


                Toast.makeText(request_motor.this, "IC Uploaded", Toast.LENGTH_SHORT).show();
                StorageReference storageRef = storage.getReference();
                // Create a unique file name for the image
                String fileName = RenterMatric +"(Licence)"+".jpg";

                // Create a reference to the file location in storage
                StorageReference imageRef2 = storageRef.child("Renting motor/" +fileName);

                // Upload the file to storage
                imageRef2.putFile(filePath2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        imageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {

                                String imageUrlLC = uri.toString();
                                String RentID = FirebaseDatabase.getInstance().getReference("Renting motor").getKey();
                                // Add the download URL to the Realtime Database
                                FirebaseDatabase.getInstance().getReference("Renting motor")
                                        .child(Uid)
                                        .child("licenseUrl")
                                        .setValue(imageUrlLC);
                                Toast.makeText(request_motor.this, "Licence uploaded", Toast.LENGTH_SHORT).show();
                                viewReq();
                            }
                        });


                    }
                });

            }
        });
    }

    private void viewReq() {

        Intent intent=new Intent(this,view_rentingReqMotor.class);
        startActivity(intent);
        finish();


    }

    private void selsectImage2() {
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
                    filePath2 =data.getData();
                    Licence.setVisibility(View.VISIBLE);
                    Licence.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Licence.setImageURI(filePath2);



                }
            });

    private void selectImage() {
        if (imageCounter >= 2) {
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
                    ic.setVisibility(View.VISIBLE);
                    ic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ic.setImageURI(filePath);



                }
            });

    private void showdata(String getowMat, String getplatow, String getNpow) {


        Toast.makeText(this, "Showing data", Toast.LENGTH_SHORT).show();
        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    User user=snapshot.getValue(User.class);
                    Toast.makeText(request_motor.this, "getting matric", Toast.LENGTH_SHORT).show();
                    String getmatrik = getIntent().getStringExtra("owner Matric");
                    owner.setText(getmatrik);
                    Customer.setText(user.FullName);
                    CustomerMatrik.setText(user.MatricNumber);
                }else{
                    Toast.makeText(request_motor.this, "No Data Found in Database", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finish();

            }
        });



    }

    private void changeStatusHost() {
        String Hostid = getIntent().getStringExtra("hostid");
        String userid = getIntent().getStringExtra("userid");
        if (userid == null || Hostid == null) {
            Log.e("changeStatusHost", "userid or Hostid is null");
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hosting motor")
                .child(userid).child(Hostid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("Availability").getRef().setValue("Requested");
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
            Intent homeIntent = new Intent(request_motor.this, Homepage.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }
}