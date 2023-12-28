package com.eshan.healthapp.Models;

public class RiskStatus {

    private String _id;
    private String risk_level;
    private String created_at;
    private String updated_at;

    public RiskStatus() {

    }

    public RiskStatus(String _id, String risk_level, String created_at, String updated_at) {
        this._id = _id;
        this.risk_level = risk_level;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String get_id() {
        return _id;
    }

    public String getRisk_level() {
        return risk_level;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
