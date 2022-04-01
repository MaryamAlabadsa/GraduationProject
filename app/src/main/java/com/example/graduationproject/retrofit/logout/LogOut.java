package com.example.graduationproject.retrofit.logout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class LogOut implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 6266186571241842638L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
