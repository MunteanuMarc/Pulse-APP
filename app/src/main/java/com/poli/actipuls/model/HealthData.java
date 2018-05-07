package com.poli.actipuls.model;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

import java.util.Date;

public class HealthData {
    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("puls")
    private String puls;

    @com.google.gson.annotations.SerializedName("accelerometru")
    private String accelerometru;

    @com.google.gson.annotations.SerializedName("alerta")
    private boolean alert;

    @com.google.gson.annotations.SerializedName("id_pacient")
    private String userId;

    public HealthData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPuls() {
        return puls;
    }

    public void setPuls(String puls) {
        this.puls = puls;
    }

    public String getAccelerometru() {
        return accelerometru;
    }

    public void setAccelerometru(String accelerometru) {
        this.accelerometru = accelerometru;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

