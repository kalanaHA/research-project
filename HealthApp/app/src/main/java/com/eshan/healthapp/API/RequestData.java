package com.eshan.healthapp.API;

import com.eshan.healthapp.Models.DietPlan;
import com.eshan.healthapp.Models.RiskStatus;
import com.eshan.healthapp.ResponseModels.DietPlanResponse;
import com.eshan.healthapp.ResponseModels.EmergencyResponse;
import com.eshan.healthapp.ResponseModels.ExercisePlanResponse;
import com.eshan.healthapp.ResponseModels.LatestReadingsResponse;
import com.eshan.healthapp.ResponseModels.LoginResponse;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RequestData {

    @POST("/api/register")
    Call<LoginResponse>getLoginResponse(@Body JsonObject jsonObject);

    @POST("/api/latest-readings")
    Call<List<LatestReadingsResponse>>getLatestReadings(@Body JsonObject jsonObject);

    @GET("/api/diet-plan")
    Call<List<DietPlanResponse>>getDietPlan();

    @GET("/api/risk-history")
    Call<List<RiskStatus>>getRiskStatus();

    @GET("/api/risk-alert")
    Call<EmergencyResponse>callEmergency();

    @GET("/api/exercise-plan")
    Call<List<ExercisePlanResponse>>getExercisePlan();

}
