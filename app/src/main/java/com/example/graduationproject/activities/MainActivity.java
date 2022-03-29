package com.example.graduationproject.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.adapters.CategoriesAdapter;
import com.example.graduationproject.adapters.ImagesPostsAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.models.Category;
import com.example.graduationproject.models.Post;
import com.example.graduationproject.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Context context = MainActivity.this;
    int category_id;


    boolean isOpen = false; // by default is false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(context, "nnn", Toast.LENGTH_SHORT).show();
        setCategoryRv();
        setPostsRv();
    }

    private void setPostsRv() {
        ArrayList<String> listImages = new ArrayList<>();
        ArrayList<Post> list = new ArrayList<>();
        listImages.add("https://mir-s3-cdn-cf.behance.net/project_modules/1400/ebcdfd44216545.56076cdf0ed14.jpg");
        listImages.add("https://mir-s3-cdn-cf.behance.net/project_modules/1400/ebcdfd44216545.56076cdf0ed14.jpg");
        listImages.add("https://mir-s3-cdn-cf.behance.net/project_modules/1400/ebcdfd44216545.56076cdf0ed14.jpg");
//        list.add(new Post("book", "Book in good condition"
//                , new User("maryam", "Khan Younis", "https://png.pngtree.com/png-vector/20190114/ourlarge/pngtree-vector-add-user-icon-png-image_313043.jpg"
//                , "maryam@hotmail.com", null, null)
//                , 4, 0, 0, 2
//                , listImages));
//        list.add(new Post("book", "Book in good condition"
//                , new User("maryam", "Khan Younis", "https://png.pngtree.com/png-vector/20190114/ourlarge/pngtree-vector-add-user-icon-png-image_313043.jpg"
//                , "maryam@hotmail.com", null, null)
//                , 8, 1, 1, 2
//                , listImages));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        binding.rvPost.setLayoutManager(layoutManager);
        PostsAdapter adapter = new PostsAdapter(context, new PostRequestInterface() {
            @Override
            public void layout(Post post) {

            }
        });
        adapter.setList(list);
        binding.rvPost.setAdapter(adapter);
    }

    private void setCategoryRv() {
        ArrayList<Category> list = new ArrayList<>();
        list.add(new Category(0, "all", ""));
        list.add(new Category(1, "books", ""));
        list.add(new Category(1, "books", ""));
        list.add(new Category(1, "books", ""));
        list.add(new Category(1, "books", ""));
        list.add(new Category(2, "cash", ""));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        CategoriesAdapter adapter = new CategoriesAdapter(context, new CategoryInterface() {
            @Override
            public void layout(int id) {
                category_id = id;
            }
        });
//        adapter.setList(list.get(position).getImagesList());
        binding.rvCategory.setAdapter(adapter);
    }
}