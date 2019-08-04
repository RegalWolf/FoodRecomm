package com.example.calories.Model.Zomato;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant_ {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("location")
    @Expose
    private Location location;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Location getLocation() {
        return location;
    }

}
