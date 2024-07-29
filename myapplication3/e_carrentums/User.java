package com.tryapp.myapplication3.e_carrentums;

public class User {
    public String Email;
    public String FullName;
    public String Password;
    public String MatricNumber;


    public User(){

    }

    public User(String Email,String Password,String FullName,String MatricNumber){
        this.Email= Email;
        this.FullName = FullName;
        this.Password = Password;
        this.MatricNumber= MatricNumber;
    }


}

