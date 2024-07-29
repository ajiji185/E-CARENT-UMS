package com.tryapp.myapplication3.e_carrentums;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.DummyViewHolder> {
    private ArrayList<Dummylist> dummyList;
    private Context context;

    public DummyAdapter(Context context, ArrayList<Dummylist> dummyList) {
        this.context = context;
        this.dummyList = dummyList;
    }

    // Provide a reference to the views for each data item
    class DummyViewHolder extends RecyclerView.ViewHolder {
        private TextView dummyNameTextView;
        private Button butn;

        DummyViewHolder(@NonNull View itemView) {
            super(itemView);
            dummyNameTextView = itemView.findViewById(R.id.Text_Dumy);
            butn=itemView.findViewById(R.id.button);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DummyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.dummy_item, parent, false);
        return new DummyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull DummyViewHolder holder, int position) {
        String dummyName = dummyList.get(position).getName();
        holder.dummyNameTextView.setText(dummyName);
        holder.butn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,Homepage.class);
                context.startActivity(intent);

            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dummyList.size();
    }
}
