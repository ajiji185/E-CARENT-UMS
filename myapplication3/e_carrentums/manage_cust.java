package com.tryapp.myapplication3.e_carrentums;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class manage_cust extends AppCompatActivity {

    private TextView name;
    private TextView mat;
    private TextView phone;
    private TextView custstats;
    private TextView carpl;
    private int backButtonPresses=0;
    private ImageView icCust;

    private Button accept;
    private Button reject;
    private Button finish;
    private TextView startdate;
    private TextView enddate;
    private Button chat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_listview);


        String custName = getIntent().getStringExtra("cust_name");
        String custmat = getIntent().getStringExtra("cust_matric");
        String custphone = getIntent().getStringExtra("cust_phone");
        String imageic = getIntent().getStringExtra("ic_img");
        String platreq=getIntent().getStringExtra("PlateNumber");
        String stats=getIntent().getStringExtra("cust_stats");
        String host_id=getIntent().getStringExtra("host_id");
        String user_id=getIntent().getStringExtra("current_user");
        String carid=getIntent().getStringExtra("carid");
        String start=getIntent().getStringExtra("startdate");
        String end=getIntent().getStringExtra("enddate");



        name=findViewById(R.id.viewname);
        mat=findViewById(R.id.textView_numsm);
        phone=findViewById(R.id.textViewphone);
        custstats=findViewById(R.id.statscust);
        carpl=findViewById(R.id.carplNum);
        icCust=findViewById(R.id.imageViewcust);
        icCust.setScaleType(ImageView.ScaleType.CENTER_CROP);
        accept=findViewById(R.id.accept_btn);
        reject=findViewById(R.id.button8);
        finish=findViewById(R.id.button16);
        startdate=findViewById(R.id.textView70);
        enddate=findViewById(R.id.textView73);
        chat=findViewById(R.id.button7);

        startdate.setText(start);
        enddate.setText(end);



        name.setText(custName);
        mat.setText(custmat);
        phone.setText(custphone);
        custstats.setText(stats);
        carpl.setText(platreq);
        Picasso.get().load(imageic).error(R.drawable.car_image4).into(icCust);
        String verify="Accepted";
        finish.setVisibility(View.GONE);
        chat.setVisibility(View.GONE);

        if(stats.equals(verify)){
            reject.setVisibility(View.GONE);
            accept.setVisibility(View.GONE);
            finish.setVisibility(View.VISIBLE);
            chat.setVisibility(View.GONE);
        }
        if(stats.equals("Rejected")){
            accept.setVisibility(View.GONE);
            finish.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
            chat.setVisibility(View.GONE);
        }
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getCurrentUser().getUid();
                String userid=getIntent().getStringExtra("current_user");
                String newchatID = FirebaseDatabase.getInstance().getReference("Chat Details").push().getKey();




                DatabaseReference nameRef= FirebaseDatabase.getInstance().getReference().child("Chat");
                nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {

                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if (childSnapshot.child("PK_Host").getValue(String.class).equals(uid)&&childSnapshot.child("PK_Renter").getValue(String.class).equals(userid)) {
                                    ChatCar chat=childSnapshot.getValue(ChatCar.class);
                                    String chatid=chat.maupkmessage();




                                    Intent intent=new Intent(manage_cust.this, chatSession.class);
                                    intent.putExtra("idnow",uid);
                                    intent.putExtra("current_user",userid);
                                    intent.putExtra("chatid",chatid);
                                    startActivity(intent);
                                    finish();




                                }else{
                                    return;
                                }


                            }
                        }else {
                            Toast.makeText(manage_cust.this, "No Data at Renting", Toast.LENGTH_SHORT).show();

                            return;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(manage_cust.this);
                builder.setTitle("Renting Complete");
                builder.setMessage("are your car safely arrived?")
                        .setCancelable(true)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteDetails();
                                deletechat();
                                back();

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

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Renting");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                            // Check if the plate number matches the one specified
                            String check=getIntent().getStringExtra("PlateNumber");
                            if (childSnapshot.child("owCar").getValue(String.class).equals(check)){
                                childSnapshot.child("statusRequest").getRef().setValue("Rejected");
                                Toast.makeText(manage_cust.this, "Reject Request", Toast.LENGTH_SHORT).show();


                                back();

                            }




                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Renting");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                            // Check if the plate number matches the one specified
                            String check=getIntent().getStringExtra("PlateNumber");
                            if (childSnapshot.child("owCar").getValue(String.class).equals(check)){
                                childSnapshot.child("statusRequest").getRef().setValue("Accepted");
                                Toast.makeText(manage_cust.this, "Accept request", Toast.LENGTH_SHORT).show();
                                createchat();



                                back();

                            }




                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void deletechat() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        String newchatID = FirebaseDatabase.getInstance().getReference("Chat Details").push().getKey();
        String userid=getIntent().getStringExtra("current_user");




        DatabaseReference nameRef= FirebaseDatabase.getInstance().getReference().child("Chat");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        if (childSnapshot.child("PK_Host").getValue(String.class).equals(uid)||childSnapshot.child("PK_Renter").getValue(String.class).equals(userid)) {
                            ChatCar chat=childSnapshot.getValue(ChatCar.class);
                            String chatid=chat.maupkmessage();




                            FirebaseDatabase.getInstance().getReference("Chat Details")
                                    .child(chatid)
                                    .removeValue();
                            FirebaseDatabase.getInstance().getReference("Chat")
                                    .child(chatid)
                                    .removeValue();




                        }else{
                            return;
                        }


                    }
                }else {
                    Toast.makeText(manage_cust.this, "No Data at Renting", Toast.LENGTH_SHORT).show();

                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteDetails() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Renting");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Check if the plate number matches the one specified
                        String check=getIntent().getStringExtra("PlateNumber");
                        if (childSnapshot.child("owCar").getValue(String.class).equals(check)){
                                childSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(manage_cust.this, "Order Finish", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }




                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void deleteimg(String pl) {
        String checkr=getIntent().getStringExtra("PlateNumber");
        String custmat = getIntent().getStringExtra("cust_matric");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String file=checkr+"/";
        String imageic= custmat+"(IC)"+".jpg";
        String imageLC= custmat+"(Licence)"+".jpg";
        StorageReference fileRef = storageRef.child("Renting/"+imageic);
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully

                StorageReference ref= storageRef.child("Renting/"+imageLC);
                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful delete
            }
        });




    }

    private void changeStatusHost_accept() {
        String Hostid = getIntent().getStringExtra("carid");
        String userid = getIntent().getStringExtra("host_id");
        if (userid == null || Hostid == null) {
            Log.e("changeStatusHost", "userid or Hostid is null");
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hosting")
                .child(Hostid).child(userid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("Availability").getRef().setValue("Rented");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void changeStatusHost_reject() {
        String Hostid = getIntent().getStringExtra("carid");
        String userid = getIntent().getStringExtra("host_id");
        if (userid == null || Hostid == null) {
            Log.e("changeStatusHost", "userid or Hostid is null");
            return;
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Hosting")
                .child(Hostid).child(userid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.child("Availability").getRef().setValue("Available");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void createchat(){
        String pkrent=getIntent().getStringExtra("current_user");
        String custName = getIntent().getStringExtra("cust_name");
        String carPlate=getIntent().getStringExtra("PlateNumber");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        DatabaseReference nameRef=FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User use=snapshot.getValue(User.class);

                    String name=use.FullName;


                    String chatId = FirebaseDatabase.getInstance().getReference("Chat").push().getKey();
                    ChatCar chat= new ChatCar(chatId,uid,pkrent,name,custName);
                    FirebaseDatabase.getInstance().getReference("Chat")
                            .child(chatId)
                            .setValue(chat);


                }else {
                    Toast.makeText(manage_cust.this, "Error in database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });



    }



    private void back() {
        Intent intent=new Intent(manage_cust.this,customer_list.class);
        startActivity(intent);
        finish();

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
            Intent homeIntent = new Intent(this, customer_list.class);
            startActivity(homeIntent);
            finish();

        }
        super.onBackPressed();
    }
}