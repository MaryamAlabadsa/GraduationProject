
package com.example.graduationproject.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ChangePassResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;

    private final static long serialVersionUID = 2422163769309400492L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
