package com.tryapp.myapplication3.e_carrentums;

public class BackYard_details {

    private String uid;
    private String Contact;
    private String Matric_backyard;
    private String Image_backyard;
    private String Title;
    private String Description;

    public BackYard_details() {
    }

    public BackYard_details(String Uid,String contact, String matric_backyard, String image_backyard, String title, String description) {
        uid=Uid;
        Contact = contact;
        Matric_backyard = matric_backyard;
        Image_backyard = image_backyard;
        Title = title;
        Description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getMatric_backyard() {
        return Matric_backyard;
    }

    public void setMatric_backyard(String matric_backyard) {
        Matric_backyard = matric_backyard;
    }

    public String getImage_backyard() {
        return Image_backyard;
    }

    public void setImage_backyard(String image_backyard) {
        Image_backyard = image_backyard;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
