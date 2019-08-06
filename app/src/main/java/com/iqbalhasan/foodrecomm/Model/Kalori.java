package com.iqbalhasan.foodrecomm.Model;

import com.google.gson.annotations.SerializedName;

public class Kalori {

    @SerializedName("kalori_dibutuhkan")
    private int kaloriDibutuhkan;

    @SerializedName("kalori_dikonsumsi")
    private int kaloriDikonsumsi;

    @SerializedName("kondisi_tubuh")
    private String kondisiTubuh;

    public int getKaloriDibutuhkan() {
        return kaloriDibutuhkan;
    }

    public int getKaloriDikonsumsi() {
        return kaloriDikonsumsi;
    }

    public String getKondisiTubuh() {
        return kondisiTubuh;
    }
}
