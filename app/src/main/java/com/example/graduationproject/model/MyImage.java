package com.example.graduationproject.model;

import android.net.Uri;

public class MyImage {
    String imageUrl;
    Uri imageUri;
    Boolean isUploaded;

    public MyImage(String imageUrl, Boolean isUploaded) {
        this.imageUrl = imageUrl;
        this.isUploaded = isUploaded;
    }

    public MyImage(Uri imageUri, Boolean isUploaded) {
        this.imageUri = imageUri;
        this.isUploaded = isUploaded;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getUploaded() {
        return isUploaded;
    }

    public void setUploaded(Boolean uploaded) {
        isUploaded = uploaded;
    }
}
