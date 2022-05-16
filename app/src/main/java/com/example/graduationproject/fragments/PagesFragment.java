package com.example.graduationproject.fragments;

public enum PagesFragment {
    ADD_POSTS(0), ALL_POSTS(3), POST_ORDERS(4), PROFILE(1), NOTIFICATION(2), CHANGE_PASSWORD(5);

    private int[] value;

    PagesFragment(int... value) {
        this.value = value;
    }


    public static PagesFragment getValue(int value) {
        for (PagesFragment e : PagesFragment.values()) {
            for (int mvalue : e.value)
                if (mvalue == value) {
                    return e;
                }
        }
        return NOTIFICATION;
    }
}
