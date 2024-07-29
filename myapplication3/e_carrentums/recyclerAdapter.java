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

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private static ArrayList<Car> rentingList;
    private static Context context;

    public recyclerAdapter(ArrayList<Car> rentingList, Context context) {
        this.rentingList = rentingList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView Showproduct;
        private TextView ShowCarName;
        private TextView ShowDate;
        private TextView ShowPlace;
        private TextView ShowPrice;
        private ImageView CarImage;
        private Button request;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Showproduct=itemView.findViewById(R.id.Rent_Brand_single);
            ShowCarName=itemView.findViewById(R.id.Rent_name_single);
            ShowDate=itemView.findViewById(R.id.Rent_date_single);
            ShowPlace=itemView.findViewById(R.id.Rent_place_single);
            ShowPrice=itemView.findViewById(R.id.Rent_Price_single);
            CarImage=itemView.findViewById(R.id.RentImage_single);
            request=itemView.findViewById(R.id.Request_rent_single);

            CarImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String carPlateNumber=rentingList.get(getAdapterPosition()).mauflatnombor();

                    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Renting");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean isRenting = false;

                            for (DataSnapshot rentalSnapshot : snapshot.getChildren()) {
                                if (rentalSnapshot.child("owCar").getValue(String.class).equals(carPlateNumber)) {
                                    String currentUserid = rentalSnapshot.child("current_useridd").getValue(String.class);

                                    if (currentUserid.equals(uid)) {
                                        isRenting = true;
                                        break;
                                    }
                                }
                            }

                            if (isRenting) {
                                Toast.makeText(context, "YOU ARE CURRENTLY RENTING", Toast.LENGTH_SHORT).show();
                            } else {
                                // Add code to launch view car details activity here
                                // retrieve the data for the current item
                                String jenama = rentingList.get(getAdapterPosition()).getProduct();
                                String nama = rentingList.get(getAdapterPosition()).getNamaKereta();
                                String tarikh = rentingList.get(getAdapterPosition()).getStatus_kereta();
                                String imageurlcar = rentingList.get(getAdapterPosition()).getGambar();
                                String plate=rentingList.get(getAdapterPosition()).mauflatnombor();
                                String phone=rentingList.get(getAdapterPosition()).getNombortelepon();
                                String harga=rentingList.get(getAdapterPosition()).getMauHarga();
                                String JenisMinyak= rentingList.get(getAdapterPosition()).getJenisMinyak();
                                String Gear=rentingList.get(getAdapterPosition()).getJenisGiar();
                                String duduk=rentingList.get(getAdapterPosition()).getJenisTempatDuduk();
                                String tempatAmbil=rentingList.get(getAdapterPosition()).getTempatAmbilKereta();
                                String owner= rentingList.get(getAdapterPosition()).getNomborMatrik();
                                String hostid=rentingList.get(getAdapterPosition()).getuid_host();
                                String userid= rentingList.get(getAdapterPosition()).getuser_identity();



                                // create an Intent
                                Intent updateIntent = new Intent(itemView.getContext(), view_car_datails.class);

                                // add the data as extras to the Intent
                                updateIntent.putExtra("Jenama", jenama);
                                updateIntent.putExtra("Nama", nama);
                                updateIntent.putExtra("Tarikh", tarikh);
                                updateIntent.putExtra("ImageURL", imageurlcar);
                                updateIntent.putExtra("PlateNumber", plate);
                                updateIntent.putExtra("NumberPhone", phone);
                                updateIntent.putExtra("Price", harga);
                                updateIntent.putExtra("Minyak", JenisMinyak);
                                updateIntent.putExtra("gearType", Gear);
                                updateIntent.putExtra("seatType", duduk);
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
                            // Add code to handle cancelled database query here
                        }
                    });





                }
            });



        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView= LayoutInflater.from(context).inflate(R.layout.rent_item,parent,false);
         return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {

        String Carproduct= rentingList.get(position).getProduct();
        String NamaTuKereta= rentingList.get(position).getNamaKereta();
        String HargaTuKereta= rentingList.get(position).getMauHarga();
        String TempatKereta= rentingList.get(position).getTempatAmbilKereta();
        String CarDate= rentingList.get(position).getStatus_kereta();
        String Carimage= rentingList.get(position).getGambar();

        holder.Showproduct.setText(Carproduct);
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
    public void filterList(ArrayList<Car> filteredList){
        rentingList=filteredList;
        notifyDataSetChanged();
    }


}


