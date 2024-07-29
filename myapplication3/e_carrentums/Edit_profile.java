package com.tryapp.myapplication3.e_carrentums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_profile extends AppCompatActivity {
    private int backButtonPresses = 0;
    EditText editEmail,editPass,editName,editMatric;
    Button save;
    String emailUser,passwordUser,FullNameUser,MatricUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this, login_user.class);
            startActivity(intent);
            finish();
            return;
        }


        //back button
        ImageButton back= findViewById(R.id.back_btn_to_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoProfile();
            }
        });


        editEmail=findViewById(R.id.update_email_address);
        editPass=findViewById(R.id.update_password);
        editName=findViewById(R.id.update_User_FullName);
        editMatric=findViewById(R.id.update_matric_number);
        save=findViewById(R.id.Save_button);
        showdata();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });




    }






    private void updateUser(){
        String email = editEmail.getText().toString().trim();
        String password =  editPass.getText().toString().trim();
        String FullName =  editName.getText().toString().trim();
        String MatricNumber = editMatric.getText().toString().trim();






                            User user = new User(email,password,FullName,MatricNumber);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Edit_profile.this,"update successful",Toast.LENGTH_LONG).show();


                                                //redirect to mainActivity
                                                SwitchTorprofile();
                                            }else {
                                                Toast.makeText(Edit_profile.this,"update Have Failed",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });






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
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        }
        super.onBackPressed();
    }

    private void SwitchTorprofile() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void showdata(){

        FirebaseAuth auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    editEmail.setText( user.Email);
                    editPass.setText( user.Password);
                    editName.setText( user.FullName);
                    editMatric.setText( user.MatricNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void backtoProfile() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}