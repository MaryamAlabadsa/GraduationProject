package com.example.graduationproject.models;

public class User {
    private String u_name,u_address,u_img,u_email,u_longitude,u_latitude;

    public User(String u_name, String u_address, String u_img, String u_email, String u_longitude, String u_latitude) {
        this.u_name = u_name;
        this.u_address = u_address;
        this.u_img = u_img;
        this.u_email = u_email;
        this.u_longitude = u_longitude;
        this.u_latitude = u_latitude;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getU_img() {
        return u_img;
    }

    public void setU_img(String u_img) {
        this.u_img = u_img;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_longitude() {
        return u_longitude;
    }

    public void setU_longitude(String u_longitude) {
        this.u_longitude = u_longitude;
    }

    public String getU_latitude() {
        return u_latitude;
    }

    public void setU_latitude(String u_latitude) {
        this.u_latitude = u_latitude;
    }
}
