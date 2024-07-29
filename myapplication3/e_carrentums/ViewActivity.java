package com.tryapp.myapplication3.e_carrentums;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    private TextView ShowBrand;
    private TextView ShowCarName;
    private TextView ShowDate;
    private TextView ShowPlace;
    private TextView ShowPrice;
    private String Url;
    private ImageView CarImage;
    private Button request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ShowBrand=findViewById(R.id.Rent_Brand);
        ShowCarName=findViewById(R.id.Rent_name);
        ShowDate=findViewById(R.id.Rent_date);
        ShowPlace=findViewById(R.id.Rent_place);
        ShowPrice=findViewById(R.id.Rent_Price);
        CarImage=findViewById(R.id.RentImage);
        request= findViewById(R.id.Request_rent);

    }
}