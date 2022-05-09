package com.example.graduationproject.model;

import java.util.List;

public class SliderItem {
    String title,Description;
    List<String> imageList;

    public SliderItem(String title, String description, List<String> imageList) {
        this.title = title;
        Description = description;
        this.imageList = imageList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
