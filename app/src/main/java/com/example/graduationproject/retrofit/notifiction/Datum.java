
package com.example.graduationproject.retrofit.notifiction;

import java.io.Serializable;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Datum implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("receiver_id")
    @Expose
    private Integer receiverId;
    @SerializedName("sent_at")
    @Expose
    private String sent_at;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("ia_deleted")
    @Expose
    private String isDeleted;
    private final static long serialVersionUID = 2792360874378452260L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Datum() {
    }

    public Datum(Integer id, Integer postId, Integer senderId, Integer receiverId, String sent_at, String type, String senderName) {
        this.id = id;
        this.postId = postId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sent_at = sent_at;
        this.type = type;
        this.senderName = senderName;
    }

    public String getSent_at() {
        return sent_at;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @param id
     * @param postId
     * @param type
     * @param senderName
     */





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
