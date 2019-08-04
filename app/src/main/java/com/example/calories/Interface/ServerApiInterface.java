package com.example.calories.Interface;

import com.example.calories.Model.HapusMakananDisukai;
import com.example.calories.Model.HapusMakananTidakDisukai;
import com.example.calories.Model.HistoryKalori;
import com.example.calories.Model.HistoryMakanan;
import com.example.calories.Model.Kalori;
import com.example.calories.Model.Login;
import com.example.calories.Model.Makanan;
import com.example.calories.Model.MakananDisukai;
import com.example.calories.Model.MakananTidakDisukai;
import com.example.calories.Model.Profile;
import com.example.calories.Model.Register;
import com.example.calories.Model.SimpanMakanan;
import com.example.calories.Model.SimpanMakananDisukai;
import com.example.calories.Model.SimpanMakananTidakDisukai;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServerApiInterface {

    @GET("api/makanan/rekomendasi/all")
    Call<List<Makanan>> getRekomendasiMakanan(
            @Query("kalori_max") int kalori_max,
            @Header("Authorization") String token
    );

    @GET("api/makanan/rekomendasi/kategori")
    Call<List<Makanan>> getRekomendasiMakananKategori(
            @Query("kalori_max") int kalori_max,
            @Query("kode_kategori") String kode_kategori,
            @Header("Authorization") String token
    );

    @GET("api/makanan/kategori/all")
    Call<List<Makanan>> getMakanan(
            @Header("Authorization") String token
    );

    @GET("api/makanan/kategori")
    Call<List<Makanan>> getMakananKategori(
            @Query("kode_kategori") String kode_kategori,
            @Header("Authorization") String token
    );

    @GET("api/makanan/prioritas")
    Call<List<Makanan>> getMakananPrioritas(
            @Query("prioritas") String prioritas,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("api/users/login")
    Call<Login> postLogin(
        @Field("email") String email,
        @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/users/register")
    Call<Register> postRegister(
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirmPassword") String confirmPassword
    );

    @FormUrlEncoded
    @POST("api/profiles")
    Call<Profile> createProfile(
            @Field("jenis_kelamin") String jenisKelamin,
            @Field("usia") Integer usia,
            @Field("berat_badan") Integer beratBadan,
            @Field("tinggi_badan") Integer tinggiBadan,
            @Field("tingkat_aktivitas") String tingkatAktivitas,
            @Header("Authorization") String token
    );

    @GET("api/profiles")
    Call<Profile> getProfile(
            @Header("Authorization") String token
    );

    @GET("api/kalori")
    Call<Kalori> getKalori(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("api/history_makanan")
    Call<SimpanMakanan> saveMakanan(
            @Field("makanan_id") int makanan_id,
            @Field("jumlah") Integer jumlah,
            @Header("Authorization") String token
    );

    @GET("api/kalori/all")
    Call<List<HistoryKalori>> getHistoryKalori(
            @Header("Authorization") String token
    );

    @GET("api/history_makanan")
    Call<List<HistoryMakanan>> getHistoryMakanan(
            @Query("tanggal") String tanggal,
            @Header("Authorization") String token
    );

    @GET("api/makanan/search")
    Call<List<Makanan>> cariMakanan(
            @Query("nama") String nama,
            @Header("Authorization") String token
    );

    @GET("api/makanan_disukai")
    Call<List<MakananDisukai>> getMakananDisukai(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("api/makanan_disukai")
    Call<SimpanMakananDisukai> simpanMakananDisukai(
            @Field("makanan_id") int makanan_id,
            @Header("Authorization") String token
    );

    @DELETE("api/makanan_disukai")
    Call<HapusMakananDisukai> hapusMakananDisukai(
            @Query("makanan_id") int makanan_id,
            @Header("Authorization") String token
    );

    @GET("api/makanan_tidak_disukai")
    Call<List<MakananTidakDisukai>> getMakananTidakDisukai(
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("api/makanan_tidak_disukai")
    Call<SimpanMakananTidakDisukai> simpanMakananTidakDisukai(
            @Field("makanan_id") int makanan_id,
            @Header("Authorization") String token
    );

    @DELETE("api/makanan_tidak_disukai")
    Call<HapusMakananTidakDisukai> hapusMakananTidakDisukai(
            @Query("makanan_id") int makanan_id,
            @Header("Authorization") String token
    );

}
