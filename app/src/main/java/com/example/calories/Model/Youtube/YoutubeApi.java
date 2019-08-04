package com.example.calories.Model.Youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YoutubeApi {

    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;

    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Item> getItems() {
        return items;
    }

}
