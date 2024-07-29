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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class RentActivity_motorAdapter extends RecyclerView.Adapter<RentActivity_motorAdapter.MyViewHolder> {

    private static ArrayList<motorcycle> rentingList;
    private static Context context;

    public RentActivity_motorAdapter(ArrayList<motorcycle> rentingList, Context context) {
        this.rentingList = rentingList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        private TextView ShowCarName;
        private TextView ShowDate;
        private TextView ShowPlace;
        private TextView ShowPrice;
        private ImageView CarImage;
        private Button request;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            ShowCarName=itemView.findViewById(R.id.Rent_name_singlemotor);
            ShowDate=itemView.findViewById(R.id.Rent_date_singlemotor);
            ShowPlace=itemView.findViewById(R.id.Rent_place_singlemotor);
            ShowPrice=itemView.findViewById(R.id.Rent_Price_singlemotor);
            CarImage=itemView.findViewById(R.id.RentImage_singlemotor);
            request=itemView.findViewById(R.id.Request_rent_singlemotor);

            CarImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Renting motor");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {

                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    if (childSnapshot.child("current_useridd").getValue(String.class).equals(uid)) {


                                        Toast.makeText(context, "YOU ARE CURRENTLY RENTING", Toast.LENGTH_SHORT).show();
                                        return;



                                    }else{
                                        // retrieve the data for the current item

                                        String nama = rentingList.get(getAdapterPosition()).getMotorcycleName();
                                        String tarikh = rentingList.get(getAdapterPosition()).getMotorAvailability();
                                        String imageurlcar = rentingList.get(getAdapterPosition()).getMotorImageUrl();
                                        String plate=rentingList.get(getAdapterPosition()).getMotorPlateNumber();
                                        String phone=rentingList.get(getAdapterPosition()).getOwnerMotorNumberPhone();
                                        String harga=rentingList.get(getAdapterPosition()).getMotorPrice();
                                        String Gear=rentingList.get(getAdapterPosition()).getMotorTransmission();
                                        String tempatAmbil=rentingList.get(getAdapterPosition()).getPickupMotor();
                                        String owner= rentingList.get(getAdapterPosition()).getMotorMatric();
                                        String hostid=rentingList.get(getAdapterPosition()).getUserMotor_ID();
                                        String userid= rentingList.get(getAdapterPosition()).getMotor_id();



                                        // create an Intent
                                        Intent updateIntent = new Intent(itemView.getContext(), ActivityView_MotorDetails.class);

                                        // add the data as extras to the Intent
                                        updateIntent.putExtra("Nama", nama);
                                        updateIntent.putExtra("Tarikh", tarikh);
                                        updateIntent.putExtra("ImageURL", imageurlcar);
                                        updateIntent.putExtra("PlateNumber", plate);
                                        updateIntent.putExtra("NumberPhone", phone);
                                        updateIntent.putExtra("Price", harga);
                                        updateIntent.putExtra("gearType", Gear);
                                        updateIntent.putExtra("pickupPlace", tempatAmbil);
                                        updateIntent.putExtra("ownerMatric", owner);
                                        updateIntent.putExtra("hostid", hostid);
                                        updateIntent.putExtra("userid",userid);


                                        // start the update activity
                                        itemView.getContext().startActivity(updateIntent);
                                        ((Activity)context).finish();
                                    }


                                }
                            }else {
                                // retrieve the data for the current item
                                // retrieve the data for the current item

                                String nama = rentingList.get(getAdapterPosition()).getMotorcycleName();
                                String tarikh = rentingList.get(getAdapterPosition()).getMotorAvailability();
                                String imageurlcar = rentingList.get(getAdapterPosition()).getMotorImageUrl();
                                String plate=rentingList.get(getAdapterPosition()).getMotorPlateNumber();
                                String phone=rentingList.get(getAdapterPosition()).getOwnerMotorNumberPhone();
                                String harga=rentingList.get(getAdapterPosition()).getMotorPrice();
                                String Gear=rentingList.get(getAdapterPosition()).getMotorTransmission();
                                String tempatAmbil=rentingList.get(getAdapterPosition()).getPickupMotor();
                                String owner= rentingList.get(getAdapterPosition()).getMotorMatric();
                                String hostid=rentingList.get(getAdapterPosition()).getUserMotor_ID();
                                String userid= rentingList.get(getAdapterPosition()).getMotor_id();



                                // create an Intent
                                Intent updateIntent = new Intent(itemView.getContext(), ActivityView_MotorDetails.class);

                                // add the data as extras to the Intent
                                updateIntent.putExtra("Nama", nama);
                                updateIntent.putExtra("Tarikh", tarikh);
                                updateIntent.putExtra("ImageURL", imageurlcar);
                                updateIntent.putExtra("PlateNumber", plate);
                                updateIntent.putExtra("NumberPhone", phone);
                                updateIntent.putExtra("Price", harga);
                                updateIntent.putExtra("gearType", Gear);
                                updateIntent.putExtra("pickupPlace", tempatAmbil);
                                updateIntent.putExtra("ownerMatric", owner);
                                updateIntent.putExtra("hostid", hostid);
                                updateIntent.putExtra("userid",userid);


                                // start the update activity
                                itemView.getContext().startActivity(updateIntent);
                                ((Activity)context).finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });



        }
    }

    @NonNull
    @Override
    public RentActivity_motorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.rent_motoritem,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RentActivity_motorAdapter.MyViewHolder holder, int position) {

        String NamaTuKereta= rentingList.get(position).getMotorcycleName();
        String HargaTuKereta= rentingList.get(position).getMotorPrice();
        String TempatKereta= rentingList.get(position).getPickupMotor();
        String CarDate= rentingList.get(position).getMotorAvailability();
        String Carimage= rentingList.get(position).getMotorImageUrl();

        holder.ShowCarName.setText(NamaTuKereta);
        holder.ShowPrice.setText(HargaTuKereta);
        holder.ShowPlace.setText(TempatKereta);
        holder.ShowDate.setText(CarDate);
        Picasso.get().load(Carimage).error(R.drawable.car_image4).into(holder.CarImage);


    }

    @Override
    public int getItemCount() {
        return rentingList.size();
    }
    public void filterList(ArrayList<motorcycle>filteredList){
        rentingList=filteredList;
        notifyDataSetChanged();
    }


}


