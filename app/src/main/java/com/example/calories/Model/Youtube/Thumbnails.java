package com.example.calories.Model.Youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("default")
    @Expose
    private Default _default;

    @SerializedName("medium")
    @Expose
    private Medium medium;

    @SerializedName("high")
    @Expose
    private High high;

    public Default getDefault() {
        return _default;
    }

    public Medium getMedium() {
        return medium;
    }

    public High getHigh() {
        return high;
    }

}
