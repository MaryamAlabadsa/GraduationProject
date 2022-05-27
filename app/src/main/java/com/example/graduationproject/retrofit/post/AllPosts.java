
package com.example.graduationproject.retrofit.post;

import com.example.graduationproject.retrofit.post.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class AllPosts implements Serializable {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Post> data = new ArrayList<Post>();
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("meta")
    @Expose
    private Meta meta;
    private final static long serialVersionUID = -3246425651214902772L;

    /**
     * No args constructor for use in serialization
     */
    public AllPosts() {
    }

    /**
     * @param data
     * @param meta
     * @param links
     * @param message
     */
    public AllPosts(String message, List<Post> data, Links links, Meta meta) {
        super();
        this.message = message;
        this.data = data;
        this.links = links;
        this.meta = meta;
    }

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

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}