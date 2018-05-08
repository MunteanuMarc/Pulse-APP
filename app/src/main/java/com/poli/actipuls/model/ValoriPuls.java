package com.poli.actipuls.model;

import java.util.Date;

public class ValoriPuls {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("puls_minim")
    private int pulsMIn;

    @com.google.gson.annotations.SerializedName("puls_maxim")
    private int pulsMax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPulsMIn() {
        return pulsMIn;
    }

    public void setPulsMIn(int pulsMIn) {
        this.pulsMIn = pulsMIn;
    }

    public int getPulsMax() {
        return pulsMax;
    }

    public void setPulsMax(int pulsMax) {
        this.pulsMax = pulsMax;
    }
}
