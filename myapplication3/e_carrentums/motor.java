package com.tryapp.myapplication3.e_carrentums;

public class motor {
    public String UserMotor_ID;
    public String Motor_id;
    public String MotorcycleName;
    public String MotorTransmission;
    public String MotorPrice;
    public String MotorAvailability;
    public String MotorMatric;
    public String PickupMotor;
    public String ownerMotorNumberPhone;
    public String MotorImageUrl;
    public String MotorPlateNumber;

    public motor() {
    }

    public motor(String usermotor_id,String motor_id,String motorcycleName, String motorTransmission, String motorPrice, String motorAvailability, String motorMatric, String pickupMotor, String ownerMotorNumberPhone, String motorImageUrl, String motorPlateNumber) {
        UserMotor_ID=usermotor_id;
        Motor_id=motor_id;
        MotorcycleName = motorcycleName;
        MotorTransmission = motorTransmission;
        MotorPrice = motorPrice;
        MotorAvailability = motorAvailability;
        MotorMatric = motorMatric;
        PickupMotor = pickupMotor;
        this.ownerMotorNumberPhone = ownerMotorNumberPhone;
        MotorImageUrl = motorImageUrl;
        MotorPlateNumber = motorPlateNumber;
    }
}
