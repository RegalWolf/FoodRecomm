package com.iqbalhasan.foodrecomm.Model.Youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private Id id;

    @SerializedName("snippet")
    @Expose
    private Snippet snippet;

    public Id getId() {
        return id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

}
