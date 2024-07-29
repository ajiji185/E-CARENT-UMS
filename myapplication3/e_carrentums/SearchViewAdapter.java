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

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.myviewholder> {
    private ArrayList<Car> rentersList;
    private  Context context;

    public SearchViewAdapter(ArrayList<Car> rentersList, Context context) {
        this.rentersList = rentersList;
        this.context = context;
    }
    public class myviewholder extends RecyclerView.ViewHolder{
        private TextView Showproduct;
        private TextView ShowCarName;
        private TextView ShowDate;
        private TextView ShowPlace;
        private TextView ShowPrice;
        private ImageView CarImage;
        private Button request;

        public myviewholder(@NonNull View itemView) {
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
                    String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Renting");
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
                                        String jenama = rentersList.get(getAdapterPosition()).getProduct();
                                        String nama = rentersList.get(getAdapterPosition()).getNamaKereta();
                                        String tarikh = rentersList.get(getAdapterPosition()).getStatus_kereta();
                                        String imageurlcar = rentersList.get(getAdapterPosition()).getGambar();
                                        String plate=rentersList.get(getAdapterPosition()).mauflatnombor();
                                        String phone=rentersList.get(getAdapterPosition()).getNombortelepon();
                                        String harga=rentersList.get(getAdapterPosition()).getMauHarga();
                                        String JenisMinyak= rentersList.get(getAdapterPosition()).getJenisMinyak();
                                        String Gear=rentersList.get(getAdapterPosition()).getJenisGiar();
                                        String duduk=rentersList.get(getAdapterPosition()).getJenisTempatDuduk();
                                        String tempatAmbil=rentersList.get(getAdapterPosition()).getTempatAmbilKereta();
                                        String owner= rentersList.get(getAdapterPosition()).getNomborMatrik();
                                        String hostid=rentersList.get(getAdapterPosition()).getuid_host();
                                        String userid= rentersList.get(getAdapterPosition()).getuser_identity();



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
                            }else {
                                // retrieve the data for the current item
                                String jenama = rentersList.get(getAdapterPosition()).getProduct();
                                String nama = rentersList.get(getAdapterPosition()).getNamaKereta();
                                String tarikh = rentersList.get(getAdapterPosition()).getStatus_kereta();
                                String imageurlcar = rentersList.get(getAdapterPosition()).getGambar();
                                String plate=rentersList.get(getAdapterPosition()).mauflatnombor();
                                String phone=rentersList.get(getAdapterPosition()).getNombortelepon();
                                String harga=rentersList.get(getAdapterPosition()).getMauHarga();
                                String JenisMinyak= rentersList.get(getAdapterPosition()).getJenisMinyak();
                                String Gear=rentersList.get(getAdapterPosition()).getJenisGiar();
                                String duduk=rentersList.get(getAdapterPosition()).getJenisTempatDuduk();
                                String tempatAmbil=rentersList.get(getAdapterPosition()).getTempatAmbilKereta();
                                String owner= rentersList.get(getAdapterPosition()).getNomborMatrik();
                                String hostid=rentersList.get(getAdapterPosition()).getuid_host();
                                String userid= rentersList.get(getAdapterPosition()).getuser_identity();



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

                        }
                    });

                }
            });
        }
    }
    @NonNull
    @Override
    public SearchViewAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.rent_item,parent,false);
        return new SearchViewAdapter.myviewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        String Carproduct= rentersList.get(position).getProduct();
        String NamaTuKereta= rentersList.get(position).getNamaKereta();
        String HargaTuKereta= rentersList.get(position).getMauHarga();
        String TempatKereta= rentersList.get(position).getTempatAmbilKereta();
        String CarDate= rentersList.get(position).getStatus_kereta();
        String Carimage= rentersList.get(position).getGambar();

        holder.Showproduct.setText(Carproduct);
        holder.ShowCarName.setText(NamaTuKereta);
        holder.ShowPrice.setText(HargaTuKereta);
        holder.ShowPlace.setText(TempatKereta);
        holder.ShowDate.setText(CarDate);
        Picasso.get().load(Carimage).error(R.drawable.car_image4).into(holder.CarImage);
    }

    @Override
    public int getItemCount() {
        return rentersList.size();
    }

    public void filterList(ArrayList<Car>filteredList){
        rentersList=filteredList;
        notifyDataSetChanged();
    }

}
