
package com.example.graduationproject.retrofit.post;

import com.example.graduationproject.retrofit.post.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;
@Generated("jsonschema2pojo")
public class AllPosts implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Post> data = null;
    private final static long serialVersionUID = 4954625670901394698L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Post> getData() {
        return data;
    }

    public void setData(List<Post> data) {
        this.data = data;
    }

}