package com.tryapp.myapplication3.e_carrentums;

public class RentingDetailsMotor {
    private String OwnerMatric;
    private String OwnerNumber;
    private String CustomerMatric;
    private String CustomerName;
    private String CustomerNumber;
    private String ICImageUrl;
    private String LicenseUrl;
    private String StatusRequest;
    private String Owmotor;
    private String ID_Hosting;
    private String ID_motor;

    private String StartDate;
    private String EndDate;
    private String Current_user;

    public String getOwnerMatricm() {
        return OwnerMatric;
    }

    public void setOwnerMatricm(String ownerMatric) {
        OwnerMatric = ownerMatric;
    }

    public String getOwnerNumberm() {
        return OwnerNumber;
    }

    public void setOwnerNumberm(String ownerNumber) {
        OwnerNumber = ownerNumber;
    }

    public String getCustomerMatricm() {
        return CustomerMatric;
    }

    public void setCustomerMatricm(String customerMatric) {
        CustomerMatric = customerMatric;
    }

    public String getCustomerNamem() {
        return CustomerName;
    }

    public void setCustomerNamem(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerNumberm() {
        return CustomerNumber;
    }

    public void setCustomerNumberm(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getICImageUrlm() {
        return ICImageUrl;
    }

    public void setICImageUrlm(String ICImageUrl) {
        this.ICImageUrl = ICImageUrl;
    }

    public String getLicenseUrlm() {
        return LicenseUrl;
    }

    public void setLicenseUrlm(String licenseUrl) {
        LicenseUrl = licenseUrl;
    }

    public String getStatusRequestm() {
        return StatusRequest;
    }

    public void setStatusRequestm(String statusRequest) {
        StatusRequest = statusRequest;
    }

    public String getOwmotorm() {
        return Owmotor;
    }

    public void setOwmotorm(String owmotor) {
        Owmotor = owmotor;
    }

    public String getID_Hostingm() {
        return ID_Hosting;
    }

    public void setID_Hostingm(String ID_Hosting) {
        this.ID_Hosting = ID_Hosting;
    }

    public String getID_motorm() {
        return ID_motor;
    }

    public void setID_motorm(String ID_motor) {
        this.ID_motor = ID_motor;
    }

    public String getStartDatem() {
        return StartDate;
    }

    public void setStartDatem(String startDate) {
        StartDate = startDate;
    }

    public String getEndDatem() {
        return EndDate;
    }

    public void setEndDatem(String endDate) {
        EndDate = endDate;
    }

    public String getCurrent_userm() {
        return Current_user;
    }

    public void setCurrent_userm(String current_user) {
        Current_user = current_user;
    }

    public RentingDetailsMotor(String ID_Hosting,String ID_motor,String current_user,String ownerMatric, String ownerNumber,String owmotor, String customerMatric, String customerName, String customerNumber, String ICImageUrl, String licenseUrl, String statusRequest,String startDate, String endDate) {
        OwnerMatric = ownerMatric;
        OwnerNumber = ownerNumber;
        CustomerMatric = customerMatric;
        CustomerName = customerName;
        CustomerNumber = customerNumber;
        this.ICImageUrl = ICImageUrl;
        LicenseUrl = licenseUrl;
        StatusRequest = statusRequest;
        Owmotor = owmotor;
        this.ID_Hosting = ID_Hosting;
        this.ID_motor = ID_motor;
        StartDate = startDate;
        EndDate = endDate;
        Current_user = current_user;
    }
}
