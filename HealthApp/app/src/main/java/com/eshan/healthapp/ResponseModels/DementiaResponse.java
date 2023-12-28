package com.eshan.healthapp.ResponseModels;

import com.eshan.healthapp.Models.Result;
import com.google.gson.annotations.SerializedName;

public class DementiaResponse {

    @SerializedName("result")
    private Result result;

    public DementiaResponse() {

    }

    public DementiaResponse(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
