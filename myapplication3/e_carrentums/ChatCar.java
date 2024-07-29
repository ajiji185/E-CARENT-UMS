package com.tryapp.myapplication3.e_carrentums;

public class ChatCar {
    public String PK_Host;
    public String PK_Renter;
    public String PK_HostName;
    public String PK_RenterName;


    public String PK_Message;

    public ChatCar() {
    }

    public ChatCar(String PK_Message, String PK_Host, String PK_Renter, String PK_HostName, String PK_RenterName) {
        this.PK_Message=PK_Message;
        this.PK_Host = PK_Host;
        this.PK_Renter = PK_Renter;
        this.PK_HostName = PK_HostName;
        this.PK_RenterName = PK_RenterName;

    }

    public String maupkmessage() {
        return PK_Message;
    }

    public void setsetPKMessage(String PK_Message) {
        this.PK_Message = PK_Message;
    }

    public String maupkHost() {
        return PK_Host;
    }

    public void setPKHost(String PK_Host) {
        this.PK_Host = PK_Host;
    }

    public String maupkrenter() {
        return PK_Renter;
    }

    public void setPKRenter(String PK_Renter) {
        this.PK_Renter = PK_Renter;
    }

    public String mauhostname() {
        return PK_HostName;
    }

    public void setPKHostName(String PK_HostName) {
        this.PK_HostName = PK_HostName;
    }

    public String getchatdetails() {
        return PK_RenterName;
    }

    public void setPKRenterName(String PK_RenterName) {
        this.PK_RenterName = PK_RenterName;
    }


}
