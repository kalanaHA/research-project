package com.eshan.healthapp.ResponseModels;

import com.eshan.healthapp.Models.ExercisePlan;
import com.google.gson.annotations.SerializedName;

public class ExercisePlanResponse {

    @SerializedName("_id")
    private String _id;

    @SerializedName("exercise_plan")
    private ExercisePlan exercise_plan;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public ExercisePlanResponse() {

    }

    public ExercisePlanResponse(String _id, ExercisePlan exercise_plan, String created_at, String updated_at) {
        this._id = _id;
        this.exercise_plan = exercise_plan;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String get_id() {
        return _id;
    }

    public ExercisePlan getExercise_plan() {
        return exercise_plan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
