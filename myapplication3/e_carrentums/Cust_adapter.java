package com.tryapp.myapplication3.e_carrentums;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Cust_adapter extends RecyclerView.Adapter<Cust_adapter.MyviewHolder> {
    private ArrayList<RentingDetails> CustList;
    private Context context;

    public Cust_adapter(ArrayList<RentingDetails> custList, Context context) {
        CustList = custList;
        this.context = context;
    }
    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView cust_name;
        private TextView cust_matrikNum;
        private TextView cust_phone;
        private TextView cust_status;
        private TextView cust_carReq;
        private ImageView cust_ic;
        private Button reject;
        private Button accept;
        private Button Manage;


        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            cust_name= itemView.findViewById(R.id.textView_name_smallcust);
            cust_matrikNum=itemView.findViewById(R.id.textView_numsm_cust);
            cust_phone=itemView.findViewById(R.id.num_small_cust);
            Manage=itemView.findViewById(R.id.seemore_btn);
            cust_ic=itemView.findViewById(R.id.imageView_small_cust);

            Manage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nama_cust=CustList.get(getAdapterPosition()).getCustomerName();
                    String matric_cust=CustList.get(getAdapterPosition()).getCustomerMatric();
                    String phone_cust=CustList.get(getAdapterPosition()).getCustomerNumber();
                    String stats_cust=CustList.get(getAdapterPosition()).getStatusRequest();
                    String reqcar_cust=CustList.get(getAdapterPosition()).getOwCar();
                    String ic_custR=CustList.get(getAdapterPosition()).getICImageUrl();
                    String host_id=CustList.get(getAdapterPosition()).getID_HostingDr();
                    String current_user=CustList.get(getAdapterPosition()).getCurrent_useridd();
                    String startdate=CustList.get(getAdapterPosition()).getStartDate();
                    String enddate=CustList.get(getAdapterPosition()).getEndDate();
                    String carId=CustList.get(getAdapterPosition()).getID_carDr();

                    Intent updateIntent = new Intent(itemView.getContext(), manage_cust.class);

                    // add the data as extras to the Intent
                    updateIntent.putExtra("cust_name", nama_cust);
                    updateIntent.putExtra("cust_matric", matric_cust);
                    updateIntent.putExtra("cust_phone", phone_cust);
                    updateIntent.putExtra("cust_stats", stats_cust);
                    updateIntent.putExtra("PlateNumber", reqcar_cust);
                    updateIntent.putExtra("ic_img", ic_custR);
                    updateIntent.putExtra("host_id", host_id);
                    updateIntent.putExtra("current_user", current_user);
                    updateIntent.putExtra("startdate", startdate);
                    updateIntent.putExtra("enddate", enddate);
                    updateIntent.putExtra("carid", carId);





                    // start the update activity
                    itemView.getContext().startActivity(updateIntent);
                    ((Activity)context).finish();

                }
            });







        }
    }


    @NonNull
    @Override
    public Cust_adapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.cust_list_small,parent,false);
        return new Cust_adapter.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Cust_adapter.MyviewHolder holder, int position) {
        String nama_cust=CustList.get(position).getCustomerName();
        String matric_cust=CustList.get(position).getCustomerMatric();
        String phone_cust=CustList.get(position).getCustomerNumber();
        String stats_cust=CustList.get(position).getStatusRequest();
        String reqcar_cust=CustList.get(position).getOwCar();
        String ic_custR=CustList.get(position).getICImageUrl();

        holder.cust_name.setText(nama_cust);
        holder.cust_matrikNum.setText(matric_cust);
        holder.cust_phone.setText(phone_cust);
        holder.cust_ic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.get()
                .load(ic_custR).error(R.drawable.car_image4).into(holder.cust_ic);




    }

    @Override
    public int getItemCount() {
        return CustList.size();
    }
}
