package com.example.graduationproject.fragments;

import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.post.Post;

public interface FragmentSwitcher {
    void switchFragment(PagesFragment pagesFragment, PostOrdersInfo object, String post);

}
