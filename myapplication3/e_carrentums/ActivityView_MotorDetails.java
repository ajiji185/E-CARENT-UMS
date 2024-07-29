package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityView_MotorDetails extends AppCompatActivity {
    private String imageurl;
    int backButtonPresses=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_motor_details);


        String getnama = getIntent().getStringExtra("Nama");
        String gettarikh = getIntent().getStringExtra("Tarikh");
        String getimg = getIntent().getStringExtra("ImageURL");
        String getplat = getIntent().getStringExtra("PlateNumber");
        String getNp = getIntent().getStringExtra("NumberPhone");
        String getcost = getIntent().getStringExtra("Price");
        String getoil = getIntent().getStringExtra("Minyak");
        String getjenisSeats = getIntent().getStringExtra("seatType");
        String getplaceTempat = getIntent().getStringExtra("pickupPlace");
        String getownerRef = getIntent().getStringExtra("ownerMatric");
        String getGiar=getIntent().getStringExtra("gearType");
        String userid=getIntent().getStringExtra("userid");
        String hostid=getIntent().getStringExtra("hostid");


        TextView namakereta = findViewById(R.id.textView26motor);
        TextView nomborFlat = findViewById(R.id.textView28motor);
        TextView tarikh = findViewById(R.id.textView30motor);
        TextView gear = findViewById(R.id.textView33motor);
        TextView tempat = findViewById(R.id.textView38motor);
        TextView harga = findViewById(R.id.textView39motor);
        ImageView preview = findViewById(R.id.imageView28motor);
        preview.setScaleType(ImageView.ScaleType.CENTER_CROP);


        namakereta.setText(getnama);
        nomborFlat.setText(getplat);
        tarikh.setText(gettarikh);
        gear.setText(getGiar);
        tempat.setText(getplaceTempat);
        harga.setText(getcost);

        Picasso.get()
                .load(getimg)
                .error(R.drawable.car_image4)
                .into(preview);

        Button getReq = findViewById(R.id.button5motor);
        getReq.setOnClickListener(new View.OnClickListener() {



            final String selectedCarId = getIntent().getStringExtra("PlateNumber");
            ValueEventListener ownCarListener; // Declare a variable to store the listener
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityView_MotorDetails.this);
                builder.setTitle("Requesting Motorcyle");
                builder.setMessage("Are you sure want to Request this motorcycle?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goToReqDet(getownerRef, getplat, getNp,userid,hostid);
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

    private void goToReqDet(String matric, String platnom,String Phone,String userid,String hostid) {
        // create an Intent
        Intent updateIntent = new Intent(ActivityView_MotorDetails.this, RequestCar.class);
        // add the data as extras to the Intent
        updateIntent.putExtra("owner Matric", matric);
        updateIntent.putExtra("Owner Plat", platnom);
        updateIntent.putExtra("Owner Phone", Phone);
        updateIntent.putExtra("userid", userid);
        updateIntent.putExtra("hostid", hostid);



        // start the update activity
        startActivity(updateIntent);
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
            Intent homeIntent = new Intent(this, RentActivity_motor.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}