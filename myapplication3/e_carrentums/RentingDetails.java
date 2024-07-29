package com.tryapp.myapplication3.e_carrentums;

public class RentingDetails {

    private String OwnerMatric;
    private String OwnerNumber;
    private String CustomerMatric;
    private String CustomerName;
    private String CustomerNumber;
    private String ICImageUrl;
    private String LicenseUrl;
    private String StatusRequest;
    private String OwCar;
    private String ID_Hosting;
    private String ID_car;

    private String StartDate;
    private String EndDate;
    private String Current_user;

    public String getOwCar() {
        return OwCar;
    }

    public void setOwCar(String owCar) {
        OwCar = owCar;
    }

    public RentingDetails() {
    }

    public RentingDetails(String id_hosting,String id_car,String current_user,String ownerMatric, String ownerNumber,String owCar, String customerMatric, String customerName, String customerNumber, String ICImageUrl, String licenseUrl, String statusRequest,String start_date, String end_date) {
       ID_Hosting=id_hosting;
       ID_car=id_car;
       Current_user=current_user;
        OwnerMatric = ownerMatric;
        OwnerNumber = ownerNumber;
        CustomerMatric = customerMatric;
        CustomerName = customerName;
        CustomerNumber = customerNumber;
        this.ICImageUrl = ICImageUrl;
        LicenseUrl = licenseUrl;
        StatusRequest = statusRequest;
        OwCar=owCar;
        StartDate=start_date;
        EndDate=end_date;
    }

    public String getCurrent_useridd() {
        return Current_user;
    }

    public void setCurrent_useridd(String current_user) {
        Current_user = current_user;
    }

    public String getID_HostingDr() {
        return ID_Hosting;
    }

    public void setID_HostingDr(String ID_Hosting) {
        this.ID_Hosting = ID_Hosting;
    }

    public String getID_carDr() {
        return ID_car;
    }

    public void setID_carDr(String ID_car) {
        this.ID_car = ID_car;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getOwnerMatric() {
        return OwnerMatric;
    }

    public void setOwnerMatric(String ownerMatric) {
        OwnerMatric = ownerMatric;
    }

    public String getOwnerNumber() {
        return OwnerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        OwnerNumber = ownerNumber;
    }

    public String getCustomerMatric() {
        return CustomerMatric;
    }

    public void setCustomerMatric(String customerMatric) {
        CustomerMatric = customerMatric;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getICImageUrl() {
        return ICImageUrl;
    }

    public void setICImageUrl(String ICImageUrl) {
        this.ICImageUrl = ICImageUrl;
    }

    public String getLicenseUrl() {
        return LicenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        LicenseUrl = licenseUrl;
    }

    public String getStatusRequest() {
        return StatusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        StatusRequest = statusRequest;
    }
}
