
package com.example.graduationproject.retrofit.profile.donation.posts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Data implements Serializable
{

    @SerializedName("postsList")
    @Expose
    private List<PostsList> postsList = new ArrayList<PostsList>();
    private final static long serialVersionUID = -7355531265131955522L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param postsList
     */
    public Data(List<PostsList> postsList) {
        super();
        this.postsList = postsList;
    }

    public List<PostsList> getPostsList() {
        return postsList;
    }

    public void setPostsList(List<PostsList> postsList) {
        this.postsList = postsList;
    }

}
