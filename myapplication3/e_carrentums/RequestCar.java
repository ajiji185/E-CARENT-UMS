package com.tryapp.myapplication3.e_carrentums;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RequestCar extends AppCompatActivity {
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
    public String startDate;
    private String endDate;
    private String UrlForIc;
    private String UrlForLc;
    FirebaseStorage storage;
    private Long selectedStartDate = null;
    private Long selectedEndDate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_car);

        String getmatrikow = getIntent().getStringExtra("owner Matric");
        String getplatow = getIntent().getStringExtra("Owner Plat");
        String getNpow = getIntent().getStringExtra("Owner Phone");


        owner=findViewById(R.id.Request_OwnerNumberMatric);
        Customer=findViewById(R.id.Request_Name);
        CustomerMatrik=findViewById(R.id.Request_MatricNumber);
        ic=findViewById(R.id.imageView_IC);
        Licence=findViewById(R.id.imageView_Licence);
        UpIc=findViewById(R.id.Upload_IC);
        UpLesen=findViewById(R.id.Upload_Licence);
        Save=findViewById(R.id.send_req);
        confirm=findViewById(R.id.radioButton_Confrimation);
        Customer_number=findViewById(R.id.et_CustomerNumberphone);
        calendar1=findViewById(R.id.calendarView);
        calendar2=findViewById(R.id.calendarView2);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


       calendarchecking();









