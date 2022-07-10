package com.example.graduationproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.graduationproject.retrofit.register.User;

import java.util.Set;


public class AppSharedPreferences {

    private final static String SHARED_FILE = "_dataFile";


    private SharedPreferences pref;


    public static String USER = "user";
    public static String DEVICE_TOKEN = "Device token";
    public static String AUTHENTICATION = "authentication";
    public static String LANG = "lang";


    public AppSharedPreferences(Context context) {
        pref = context.getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE);
    }


    public void writeString(String key, String value) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();

    }

    public String readString(String key) {
        return pref.getString(key, "");
    }

    public void writeUser(String key, String value) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();

    }

    public String readUser(String key) {
        return pref.getString(key, "");
    }


    public void writeLong(String key, Long value) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putLong(key, value);
        prefEditor.commit();

    }

    public long readLong(String key) {
        return pref.getLong(key, 0);
    }

//    public void writeLong(String key, Set<User> value) {
//
//        SharedPreferences.Editor prefEditor = pref.edit();
//        prefEditor.putStringSet(key, value);
//        prefEditor.commit();
//
//    }
//
//    public long readLong(String key) {
//        return pref.getLong(key, 0);
//    }


    public void writeInt(String key, int value) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putInt(key, value);
        prefEditor.commit();

    }

    public int readInt(String key) {
        return pref.getInt(key, 0);
    }


    public int readIntSetting(String key) {
        return pref.getInt(key, 3);
    }


    public void writeBoolean(String key, boolean value) {

        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.commit();

    }

    public boolean readBoolean(String key) {
        return pref.getBoolean(key, false);
    }


    public boolean readBooleanSetting(String key) {
        return pref.getBoolean(key, true);
    }


    public void clear() {
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.commit();

    }


}
