package com.iqbalhasan.foodrecomm.Model.Zomato;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ZomatoApi {

    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = null;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

}
