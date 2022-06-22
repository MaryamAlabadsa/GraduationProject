package com.example.graduationproject.utils;

public enum Constant {
    ALL_CHECKED(2),DONATION_CHECKED(1),REQUEST_CHECKED(0),NO_SELECTED_CATEGORY(0)
    ,SELECTED_CATEGORY(4),PENDING_POST(6),COMPLETE_POST(7),EMPTY_CHOICE(10);
    private int[] value;

    Constant(int... value) {
        this.value = value;
    }

    }
