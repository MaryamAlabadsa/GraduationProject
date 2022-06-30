
package com.example.graduationproject.retrofit.notifiction;

import java.io.Serializable;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Datum implements Serializable
{


    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("receiver_id")
    @Expose
    private Integer receiverId;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("sent_time")
    @Expose
    private String sentTime;
    private final static long serialVersionUID = 1714189985170398041L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Datum() {
    }

    /**
     *
     * @param senderId
     * @param senderName
     * @param receiverId
     * @param sentTime
     * @param postId
     * @param id
     * @param type
     */
    public Datum(Integer postId, Integer id, Integer senderId, String type, Integer receiverId, String senderName, String sentTime) {
        super();
        this.postId = postId;
        this.id = id;
        this.senderId = senderId;
        this.type = type;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.sentTime = sentTime;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }
}
