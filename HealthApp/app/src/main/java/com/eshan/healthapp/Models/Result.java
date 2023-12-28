package com.eshan.healthapp.Models;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("message")
    private String message;

    public Result() {
    }

    public Result(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
