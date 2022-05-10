
package com.example.graduationproject.retrofit.profile.user.info;

import java.io.Serializable;
import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Data implements Serializable
{

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("num_donation_post")
    @Expose
    private Integer numDonationPost;
    @SerializedName("num_request_post")
    @Expose
    private Integer numRequestPost;
    private final static long serialVersionUID = 8222112933100028097L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param numDonationPost
     * @param user
     * @param numRequestPost
     */
    public Data(User user, Integer numDonationPost, Integer numRequestPost) {
        super();
        this.user = user;
        this.numDonationPost = numDonationPost;
        this.numRequestPost = numRequestPost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNumDonationPost() {
        return numDonationPost;
    }

    public void setNumDonationPost(Integer numDonationPost) {
        this.numDonationPost = numDonationPost;
    }

    public Integer getNumRequestPost() {
        return numRequestPost;
    }

    public void setNumRequestPost(Integer numRequestPost) {
        this.numRequestPost = numRequestPost;
    }

}
