package com.tryapp.myapplication3.e_carrentums;

public class messej {

    public String ID_Msg;
    public String Message_details;

    public void messej(String ID_Msg, String message_details) {
        this.ID_Msg = ID_Msg;
        Message_details = message_details;
    }

    public String getIDMsg() {
        return ID_Msg;
    }

    public void setIDMsg(String ID_Msg) {
        this.ID_Msg = ID_Msg;
    }

    public String getMessagedetails() {
        return Message_details;
    }

    public void setMessagedetails(String message_details) {
        Message_details = message_details;
    }
}
