package com.eshan.healthapp.Models;

import com.google.gson.annotations.SerializedName;

public class ExercisePlan {

    @SerializedName("plan")
    private String plan;

    @SerializedName("exercises")
    private String exercises;

    public ExercisePlan() {

    }

    public ExercisePlan(String plan, String exercises) {
        this.plan = plan;
        this.exercises = exercises;
    }

    public String getPlan() {
        return plan;
    }

    public String getExercises() {
        return exercises;
    }
}
