
package com.example.graduationproject.retrofit.profile.donation.posts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.example.graduationproject.retrofit.post.Post;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class PostsList implements Serializable {


    @SerializedName("status")
    @Expose
    private Integer status;
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
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("first_user_id")
    @Expose
    private Integer firstUserId;
    @SerializedName("second_user_id")
    @Expose
    private Integer secondUserId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("number_of_requests")
    @Expose
    private Integer numberOfRequests;
    @SerializedName("first_user_name")
    @Expose
    private String firstUserName;
    @SerializedName("second_user_name")
    @Expose
    private String secondUserName;
    @SerializedName("post_first_user_email")
    @Expose
    private Object postFirstUserEmail;
    @SerializedName("post_second_user_email")
    @Expose
    private String postSecondUserEmail;
    @SerializedName("post_media")
    @Expose
    private List<String> postMedia = new ArrayList<String>();
    @SerializedName("first_user_image_link")
    @Expose
    private String firstUserImageLink;
    @SerializedName("is_ordered")
    @Expose
    private Boolean isOrdered;
    @SerializedName("is_he_the_owner_of_the_post")
    @Expose
    private Boolean isHeTheOwnerOfThePost;
    @SerializedName("is_completed")
    @Expose
    private Boolean isCompleted;
    @SerializedName("massage")
    @Expose
    private String massage;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("user_phone_number")
    @Expose
    private String userPhoneNumber;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("post")
    @Expose
    private Post post;
    private final static long serialVersionUID = -7964658604594063691L;

    /**
     * No args constructor for use in serialization
     */
    public PostsList() {
    }

    public PostsList(Integer status, Integer id, String title, String description, Boolean isDonation, Integer categoryId, Integer firstUserId, Integer secondUserId, String categoryName, Integer numberOfRequests, String firstUserName, String secondUserName, Object postFirstUserEmail, String postSecondUserEmail, List<String> postMedia, String firstUserImageLink, Boolean isOrdered, Boolean isHeTheOwnerOfThePost, Boolean isCompleted, String massage, Integer postId, Integer userId, String userName, String userImage, String createdAt, String userPhoneNumber, String publishedAt, Post post) {
        this.status = status;
        this.id = id;
        this.title = title;
        this.description = description;
        this.isDonation = isDonation;
        this.categoryId = categoryId;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.categoryName = categoryName;
        this.numberOfRequests = numberOfRequests;
        this.firstUserName = firstUserName;
        this.secondUserName = secondUserName;
        this.postFirstUserEmail = postFirstUserEmail;
        this.postSecondUserEmail = postSecondUserEmail;
        this.postMedia = postMedia;
        this.firstUserImageLink = firstUserImageLink;
        this.isOrdered = isOrdered;
        this.isHeTheOwnerOfThePost = isHeTheOwnerOfThePost;
        this.isCompleted = isCompleted;
        this.massage = massage;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.createdAt = createdAt;
        this.userPhoneNumber = userPhoneNumber;
        this.publishedAt = publishedAt;
        this.post = post;
    }

    /**
     * @param secondUserId
     * @param postSecondUserEmail
     * @param userPhoneNumber
     * @param description
     * @param title
     * @param categoryName
     * @param createdAt
     * @param userImage
     * @param firstUserId
     * @param isHeTheOwnerOfThePost
     * @param post
     * @param firstUserName
     * @param id
     * @param massage
     * @param isOrdered
     * @param isDonation
     * @param isCompleted
     * @param secondUserName
     * @param postId
     * @param userName
     * @param postFirstUserEmail
     * @param userId
     * @param numberOfRequests
     * @param postMedia
     * @param categoryId
     * @param status
     * @param firstUserImageLink
     */


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(Integer firstUserId) {
        this.firstUserId = firstUserId;
    }

    public Integer getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(Integer secondUserId) {
        this.secondUserId = secondUserId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Integer numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName;
    }

    public String getSecondUserName() {
        return secondUserName;
    }

    public void setSecondUserName(String secondUserName) {
        this.secondUserName = secondUserName;
    }

    public Object getPostFirstUserEmail() {
        return postFirstUserEmail;
    }

    public void setPostFirstUserEmail(Object postFirstUserEmail) {
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

    public Boolean getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(Boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public Boolean getIsHeTheOwnerOfThePost() {
        return isHeTheOwnerOfThePost;
    }

    public void setIsHeTheOwnerOfThePost(Boolean isHeTheOwnerOfThePost) {
        this.isHeTheOwnerOfThePost = isHeTheOwnerOfThePost;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}