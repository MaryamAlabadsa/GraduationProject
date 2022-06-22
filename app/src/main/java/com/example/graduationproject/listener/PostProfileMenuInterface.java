package com.example.graduationproject.listener;

import android.view.View;

import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.profile.donation.posts.PostsList;

public interface PostProfileMenuInterface {
    void layoutPost(PostsList post, int position, View v);
    void layoutOrder(PostsList post, int position, View v);
}
