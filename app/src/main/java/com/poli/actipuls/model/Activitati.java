package com.poli.actipuls.model;

import java.util.Date;

public class Activitati {

    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("data")
    private Date data;

    @com.google.gson.annotations.SerializedName("recomandare")
    private String recomandare;

    @com.google.gson.annotations.SerializedName("durata")
    private int durata;

    public Activitati() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getRecomandare() {
        return recomandare;
    }

    public void setRecomandare(String recomandare) {
        this.recomandare = recomandare;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }
}

