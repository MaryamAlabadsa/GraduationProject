package com.example.graduationproject.model;

public class PostOrdersInfo {
    boolean isCompleted,isDonation;
    int secondUser,postId;

    public PostOrdersInfo( int postId,boolean isCompleted, boolean isDonation, int secondUser) {
        this.isCompleted = isCompleted;
        this.isDonation = isDonation;
        this.secondUser = secondUser;
        this.postId = postId;
    }

    public boolean isDonation() {
        return isDonation;
    }

    public void setDonation(boolean donation) {
        isDonation = donation;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(int secondUser) {
        this.secondUser = secondUser;
    }
}
