package com.iqbalhasan.foodrecomm.Model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("jenis_kelamin")
    private String jenis_kelamin;

    @SerializedName("usia")
    private String usia;

    @SerializedName("berat_badan")
    private String berat_badan;

    @SerializedName("tinggi_badan")
    private String tinggi_badan;

    @SerializedName("tingkat_aktivitas")
    private String tingkat_aktivitas;

    @SerializedName("profile")
    private String profile;

    @SerializedName("kalori")
    private String kalori;

    @SerializedName("noprofile")
    private String noProfile;

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getUsia() {
        return usia;
    }

    public String getBerat_badan() {
        return berat_badan;
    }

    public String getTinggi_badan() {
        return tinggi_badan;
    }

    public String getTingkat_aktivitas() {
        return tingkat_aktivitas;
    }

    public String getProfile() {
        return profile;
    }

    public String getKalori() {
        return kalori;
    }

    public String getNoProfile() {
        return noProfile;
    }
}
