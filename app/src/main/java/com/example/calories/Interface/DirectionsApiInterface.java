package com.example.calories.Interface;

import com.example.calories.Model.Maps.Maps;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionsApiInterface {

    @GET("directions/json")
    Call<Maps> getMaps(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );
}
