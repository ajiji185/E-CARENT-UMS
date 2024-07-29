package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class Homepage extends AppCompatActivity {


private ImageButton CarList;
private ImageButton rentListactivity;
private ImageButton viewRent;
private ImageButton reqdet;
private ImageButton custlistl;

private ImageButton additem;
private ImageButton itemList;
private ImageButton itemall;

private ImageButton gHost;
private ImageButton chathost;

private Switch role;
private TextView roles;

    private TextView textv;

    private  TextView teset;
    private TextView text2;
    private int backButtonPresses = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         roles= findViewById(R.id.textView69);
        textv=findViewById(R.id.textView14);
        teset=findViewById(R.id.textView15);
        text2=findViewById(R.id.custList);


        role= findViewById(R.id.switch1);



        itemall=findViewById(R.id.imageButton2);
        itemall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Homepage.this,Backyard_itemview.class);
                startActivity(intent);
                finish();
            }
        });

        itemList=findViewById(R.id.imageButton_item_list);
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Homepage.this,BackYard_list.class);
                startActivity(intent);
                finish();
            }
        });

        additem=findViewById(R.id.backyardSale_img_btn);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this,add_item_BackYard.class);
                startActivity(intent);
                finish();
            }
        });

        custlistl=findViewById(R.id.imageButton_cust_list);
        custlistl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this,customer_list.class);
                startActivity(intent);
                finish();
            }
        });

        reqdet=findViewById(R.id.booknow_img_btn);
        reqdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this, View_rentingReq.class);
                startActivity(intent);
                finish();
            }
        });

        rentListactivity=findViewById(R.id.imageButton_searchRect);
        rentListactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this, SearchView.class);
                startActivity(intent);
                finish();
            }
        });

        viewRent=findViewById(R.id.imageButton4);
        viewRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycle();
            }
        });

        ImageButton profile= findViewById(R.id.Profile_image_btn);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile();
            }
        });

       gHost =findViewById(R.id.Host_img_btn);
        gHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registercar();

            }
        });

        CarList=findViewById(R.id.imageButton_CarList);
        CarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                            viewcar();



            }
        });

        List<Integer> imageList = Arrays.asList(R.drawable.car_image3,R.drawable.car_image4,R.drawable.car_image5);

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImageSliderAdapter(this, imageList));
        viewPager.setCurrentItem(1);
        final int[] currentPage = {1};
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage[0] == imageList.size()) {
                    currentPage[0] = 0;
                }
                viewPager.setCurrentItem(currentPage[0]++, true);
            }
        };
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3000);

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isChecked=role.isChecked();
                if(isChecked){


                    roles.setText("You now as a Host");
                    //hide all renters
                    viewRent.setVisibility(View.GONE);
                    textv.setVisibility(View.GONE);
                    reqdet.setVisibility(View.GONE);
                    //show all host
                    CarList.setVisibility(View.VISIBLE);
                    teset.setVisibility(View.VISIBLE);
                    gHost.setVisibility(View.VISIBLE);
                    custlistl.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);



                }else{
                    roles.setText("You now as a Renter");
                    //hide all host btn
                    CarList.setVisibility(View.GONE);
                    teset.setVisibility(View.GONE);
                    gHost.setVisibility(View.GONE);
                    custlistl.setVisibility(View.GONE);
                    text2.setVisibility(View.GONE);
                    //view all renters
                    viewRent.setVisibility(View.VISIBLE);
                    textv.setVisibility(View.VISIBLE);
                    reqdet.setVisibility(View.VISIBLE);

                }
            }
        });





    }

    private void recycle() {


        Intent intent = new Intent(Homepage.this, Rent_activity.class);
        startActivity(intent);
        finish();


    }


    private void viewcar() {
        Intent intent=new Intent(Homepage.this, Host_list.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backButtonPresses++;
        handleBackButtonPress();

    }


    private void handleBackButtonPress() {
        if (backButtonPresses == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit E-Carent");
            //alert dialog
            builder.setMessage("Are you sure want to exit?")
                    .setCancelable(true)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();

                        }
                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
        }else
        {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
           if(backButtonPresses==2) {
               finish();}
        }


    }


    private void registercar() {

        Intent intent = new Intent(this, register_car.class);
        startActivity(intent);
        finish();
    }

    private void profile() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }



}