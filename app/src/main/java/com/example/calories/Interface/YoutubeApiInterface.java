package com.example.calories.Interface;

import com.example.calories.Model.Youtube.YoutubeApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApiInterface {

    @GET("search")
    Call<YoutubeApi> getVideo(
            @Query("part") String part,
            @Query("q") String query,
            @Query("maxResults") int maxResult,
            @Query("key") String key
    );

}
