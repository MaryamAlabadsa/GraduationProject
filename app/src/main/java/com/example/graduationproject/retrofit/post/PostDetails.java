package com.example.graduationproject.retrofit.post;

import com.example.graduationproject.retrofit.request.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostDetails implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("orders")
    @Expose
    private List<Data> orders = new ArrayList<Data>();
    private final static long serialVersionUID = 1094002464248124249L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PostDetails() {
    }

    /**
     *
     * @param post
     * @param orders
     * @param message
     */
    public PostDetails(String message, Post post, List<Data> orders) {
        super();
        this.message = message;
        this.post = post;
        this.orders = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Data> getOrders() {
        return orders;
    }

    public void setOrders(List<Data> orders) {
        this.orders = orders;
    }

}