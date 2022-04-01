
package com.example.graduationproject.retrofit.post;

import com.example.graduationproject.retrofit.post.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class AllPosts implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    private final static long serialVersionUID = -5914029478388490072L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public com.example.graduationproject.retrofit.post.Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
