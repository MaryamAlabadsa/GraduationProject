
package com.example.graduationproject.retrofit.profile.user.info;

import java.io.Serializable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Data implements Serializable {

    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_address")
    @Expose
    private String userAddress;
    @SerializedName("num_donation_post")
    @Expose
    private String numDonationPost;

    @SerializedName("num_request_post")
    @Expose
    private String numRequestPost;

    private final static long serialVersionUID = 8222112933100028097L;

    /**
     * No args constructor for use in serialization
     */
    public Data() {
    }

    public Data(String userImage, String userName, String numDonationPost, String numRequestPost) {
        this.userImage = userImage;
        this.userName = userName;
        this.numDonationPost = numDonationPost;
        this.numRequestPost = numRequestPost;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumDonationPost() {
        return numDonationPost;
    }

    public void setNumDonationPost(String numDonationPost) {
        this.numDonationPost = numDonationPost;
    }

    public String getNumRequestPost() {
        return numRequestPost;
    }

    public void setNumRequestPost(String numRequestPost) {
        this.numRequestPost = numRequestPost;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
