
package com.example.graduationproject.retrofit.post;

import com.example.graduationproject.retrofit.categories.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Data implements Serializable
{

    @SerializedName("category")
    @Expose
    private List<Post> Post = null;
    private final static long serialVersionUID = -6669401469831123281L;

    public List<com.example.graduationproject.retrofit.post.Post> getPost() {
        return Post;
    }

    public void setPost(List<com.example.graduationproject.retrofit.post.Post> post) {
        Post = post;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
