package com.example.graduationproject.listener;

import android.view.View;

import com.example.graduationproject.retrofit.post.Post;

public interface PostMenuInterface {
    void layout(Post post, int position, View v);
}
