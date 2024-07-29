package com.tryapp.myapplication3.e_carrentums;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class register extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private EditText etRegisterFullName;
    private EditText etRegisterPassword;
    private EditText etRegisterEmail;
    private EditText etRegisterMatricNumber;
    private Button register;
    private TextView login;

    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth= FirebaseAuth.getInstance();

        register= findViewById(R.id.Save_button);
        register.setOnClickListener(this);

        login= findViewById(R.id.switch_to_login);
        login.setOnClickListener(this);

        etRegisterEmail = findViewById(R.id.update_email_address);
        etRegisterPassword = findViewById(R.id.update_password);
        etRegisterFullName = findViewById(R.id.update_User_FullName);
        etRegisterMatricNumber=findViewById(R.id.update_matric_number);

        progressBar = findViewById(R.id.progressBar2);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_to_login:
                startActivity(new Intent(this,login_user.class));
                break;
            case R.id.Save_button:
                checkMatric();
                break;
        }
    }

    private void registerUser(){
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();
        String FullName = etRegisterFullName.getText().toString().trim();
        String MatricNumber = etRegisterMatricNumber.getText().toString().trim();



        if(email.isEmpty()){
            etRegisterEmail.setError("email required!");
            etRegisterEmail.requestFocus();
            return;
        }



        if(password.isEmpty()){
            etRegisterPassword.setError("password required!");
            etRegisterPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            etRegisterPassword.setError("password must more than 6 characters!");
            etRegisterPassword.requestFocus();
            return;
        }

        if(FullName.isEmpty()){
            etRegisterFullName.setError("provide fullName");
            etRegisterFullName.requestFocus();
            return;
        }

        if(MatricNumber.isEmpty()){
            etRegisterMatricNumber.setError("Provide Matric Number");
            etRegisterMatricNumber.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(email,password,FullName,MatricNumber);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(com.tryapp.myapplication3.e_carrentums.register.this,"Registration successful",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                //redirect to mainActivity
                                                SwitchToMain();
                                            }else {
                                                Toast.makeText(com.tryapp.myapplication3.e_carrentums.register.this,"Registration Have Failed",Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                return;
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(com.tryapp.myapplication3.e_carrentums.register.this,"Registration Have Failed",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }


                });


        }
    private void  SwitchToMain(){
        Intent intent = new Intent(this,Homepage.class);
        startActivity(intent);
        finish();


    }
    private void checkMatric(){
        String MatricNumber = etRegisterMatricNumber.getText().toString().trim();
        //check Number Matric
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.orderByChild("MatricNumber").equalTo(MatricNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // car matric already exists
                   etRegisterMatricNumber.setError("Matric Number Already Register");

                    return;
                } else {
                    // car matric does not exist
                    registerUser();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
}}
