package com.example.graduationproject.retrofit.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendDeviceTokenResponse {

    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 5384784508736413154L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
