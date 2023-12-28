package com.eshan.healthapp.Models;

import com.google.gson.annotations.SerializedName;

public class DietPlan {

    @SerializedName("plan")
    private String plan;

    @SerializedName("breakfast")
    private String breakfast;

    @SerializedName("lunch")
    private String lunch;

    @SerializedName("dinner")
    private String dinner;

    public DietPlan() {

    }

    public DietPlan(String plan, String breakfast, String lunch, String dinner) {
        this.plan = plan;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public String getPlan() {
        return plan;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public String getDinner() {
        return dinner;
    }
}
