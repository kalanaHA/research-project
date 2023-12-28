package com.eshan.healthapp.ResponseModels;

import com.eshan.healthapp.Models.DietPlan;
import com.google.gson.annotations.SerializedName;

public class DietPlanResponse {

    @SerializedName("_id")
    private String _id;

    @SerializedName("diet_plan")
    private DietPlan diet_plan;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public DietPlanResponse() {

    }

    public DietPlanResponse(String _id, DietPlan diet_plan, String created_at, String updated_at) {
        this._id = _id;
        this.diet_plan = diet_plan;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String get_id() {
        return _id;
    }

    public DietPlan getDiet_plan() {
        return diet_plan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
