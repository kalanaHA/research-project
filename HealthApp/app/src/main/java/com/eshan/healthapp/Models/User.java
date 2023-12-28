package com.eshan.healthapp.Models;

public class User {

    private String name;
    private String address;
    private String bloodType;
    private String healthStatus;

    private String phoneNumber;

    public User() {

    }

    public User(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public User(String name, String address, String bloodType, String healthStatus) {
        this.name = name;
        this.address = address;
        this.bloodType = bloodType;
        this.healthStatus = healthStatus;
    }

    public User(String name, String address, String bloodType, String healthStatus, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.bloodType = bloodType;
        this.healthStatus = healthStatus;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
