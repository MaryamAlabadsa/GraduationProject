package com.example.graduationproject.Spinner;

import java.io.Serializable;

public class CategoriesSpinner implements Serializable {
    private String categories;
    private int image;

    public CategoriesSpinner() {

    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
