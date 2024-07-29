package com.tryapp.myapplication3.e_carrentums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HostMotorAdapter extends RecyclerView.Adapter<HostMotorAdapter.MyViewHolder>{
    private ArrayList<motorcycle> HostList;
    private Context context;

    public HostMotorAdapter(ArrayList<motorcycle> hostList, Context context) {
        HostList = hostList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView ShowHostproduct;
        private TextView Showplatecar;
        private TextView ShowHostName;
        private TextView ShowHostDate;
        private ImageView showHostcar;
        private String url;
        private Button deleteCar;
        private Button updateCar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Showplatecar=itemView.findViewById(R.id.hostmotor_plate);
            ShowHostName=itemView.findViewById(R.id.Hostmotor_Name);
            ShowHostDate=itemView.findViewById(R.id.Hostmotor_date);
            showHostcar=itemView.findViewById(R.id.imageView_Motorselected);
            showHostcar.setScaleType(ImageView.ScaleType.CENTER_CROP);
            updateCar=itemView.findViewById(R.id.button3motor);
            deleteCar=itemView.findViewById(R.id.button2motor);
        }
    }
    @NonNull
    @Override
    public HostMotorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.hostmotor_view,parent,false);
        return new HostMotorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HostMotorAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
