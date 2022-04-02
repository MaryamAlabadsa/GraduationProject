package com.example.graduationproject.retrofit.post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Post implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_donation")
    @Expose
    private Boolean isDonation;
    @SerializedName("number_of_requests")
    @Expose
    private Integer numberOfRequests;
    @SerializedName("post_first_user")
    @Expose
    private String postFirstUser;
    @SerializedName("post_second_user")
    @Expose
    private String postSecondUser;
    @SerializedName("post_first_user_email")
    @Expose
    private String postFirstUserEmail;
    @SerializedName("post_second_user_email")
    @Expose
    private String postSecondUserEmail;
    @SerializedName("post_media")
    @Expose
    private List<String> postMedia = null;
    @SerializedName("first_user_image_link")
    @Expose
    private String firstUserImageLink;
    private final static long serialVersionUID = 2964213240315325669L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDonation() {
        return isDonation;
    }

    public void setIsDonation(Boolean isDonation) {
        this.isDonation = isDonation;
    }

    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Integer numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public String getPostFirstUser() {
        return postFirstUser;
    }

    public void setPostFirstUser(String postFirstUser) {
        this.postFirstUser = postFirstUser;
    }

    public String getPostSecondUser() {
        return postSecondUser;
    }

    public void setPostSecondUser(String postSecondUser) {
        this.postSecondUser = postSecondUser;
    }

    public String getPostFirstUserEmail() {
        return postFirstUserEmail;
    }

    public void setPostFirstUserEmail(String postFirstUserEmail) {
        this.postFirstUserEmail = postFirstUserEmail;
    }

    public String getPostSecondUserEmail() {
        return postSecondUserEmail;
    }

    public void setPostSecondUserEmail(String postSecondUserEmail) {
        this.postSecondUserEmail = postSecondUserEmail;
    }

    public List<String> getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(List<String> postMedia) {
        this.postMedia = postMedia;
    }

    public String getFirstUserImageLink() {
        return firstUserImageLink;
    }

    public void setFirstUserImageLink(String firstUserImageLink) {
        this.firstUserImageLink = firstUserImageLink;
    }

}