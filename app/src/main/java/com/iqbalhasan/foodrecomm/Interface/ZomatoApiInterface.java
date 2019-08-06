package com.iqbalhasan.foodrecomm.Interface;

import com.iqbalhasan.foodrecomm.Model.Zomato.ZomatoApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.iqbalhasan.foodrecomm.Config.Config.ZOMATO_KEY;

public interface ZomatoApiInterface {

    @Headers("user-key: " + ZOMATO_KEY)
    @GET("search")
    Call<ZomatoApi> getRestoran(
            @Query("q") String query,
            @Query("lat") double lattitude,
            @Query("lon") double longitude,
            @Query("sort") String sort,
            @Query("order") String order
    );

}
