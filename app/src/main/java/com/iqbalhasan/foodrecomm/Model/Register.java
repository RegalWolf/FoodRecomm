package com.iqbalhasan.foodrecomm.Model;

import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("register")
    private String register;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRegister() {
        return register;
    }
}
