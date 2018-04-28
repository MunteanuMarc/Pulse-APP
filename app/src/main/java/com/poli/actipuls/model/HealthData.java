package com.poli.actipuls.model;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

import java.util.Date;

public class HealthData {
    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("Puls")
    private String puls;

    @com.google.gson.annotations.SerializedName("Accelerometru")
    private String accelerometru;

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
}

