package com.iqbalhasan.foodrecomm.Model;

import com.google.gson.annotations.SerializedName;

public class SimpanMakanan {

    @SerializedName("makanan")
    private String makanan;

    @SerializedName("kalori")
    private String kalori;

    public String getMakanan() {
        return makanan;
    }

    public String getKalori() {
        return kalori;
    }
}
