
package com.example.graduationproject.retrofit.profile.user.info;

import java.io.Serializable;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class User implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("Longitude")
    @Expose
    private Object longitude;
    @SerializedName("Latitude")
    @Expose
    private Object latitude;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("email_verified_at")
    @Expose
    private Object emailVerifiedAt;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    private final static long serialVersionUID = -8950617325703496724L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public User() {
    }

    /**
     * 
     * @param imageLink
     * @param address
     * @param phoneNumber
     * @param emailVerifiedAt
     * @param latitude
     * @param name
     * @param id
     * @param email
     * @param longitude
     */
    public User(Integer id, String name, String email, String address, Object longitude, Object latitude, String phoneNumber, Object emailVerifiedAt, String imageLink) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phoneNumber;
        this.emailVerifiedAt = emailVerifiedAt;
        this.imageLink = imageLink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Object emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}
