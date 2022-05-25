package com.example.graduationproject.fragments;

public class MyTitleEventBus {
    private PagesFragment type;
    private String text = "";

    public MyTitleEventBus(PagesFragment type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PagesFragment getType() {
        return type;
    }

    public void setType(PagesFragment type) {
        this.type = type;
    }
}
