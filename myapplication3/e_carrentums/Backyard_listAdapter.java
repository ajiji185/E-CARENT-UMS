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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Backyard_listAdapter extends RecyclerView.Adapter<Backyard_listAdapter.MyViewHolder> {

    private static ArrayList<BackYard_details> BKlist;
    private static Context context;

    public Backyard_listAdapter(ArrayList<BackYard_details> BKlist, Context context) {
        this.BKlist = BKlist;
        this.context = context;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView itempic;
        private TextView itemName;
        private Button update;
        private Button delete;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itempic=itemView.findViewById(R.id.imageView30);
            itemName=itemView.findViewById(R.id.textView55);
            update=itemView.findViewById(R.id.button11);
            delete=itemView.findViewById(R.id.button10);


            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // retrieve the data for the current item
                    String image = BKlist.get(getAdapterPosition()).getImage_backyard();
                    String phonebk = BKlist.get(getAdapterPosition()).getContact();
                    String title = BKlist.get(getAdapterPosition()).getTitle();
                    String desc = BKlist.get(getAdapterPosition()).getDescription();
                    String uid=BKlist.get(getAdapterPosition()).getUid();
                    String matr=BKlist.get(getAdapterPosition()).getMatric_backyard();



                    // create an Intent
                    Intent updateIntent = new Intent(itemView.getContext(),Update_bkList.class);

                    // add the data as extras to the Intent
                    updateIntent.putExtra("gambar", image);
                    updateIntent.putExtra("phone", phonebk);
                    updateIntent.putExtra("tajuk", title);
                    updateIntent.putExtra("description", desc);
                    updateIntent.putExtra("Uid",uid);
                    updateIntent.putExtra("matrik",matr);

                    // start the update activity
                    itemView.getContext().startActivity(updateIntent);
                    ((Activity)context).finish();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                                String uid=BKlist.get(getAdapterPosition()).getUid();

                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    builder.setTitle("Delete item");
                    builder.setMessage("Are you want to delete this item?")
                            .setCancelable(true)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Backyard_Sale")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(uid);
                                    ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            String title=BKlist.get(getAdapterPosition()).getUid();

                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageRef = storage.getReference();
                                            // Create a unique file name for the image
                                            String fileName = title+"(item)"+".jpg";
                                            // Create a reference to the file location in storage
                                            StorageReference imageRef = storageRef.child("BackYardSale/"+fileName);
                                            imageRef.delete();
                                            Toast.makeText(context, "item deleted", Toast.LENGTH_SHORT).show();


                                            // create an Intent
                                            Intent refreshIntent = new Intent(itemView.getContext(), BackYard_list.class);
                                            itemView.getContext().startActivity(refreshIntent);
                                            ((Activity)context).finish();


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
    public Backyard_listAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.backyard_list,parent,false);
        return new Backyard_listAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Backyard_listAdapter.MyViewHolder holder, int position) {
        String title=BKlist.get(position).getTitle();
        String image=BKlist.get(position).getImage_backyard();

        holder.itemName.setText(title);
        holder.itempic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.get().load(image).error(R.drawable.indicator_drawable).into(holder.itempic);

    }

    @Override
    public int getItemCount() {
        return BKlist.size();
    }
}
