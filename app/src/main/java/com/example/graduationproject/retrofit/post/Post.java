
package com.example.graduationproject.retrofit.post;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Post {


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
    @SerializedName("Order_id")
    @Expose
    private Integer orderId;
    @SerializedName("is_he_the_owner_of_the_post")
    @Expose
    private Boolean isHeTheOwnerOfThePost;
    @SerializedName("is_completed")
    @Expose
    private Boolean isCompleted;
    private final static long serialVersionUID = -9076844435418703799L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Post() {
    }

    /**
     *
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
    public Post(Integer id, String title, String description, Boolean isDonation, Integer categoryId, Integer firstUserId, Integer secondUserId, String categoryName, Integer numberOfRequests, String firstUserName, String secondUserName, Object postFirstUserEmail, String postSecondUserEmail, List<String> postMedia, String firstUserImageLink, Boolean isOrdered, Integer orderId, Boolean isHeTheOwnerOfThePost, Boolean isCompleted) {
        super();
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

}