// If the user doesn't select a date, date3 will be equal to the current date
        if (startDate == null) {
            startDate = String.format("%02d/%02d/%04d", day, month+1, year);

        }
        if (endDate == null) {
            endDate = String.format("%02d/%02d/%04d", day, month+1, year);

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
                    Toast.makeText(RequestCar.this, "requesting..", Toast.LENGTH_SHORT).show();


                    // The radio button is checked
                    registerRenter(getmatrikow,getplatow,getNpow);
                } else {
                    Toast.makeText(RequestCar.this, "Please confirm your Document are True", Toast.LENGTH_SHORT).show();
                    // The radio button is not checked

                    confirm.requestFocus();
                    confirm.setTextColor(Color.RED);
                    return;
                }

            }
        });



    }

    private void calendarchecking() {
        String check = getIntent().getStringExtra("Owner Plat");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Renting");
        Query query = ref.orderByChild("owCar").equalTo(check);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Keep track of all rental periods for the car
                    List<Pair<Long, Long>> rentalPeriods = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String startDateStr = childSnapshot.child("startDate").getValue(String.class);
                        String endDateStr = childSnapshot.child("endDate").getValue(String.class);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar startCalendar = Calendar.getInstance();
                        Calendar endCalendar = Calendar.getInstance();

                        try {
                            startCalendar.setTime(format.parse(startDateStr));
                            endCalendar.setTime(format.parse(endDateStr));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long startDateLong = startCalendar.getTimeInMillis();
                        long endDateLong = endCalendar.getTimeInMillis();
                        // Get the start and end timestamps

                        final long oneDayMillis = 24 * 60 * 60 * 1000;
                        final long rangeMillis = startDateLong - endDateLong;

                        rentalPeriods.add(new Pair<>(startDateLong, endDateLong));
                        rentalPeriods.add(new Pair<>(startDateLong, endDateLong));
                    }

                    // Disable and change style of dates within the range of any rental period
                    final int disabledStyle = R.style.My_Calendar_Disabled;
                    final long todayMillis = System.currentTimeMillis();

                    // Set the minimum date to today
                    calendar1.setMinDate(todayMillis);
                    calendar2.setMinDate(todayMillis);

                    // Set the maximum date to 2 month from today
                    long maxDateMillis = todayMillis + 60 * 24 * 60 * 60 * 1000L;
                    calendar1.setMaxDate(maxDateMillis);
                    calendar2.setMaxDate(maxDateMillis);

                    for (long i = todayMillis; i <= maxDateMillis; i += 86400000) {
                        boolean dateDisabled = false;
                        for (Pair<Long, Long> rentalPeriod : rentalPeriods) {
                            if (i >= rentalPeriod.first && i <= rentalPeriod.second) {
                                dateDisabled = true;
                                break;
                            }
                        }

                        if (dateDisabled) {
                            // Set the date for both calendars to the nearest free date
                            calendar1.setDate(i + 86400000);
                            calendar2.setDate(i + 86400000);
                        }
                    }


                    // Make the range of any rental period unclickable in the CalendarView
                    // Save original start and end dates
                    String originalStartDate = startDate;
                    String originalEndDate = endDate;




                    // Set the onDateChangeListener for the calendars
                    calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            Calendar selectedCalendar = Calendar.getInstance();
                            selectedCalendar.set(year, month, dayOfMonth);
                            long selectedMillis = selectedCalendar.getTimeInMillis();

                            boolean dateDisabled = false;
                            for (Pair<Long, Long> rentalPeriod : rentalPeriods) {
                                if (selectedMillis >= rentalPeriod.first && selectedMillis <= rentalPeriod.second) {
                                    dateDisabled = true;
                                    break;
                                }
                            }

                            if (dateDisabled) {
                                if (selectedStartDate != null) {
                                    // Set the calendar back to the previously selected start date
                                    calendar1.setDate(selectedStartDate);
                                }
                                Toast toast = Toast.makeText(RequestCar.this, "This date is not available", Toast.LENGTH_SHORT);
                                toast.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 500);

                            } else {
                                selectedStartDate = selectedMillis;
                                startDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                            }
                        }
                    });

                    calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            Calendar selectedCalendar = Calendar.getInstance();
                            selectedCalendar.set(year, month, dayOfMonth);
                            long selectedMillis = selectedCalendar.getTimeInMillis();

                            boolean dateDisabled = false;
                            for (Pair<Long, Long> rentalPeriod : rentalPeriods) {
                                if (selectedMillis >= rentalPeriod.first && selectedMillis <= rentalPeriod.second) {
                                    dateDisabled = true;
                                    break;
                                }
                            }

                            if (dateDisabled) {
                                if (selectedEndDate != null) {
                                    // Set the calendar back to the previously selected end date
                                    calendar2.setDate(selectedEndDate);
                                }
                                Toast toast = Toast.makeText(RequestCar.this, "This date is not available", Toast.LENGTH_SHORT);
                                toast.show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 500);

                            } else {
                                selectedEndDate = selectedMillis;
                                endDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                            }
                        }
                    });


                } else {
                    Toast.makeText(RequestCar.this, "No Renting for this car yet", Toast.LENGTH_SHORT).show();
                    final long todayMillis = System.currentTimeMillis();

                    // Set the minimum date to today
                    calendar1.setMinDate(todayMillis);
                    calendar2.setMinDate(todayMillis);

                    // Set the maximum date to 2 month from today
                    long maxDateMillis = todayMillis + 60 * 24 * 60 * 60 * 1000L;
                    calendar1.setMaxDate(maxDateMillis);
                    calendar2.setMaxDate(maxDateMillis);

                    calendar1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            startDate= String.format("%02d/%02d/%04d", dayOfMonth, month+1, year);
                        }
                    });
                    calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            endDate= String.format("%02d/%02d/%04d", dayOfMonth, month+1, year);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
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
                RentingDetails rent=new RentingDetails(Hostid,userid,current_user,getmatrik, owNp,CarPlate, CustMat, CustName, cusNp,Imageic , ImageLc, Status,startrent,endrent);
                Toast.makeText(RequestCar.this, "Uploading...", Toast.LENGTH_SHORT).show();
                FirebaseAuth auth= FirebaseAuth.getInstance();
                FirebaseUser currentUser= auth.getCurrentUser();
                FirebaseDatabase database= FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Renting").child(currentUser.getUid());
                reference.setValue(rent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RequestCar.this, "Upload Details Success", Toast.LENGTH_SHORT).show();

                        viewReq();




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
        StorageReference imageRef = storageRef.child("Renting/" + fileName);
        // Upload the file to storage
        imageRef.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrlIC = uri.toString();
                        String RentID = FirebaseDatabase.getInstance().getReference("Renting").getKey();
                        // Add the download URL to the Realtime Database
                        FirebaseDatabase.getInstance().getReference("Renting")
                                .child(Uid)
                                .child("ICImageUrl")
                                .setValue(imageUrlIC);
                    }
                });


                Toast.makeText(RequestCar.this, "IC Uploaded", Toast.LENGTH_SHORT).show();
                StorageReference storageRef = storage.getReference();
                // Create a unique file name for the image
                String fileName = RenterMatric +"(Licence)"+".jpg";

                // Create a reference to the file location in storage
                StorageReference imageRef2 = storageRef.child("Renting/" +fileName);

                // Upload the file to storage
                imageRef2.putFile(filePath2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        imageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {

                                String imageUrlLC = uri.toString();
                                String RentID = FirebaseDatabase.getInstance().getReference("Renting").getKey();
                                // Add the download URL to the Realtime Database
                                FirebaseDatabase.getInstance().getReference("Renting")
                                        .child(Uid)
                                        .child("licenseUrl")
                                        .setValue(imageUrlLC);
                                Toast.makeText(RequestCar.this, "Licence uploaded", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });

            }
        });
    }

    private void viewReq() {

        Intent intent=new Intent(this,View_rentingReq.class);
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
                    Toast.makeText(RequestCar.this, "getting matric", Toast.LENGTH_SHORT).show();
                    String getmatrik = getIntent().getStringExtra("owner Matric");
                    owner.setText(getmatrik);
                    Customer.setText(user.FullName);
                    CustomerMatrik.setText(user.MatricNumber);
                }else{
                    Toast.makeText(RequestCar.this, "No Data Found in Database", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              finish();

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
            Intent homeIntent = new Intent(RequestCar.this, Homepage.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }

}