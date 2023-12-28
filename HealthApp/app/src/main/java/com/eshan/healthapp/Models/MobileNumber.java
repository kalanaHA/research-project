package com.eshan.healthapp.Models;

public class MobileNumber {

    private int id;
    private String mobileNumber;

    public MobileNumber() {

    }

    public MobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public MobileNumber(int id, String mobileNumber) {
        this.id = id;
        this.mobileNumber = mobileNumber;
    }

    public int getId() {
        return id;
    }



    public String getMobileNumber() {
        return mobileNumber;
    }

}
