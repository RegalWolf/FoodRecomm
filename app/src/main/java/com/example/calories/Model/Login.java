package com.example.calories.Model;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
}
