package com.eshan.healthapp.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class LatestReadingsResponse {

    @SerializedName("HeartRate")
    private int HeartRate;

    @SerializedName("BloodOxygenLevel")
    private int BloodOxygenLevel;

    @SerializedName("BodyTemperature")
    private double  BodyTemperature;

    @SerializedName("created_time")
    private String created_time;

    public LatestReadingsResponse() {

    }

    public LatestReadingsResponse(int heartRate, int bloodOxygenLevel, double  bodyTemperature, String created_time) {
        HeartRate = heartRate;
        BloodOxygenLevel = bloodOxygenLevel;
        BodyTemperature = bodyTemperature;
        this.created_time = created_time;
    }

    public int getHeartRate() {
        return HeartRate;
    }

    public int getBloodOxygenLevel() {
        return BloodOxygenLevel;
    }

    public double  getBodyTemperature() {
        return BodyTemperature;
    }

    public String getCreated_time() {
        return created_time;
    }
}
