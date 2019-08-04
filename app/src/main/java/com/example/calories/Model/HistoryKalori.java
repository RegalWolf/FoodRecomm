package com.example.calories.Model;

import com.google.gson.annotations.SerializedName;

public class HistoryKalori {

    @SerializedName("history_kalori_id")
    private String id;

    @SerializedName("kalori_dibutuhkan")
    private String kalori_dibutuhkan;

    @SerializedName("kalori_dikonsumsi")
    private String kalori_dikonsumsi;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("kondisi_tubuh")
    private String kondisi_tubuh;

    @SerializedName("pengguna_id")
    private String user_id;

    public String getId() {
        return id;
    }

    public String getKalori_dibutuhkan() {
        return kalori_dibutuhkan;
    }

    public String getKalori_dikonsumsi() {
        return kalori_dikonsumsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKondisi_tubuh() {
        return kondisi_tubuh;
    }

    public String getUser_id() {
        return user_id;
    }
}
