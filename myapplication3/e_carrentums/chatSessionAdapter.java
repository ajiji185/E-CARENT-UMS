package com.tryapp.myapplication3.e_carrentums;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class chatSessionAdapter extends RecyclerView.Adapter<chatSessionAdapter.MyViewHolder> {

    private ArrayList<messej> chatdet;
    private  Context context;

    public chatSessionAdapter( ArrayList<messej> chatdet, Context context) {

        this.chatdet = chatdet;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView chat;
        private CardView views;
        private TextView chat2;
        private CardView views2;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            chat=itemView.findViewById(R.id.textView74);
            views=itemView.findViewById(R.id.card);

        }
    }

    @NonNull
    @Override
    public chatSessionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.chat_list_menu,parent,false);
        return new chatSessionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull chatSessionAdapter.MyViewHolder holder, int position) {

        String msgid=chatdet.get(position).getIDMsg();
        String mesg = chatdet.get(position).getMessagedetails();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        if (msgid.equals(uid)) {
            holder.views.setBackgroundColor(Color.parseColor("#EDEDED"));

            holder.chat.setText(mesg);

        } else {
            holder.views.setBackgroundColor(Color.parseColor("#DAD0FD"));
            holder.chat.setText(mesg);}


















    }

    @Override
    public int getItemCount() {
        return  chatdet.size();
    }

    private int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}
