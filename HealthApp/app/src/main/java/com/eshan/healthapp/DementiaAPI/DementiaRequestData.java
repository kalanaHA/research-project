package com.eshan.healthapp.DementiaAPI;

import com.eshan.healthapp.ResponseModels.DementiaResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DementiaRequestData {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/perform-inference")
    Call<DementiaResponse> getPrediction(
            @Field("Diabetic") int Diabetic,
            @Field("Alcohol_Level") float Alcohol_Level,
            @Field("Weight") float Weight,
            @Field("MRI_Delay") float MRI_Delay);

}
