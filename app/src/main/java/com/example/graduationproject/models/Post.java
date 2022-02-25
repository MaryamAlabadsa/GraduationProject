package com.example.graduationproject.models;

import java.util.ArrayList;

public class Post {
    private String title, description;
    private User user;
    private int number_of_request, is_donation,is_available,category_id;
    private ArrayList<String> imagesList;

    public Post(String title, String description, User user, int number_of_request, int is_donation, int is_available, int category_id, ArrayList<String> imagesList) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.number_of_request = number_of_request;
        this.is_donation = is_donation;
        this.is_available = is_available;
        this.category_id = category_id;
        this.imagesList = imagesList;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumber_of_request() {
        return number_of_request;
    }

    public void setNumber_of_request(int number_of_request) {
        this.number_of_request = number_of_request;
    }

    public int getIs_donation() {
        return is_donation;
    }

    public void setIs_donation(int is_donation) {
        this.is_donation = is_donation;
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }
}
