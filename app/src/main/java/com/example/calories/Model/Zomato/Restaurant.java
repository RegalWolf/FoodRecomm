package com.example.calories.Model.Zomato;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("restaurant")
    @Expose
    private Restaurant_ restaurant;

    public Restaurant_ getRestaurant() {
        return restaurant;
    }

}
