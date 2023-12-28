package com.eshan.healthapp.ResponseModels;

public class EmergencyResponse {

    private boolean ok;

    public EmergencyResponse() {

    }

    public EmergencyResponse(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }
}
