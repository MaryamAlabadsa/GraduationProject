
package com.example.graduationproject.retrofit.post;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@Generated("jsonschema2pojo")
public class Post {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Integer id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "isDonation")
    @SerializedName("is_donation")
    @Expose
    private Boolean isDonation;

    @ColumnInfo(name = "categoryId")
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;

    @ColumnInfo(name = "firstUserId")
    @SerializedName("first_user_id")
    @Expose
    private Integer firstUserId;

    @ColumnInfo(name = "secondUserId")
    @SerializedName("second_user_id")
    @Expose
    private Integer secondUserId;

    @ColumnInfo(name = "categoryName")
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    @ColumnInfo(name = "numberOfRequests")
    @SerializedName("number_of_requests")
    @Expose
    private Integer numberOfRequests;

    @ColumnInfo(name = "firstUserName")
    @SerializedName("first_user_name")
    @Expose
    private String firstUserName;

    @ColumnInfo(name = "secondUserName")
    @SerializedName("second_user_name")
    @Expose
    private String secondUserName;

    @ColumnInfo(name = "postFirstUserEmail")
    @SerializedName("post_first_user_email")
    @Expose
    private String postFirstUserEmail;

    @ColumnInfo(name = "postSecondUserEmail")
    @SerializedName("post_second_user_email")
    @Expose
    private String postSecondUserEmail;

//    @ColumnInfo(name = "postMedia")
    @SerializedName("post_media")
    @Expose
    private List<String> postMedia = new ArrayList<String>();

    @ColumnInfo(name = "firstUserImageLink")
    @SerializedName("first_user_image_link")
    @Expose
    private String firstUserImageLink;

    @ColumnInfo(name = "isOrdered")
    @SerializedName("is_ordered")
    @Expose
    private Boolean isOrdered;

    @ColumnInfo(name = "orderId")
    @SerializedName("Order_id")
    @Expose
    private Integer orderId;

    @ColumnInfo(name = "isHeTheOwnerOfThePost")
    @SerializedName("is_he_the_owner_of_the_post")
    @Expose
    private Boolean isHeTheOwnerOfThePost;

    @ColumnInfo(name = "isCompleted")
    @SerializedName("is_completed")
    @Expose
    private Boolean isCompleted;

    @ColumnInfo(name = "publishedAt")
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    private final static long serialVersionUID = -9076844435418703799L;

    /**
     * No args constructor for use in serialization
     */
    public Post() {
    }

    public Post(Integer id, String title, String description, Boolean isDonation, Integer categoryId, Integer firstUserId, Integer secondUserId, String categoryName, Integer numberOfRequests, String firstUserName, String secondUserName, String postFirstUserEmail, String postSecondUserEmail, List<String> postMedia, String firstUserImageLink, Boolean isOrdered, Integer orderId, Boolean isHeTheOwnerOfThePost, Boolean isCompleted, String publishedAt) {
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
        this.orderId = orderId;
        this.isHeTheOwnerOfThePost = isHeTheOwnerOfThePost;
        this.isCompleted = isCompleted;
        this.publishedAt = publishedAt;
    }

    /**
     * @param secondUserId
     * @param secondUserName
     * @param orderId
     * @param postSecondUserEmail
     * @param description
     * @param title
     * @param categoryName
     * @param postFirstUserEmail
     * @param firstUserId
     * @param isHeTheOwnerOfThePost
     * @param numberOfRequests
     * @param firstUserName
     * @param postMedia
     * @param id
     * @param isOrdered
     * @param categoryId
     * @param isDonation
     * @param firstUserImageLink
     * @param isCompleted
     */

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

    public Boolean getIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(Boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}