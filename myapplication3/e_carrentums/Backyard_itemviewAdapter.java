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

public class Backyard_itemviewAdapter extends RecyclerView.Adapter<Backyard_itemviewAdapter.MyviewHolder> {

    private static ArrayList<BackYard_details> backYardlist;
    private static Context context;

    public Backyard_itemviewAdapter(ArrayList<BackYard_details> backYardlist, Context context) {
        this.backYardlist = backYardlist;
        this.context = context;
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        private ImageView itempic;
        private TextView itemName;
        private TextView contct;
        private Button details;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            itempic=itemView.findViewById(R.id.imageView_BY);
            itemName=itemView.findViewById(R.id.textView_itemName4);
            contct=itemView.findViewById(R.id.textView_itemName5);
            details=itemView.findViewById(R.id.button_details);

            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // retrieve the data for the current item
                    String image = backYardlist.get(getAdapterPosition()).getImage_backyard();
                    String phonebk = backYardlist.get(getAdapterPosition()).getContact();
                    String title = backYardlist.get(getAdapterPosition()).getTitle();
                    String desc = backYardlist.get(getAdapterPosition()).getDescription();
                    String uid=backYardlist.get(getAdapterPosition()).getUid();
                    String matr=backYardlist.get(getAdapterPosition()).getMatric_backyard();



                    // create an Intent
                    Intent updateIntent = new Intent(itemView.getContext(),backyard_seemore.class);

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
        }
    }

    @NonNull
    @Override
    public Backyard_itemviewAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.itemviewall,parent,false);
        return new Backyard_itemviewAdapter.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Backyard_itemviewAdapter.MyviewHolder holder, int position) {

        String title=backYardlist.get(position).getTitle();
        String image=backYardlist.get(position).getImage_backyard();
        String contact=backYardlist.get(position).getContact();


        holder.itemName.setText(title);
        holder.contct.setText(contact);
        holder.itempic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(image).error(R.drawable.indicator_drawable).into(holder.itempic);

    }

    @Override
    public int getItemCount() {
        return backYardlist.size();
    }

    public void filterList(ArrayList<BackYard_details>filteredList){
        backYardlist=filteredList;
        notifyDataSetChanged();
    }
}
