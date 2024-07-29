package com.tryapp.myapplication3.e_carrentums;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.MyViewHolder>{
    private ArrayList<Car> HostList;
    private Context context;

    public HostAdapter(ArrayList<Car> hostList, Context context) {
        HostList = hostList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{


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

            ShowHostproduct=itemView.findViewById(R.id.Host_brand);
            Showplatecar=itemView.findViewById(R.id.host_plate);
            ShowHostName=itemView.findViewById(R.id.Host_Name);
            ShowHostDate=itemView.findViewById(R.id.Host_date);
            showHostcar=itemView.findViewById(R.id.imageView_Host);
            showHostcar.setScaleType(ImageView.ScaleType.CENTER_CROP);
            updateCar=itemView.findViewById(R.id.button3);
            deleteCar=itemView.findViewById(R.id.button2);

            updateCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // retrieve the data for the current item
                    String jenama = HostList.get(getAdapterPosition()).getProduct();
                    String nama = HostList.get(getAdapterPosition()).getNamaKereta();
                    String Status_kereta = HostList.get(getAdapterPosition()).getStatus_kereta();
                    String imageurlcar = HostList.get(getAdapterPosition()).getGambar();
                    String plate=HostList.get(getAdapterPosition()).mauflatnombor();
                    String phone=HostList.get(getAdapterPosition()).getNombortelepon();
                    String harga=HostList.get(getAdapterPosition()).getMauHarga();
                    String carId=HostList.get(getAdapterPosition()).getuid_host();
                    String pickup=HostList.get(getAdapterPosition()).getTempatAmbilKereta();



                    // create an Intent
                    Intent updateIntent = new Intent(itemView.getContext(), UpdateHost.class);




                    // add the data as extras to the Intent
                    updateIntent.putExtra("carId",carId);
                    updateIntent.putExtra("Jenama", jenama);
                    updateIntent.putExtra("Nama", nama);
                    updateIntent.putExtra("status", Status_kereta);
                    updateIntent.putExtra("ImageURL", imageurlcar);
                    updateIntent.putExtra("PlateNumber", plate);
                    updateIntent.putExtra("NumberPhone", phone);
                    updateIntent.putExtra("Price", harga);
                    updateIntent.putExtra("pickup", pickup);


                    // start the update activity
                    itemView.getContext().startActivity(updateIntent);
                    ((Activity)context).finish();

                }
            });
            deleteCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setTitle("Delete Car");
                    builder.setMessage("Are you want to delete this car?")
                            .setCancelable(true)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final int position = getAdapterPosition();
                                    final String carPlate = HostList.get(position).mauflatnombor();

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hosting")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    // Add a single-shot value event listener to the reference
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            // Iterate through the snapshots of the children
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                // Check if the plate number matches the one specified
                                                if (childSnapshot.child("PlateNumber").getValue(String.class).equals(carPlate)) {
                                                    // Get a reference to the child node that corresponds to the plate number
                                                    String carUid = childSnapshot.getKey();
                                                    // Get a reference to the child node that corresponds to the uid
                                                    DatabaseReference carRef = reference.child(carUid);
                                                    // Remove the value at the child node
                                                    carRef.removeValue();

                                                    DatabaseReference imageUrlRef = childSnapshot.child("ImageUrl").getRef();
                                                    // Remove the value at the child node
                                                    imageUrlRef.removeValue();

                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    StorageReference storageRef = storage.getReference();
                                                    // Create a unique file name for the image
                                                    String fileName = carPlate+".jpg";
                                                    // Create a reference to the file location in storage
                                                    StorageReference imageRef = storageRef.child("images/"+fileName);
                                                    imageRef.delete();


                                                    // create an Intent
                                                    Intent refreshIntent = new Intent(itemView.getContext(), Host_list.class);
                                                    itemView.getContext().startActivity(refreshIntent);
                                                    ((Activity)context).finish();


                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });

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


    }


    @NonNull
    @Override
    public HostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.host_listview,parent,false);
        return new HostAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HostAdapter.MyViewHolder holder, int position) {
        String jenama=HostList.get(position).getProduct();
        String nama=HostList.get(position).getNamaKereta();
        String tarikh=HostList.get(position).getStatus_kereta();
        String imageurlcar=HostList.get(position).getGambar();
        String platkersh=HostList.get(position).mauflatnombor();

        holder.ShowHostName.setText(nama);
        holder.ShowHostproduct.setText(jenama);
        holder.ShowHostDate.setText(tarikh);
        holder.Showplatecar.setText(platkersh);

        Picasso.get().load(imageurlcar).into(holder.showHostcar);


    }

    @Override
    public int getItemCount() {
        return HostList.size();
    }
}

