package com.tryapp.myapplication3.e_carrentums;

public class Kereta {
    public String Host_ID;
    public String User_ID;
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

    public Kereta(String user_id,String host_id, String brand, String carName, String transmission, String seats, String fuelType, String price, String availability, String carMatric, String pickupCar, String numberPhone, String imageUrl, String plateNumber) {
        User_ID=user_id;
        Host_ID=host_id;
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

    public Kereta() {
    }
}
