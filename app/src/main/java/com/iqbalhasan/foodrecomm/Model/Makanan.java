package com.iqbalhasan.foodrecomm.Model;

import com.google.gson.annotations.SerializedName;

public class Makanan {

    @SerializedName("makanan_id")
    private int makanan_id;

    @SerializedName("nama")
    private String nama;

    @SerializedName("kalori")
    private int kalori;

    @SerializedName("protein")
    private double protein;

    @SerializedName("lemak")
    private double lemak;

    @SerializedName("karbohidrat")
    private double karbohidrat;

    @SerializedName("kalsium")
    private double kalsium;

    @SerializedName("fosfor")
    private int fosfor;

    @SerializedName("zat_besi")
    private double zat_besi;

    @SerializedName("vit_a")
    private double vit_a;

    @SerializedName("vit_b1")
    private double vit_b1;

    @SerializedName("vit_c")
    private double vit_c;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("prioritas")
    private String prioritas;

    public int getMakanan_id() {
        return makanan_id;
    }

    public String getNama() {
        return nama;
    }

    public int getKalori() {
        return kalori;
    }

    public double getProtein() {
        return protein;
    }

    public double getLemak() {
        return lemak;
    }

    public double getKarbohidrat() {
        return karbohidrat;
    }

    public double getKalsium() {
        return kalsium;
    }

    public int getFosfor() {
        return fosfor;
    }

    public double getZat_besi() {
        return zat_besi;
    }

    public double getVit_a() {
        return vit_a;
    }

    public double getVit_b1() {
        return vit_b1;
    }

    public double getVit_c() {
        return vit_c;
    }

    public String getKategori() {
        return kategori;
    }

    public String getPrioritas() {
        return prioritas;
    }
}
