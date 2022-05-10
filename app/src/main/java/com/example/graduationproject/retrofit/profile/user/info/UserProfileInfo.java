
package com.example.graduationproject.retrofit.profile.user.info;

import java.io.Serializable;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UserProfileInfo implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    private final static long serialVersionUID = -6505443730176066322L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserProfileInfo() {
    }

    /**
     * 
     * @param data
     * @param message
     */
    public UserProfileInfo(String message, Data data) {
        super();
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
