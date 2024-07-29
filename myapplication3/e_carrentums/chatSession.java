package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatSession extends AppCompatActivity {

    private ArrayList<messej>chatdet=new ArrayList<>();
    private RecyclerView recyclerView;
    private chatSessionAdapter adapter;
    private int backButtonPresses = 0;
    private ImageButton sent;
    private ImageView deletechat;
    private EditText mesej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_session);

        recyclerView=findViewById(R.id.chat_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create the adapter and set it to the RecyclerView
        adapter = new chatSessionAdapter(chatdet, this);
        recyclerView.setAdapter(adapter);

        chatdet=new ArrayList<>();

        sent=findViewById(R.id.imageButton5);
        mesej=findViewById(R.id.editTextTextPersonName5);
        deletechat= findViewById(R.id.imageView37);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        deletechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Delete item");
                builder.setMessage("Are you want to clear chat?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deletechat();
                                refresh();
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

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentmessage();
            }
        });

        retrive_idchat();
        setadapter();
    }


    private void deletechat() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String newchatID = FirebaseDatabase.getInstance().getReference("Chat Details").push().getKey();




        DatabaseReference nameRef= FirebaseDatabase.getInstance().getReference().child("Chat");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.child("PK_Host").getValue(String.class).equals(uid)||childSnapshot.child("PK_Renter").getValue(String.class).equals(uid)) {
                            ChatCar chat=childSnapshot.getValue(ChatCar.class);
                            String chatid=chat.maupkmessage();



                            String msg=mesej.getText().toString().trim();
                            message nchat= new message(uid,msg);
                            FirebaseDatabase.getInstance().getReference("Chat Details")
                                    .child(chatid)
                                            .removeValue();




                        }else{
                            return;
                        }


                    }
                }else {
                    Toast.makeText(chatSession.this, "No Data at Renting", Toast.LENGTH_SHORT).show();

                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sentmessage() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String newchatID = FirebaseDatabase.getInstance().getReference("Chat Details").push().getKey();
        String chatID=getIntent().getStringExtra("chatid");



        DatabaseReference nameRef= FirebaseDatabase.getInstance().getReference().child("Chat");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
String receiver=getIntent().getStringExtra("current_user");
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.child("PK_Host").getValue(String.class).equals(uid)&&childSnapshot.child("PK_Renter").getValue(String.class).equals(receiver)) {
                            ChatCar chat=childSnapshot.getValue(ChatCar.class);
                            String chatid=chat.maupkmessage();



                           String msg=mesej.getText().toString().trim();
                            message nchat= new message(uid,msg);
                            FirebaseDatabase.getInstance().getReference("Chat Details")
                                    .child(chatID)
                                    .child(newchatID)
                                    .setValue(nchat).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            mesej.setText("");



                                        }
                                    });


                        }else{
                            return;
                        }


                    }
                }else {
                    Toast.makeText(chatSession.this, "No Data at Renting", Toast.LENGTH_SHORT).show();

                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void retrive_idchat(){
        String receiver=getIntent().getStringExtra("current_user");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String newchatID = FirebaseDatabase.getInstance().getReference("Chat Details").push().getKey();
        String chatID=getIntent().getStringExtra("chatid");




        DatabaseReference nameRef= FirebaseDatabase.getInstance().getReference().child("Chat");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.child("PK_Host").getValue(String.class).equals(uid)&&childSnapshot.child("PK_Renter").getValue(String.class).equals(receiver)) {
                            ChatCar chat=childSnapshot.getValue(ChatCar.class);
                            if(childSnapshot.child("PK_Host").getValue(String.class).equals(uid)){
                                String renter=chat.getchatdetails();
                                TextView textView=findViewById(R.id.textView75);
                                textView.setText(renter);
                            }else {

                                String host=chat.mauhostname();
                                TextView textView=findViewById(R.id.textView75);
                                textView.setText(host);
                            }

                            String chatid=chat.maupkmessage();
                            retrieveDataFirebase(chatid);





                        }else{
                            return;
                        }


                    }
                }else {
                    Toast.makeText(chatSession.this, "No Data at Renting", Toast.LENGTH_SHORT).show();

                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void retrieveDataFirebase(String chatid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chat Details").child(chatid);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                messej mesek = snapshot.getValue(messej.class);
                if (mesek != null) {
                    chatdet.add(mesek);
                    adapter.notifyItemInserted(chatdet.size() - 1); // Notify the adapter that a new message has been added
                }
            }

            // Other methods of the ChildEventListener interface
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    private void refresh() {
        Intent homeIntent = new Intent(this, chatSession.class);
        startActivity(homeIntent);
        finish();
    }

    private void setadapter() {
        adapter=new chatSessionAdapter(chatdet,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {


        backButtonPresses++;
        handleBackButtonPress();
        super.onBackPressed();
    }
    private void handleBackButtonPress() {
        if (backButtonPresses == 1) {
            // redirect to home page
            Intent homeIntent = new Intent(this, Homepage.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }
}