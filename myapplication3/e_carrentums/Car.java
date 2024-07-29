package com.tryapp.myapplication3.e_carrentums;

public class Car {

    public String User_ID;
    public String Host_ID;
    public String Brand;
    public String CarName;
    public String Transmission;
    public String Seats;
    public String FuelType;
    public String Price;
    public String Availability;
    public String carMatric;
    public String PickupCar;
    public String NumberPhone;
    public String ImageUrl;
    public String PlateNumber;


    public Car(String user_id,String host_id, String brand, String carName, String transmission, String seats, String fuelType, String price, String availability, String carMatric, String pickupCar, String numberPhone, String imageUrl, String plateNumber) {

        User_ID=user_id;
        Host_ID = host_id;
        Brand = brand;
        CarName = carName;
        Transmission = transmission;
        Seats = seats;
        FuelType = fuelType;
        Price = price;
        Availability = availability;
        this.carMatric = carMatric;
        PickupCar = pickupCar;
        NumberPhone = numberPhone;
        ImageUrl = imageUrl;
        PlateNumber=plateNumber;
    }

    public String getuser_identity(){
        return User_ID;
    }

    public void setUser_identity(String user_ID) {
        User_ID = user_ID;
    }

    public String getuid_host() {
        return Host_ID;
    }

    public void setHost_punya_uid(String host_uid) {
        Host_ID = host_uid;
    }

    public String getProduct() {
        return Brand;

    }

    public void setproduct(String brand) {
        Brand = brand;
    }

    public String getNamaKereta() {
        return CarName;
    }

    public void SetNamaKereta(String carName) {
        CarName = carName;
    }

    public String getJenisGiar() {
        return Transmission;
    }

    public void SetJenisGiar(String transmission) {
        Transmission = transmission;
    }

    public String getJenisTempatDuduk() {
        return Seats;
    }

    public void setJenisTempatDuduk(String seats) {
        Seats = seats;
    }

    public String getJenisMinyak() {
        return FuelType;
    }

    public void SetJenisMinyak(String fuelType) {
        FuelType = fuelType;
    }

    public String getMauHarga() {
        return Price;
    }

    public void setHarga(String price) {
        Price = price;
    }

    public String getStatus_kereta() {
        return Availability;
    }

    public void setStatus_kereta(String statusCar) {
        Availability = statusCar;
    }

    public String getNomborMatrik() {
        return carMatric;
    }

    public void setnomborMatrik(String carMatric) {
        this.carMatric = carMatric;
    }

    public String getTempatAmbilKereta() {
        return PickupCar;
    }

    public void setAmbilKereta(String pickupCar) {
        PickupCar = pickupCar;
    }

    public String getNombortelepon() {
        return NumberPhone;
    }

    public void setNombortelepon(String numberPhone) {
        NumberPhone = numberPhone;
    }

    public String getGambar() {
        return ImageUrl;
    }

    public void setGambar(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Car(){

    }

    public String mauflatnombor() {
        return PlateNumber;
    }

    public void setNomborFlat(String plateNumber) {
        PlateNumber = plateNumber;
    }
}


