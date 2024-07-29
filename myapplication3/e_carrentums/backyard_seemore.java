package com.tryapp.myapplication3.e_carrentums;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class backyard_seemore extends AppCompatActivity {

    private ImageView itemimg;
    private TextView itDesc;
    private TextView itContact;
    private int backButtonPresses=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backyard_seemore);

        itemimg=findViewById(R.id.imageView34);
        itDesc=findViewById(R.id.textView64);
        itContact=findViewById(R.id.textView67);

        Showdata();

    }

    private void Showdata() {

        String descr=getIntent().getStringExtra("description");
        String contact=getIntent().getStringExtra("phone");
        String Imageit=getIntent().getStringExtra("gambar");
        itContact.setText(contact);
        itDesc.setText(descr);
        itemimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(Imageit).error(R.drawable.indicator_drawable).into(itemimg);

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
            Intent homeIntent = new Intent(this, Backyard_itemview.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }
}