package com.example.calories.Model.Youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet {

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;

    @SerializedName("channelTitle")
    @Expose
    private String channelTitle;

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

}
