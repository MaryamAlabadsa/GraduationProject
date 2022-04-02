package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.PostItemBinding;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.retrofit.post.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    Context context;
    List<Post> list;
    PostRequestInterface postRequestInterface;

    public PostsAdapter(Context context, PostRequestInterface postRequestInterface) {
        this.context = context;
        this.postRequestInterface = postRequestInterface;
    }

    public void setList(List<Post> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        PostItemBinding binding;

        public MyViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public PostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemBinding binding = PostItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(binding);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.binding.titlePost.setText("" + list.get(position).getTitle());
//        holder.binding.uNamePost.setText("number of request" + list.get(position).getPostFirstUser());
        holder.binding.descriptionPost.setText("" + list.get(position).getDescription());
        holder.binding.numberRequestsPost.setText("number of request =  " + list.get(position).getNumberOfRequests());
        holder.binding.uNamePost.setText("" + list.get(position).getPostFirstUser());
        Glide.with(context).load(list.get(position).getPostMedia().get(0)).circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.uImgPost);
//        setImagesRv(holder, position);
        if (list.get(position).getIsDonation() ) {
            holder.binding.isAvailable.setBackgroundColor(Color.red(0));
            holder.binding.isAvailable.setTextColor(Color.RED);
            holder.binding.isAvailable.setText("not Available");

        } else if (list.get(position).getIsDonation() ) {
            holder.binding.isAvailable.setBackgroundColor(Color.WHITE);
            holder.binding.isAvailable.setTextColor(Color.BLACK);
            holder.binding.isAvailable.setText("Available");

        }

        holder.binding.requestPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequestInterface.layout(list.get(position));
            }
        });

    }

//    private void setImagesRv(PostsAdapter.MyViewHolder holder, int position) {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
//                context, RecyclerView.HORIZONTAL, false);
//        holder.binding.rvImgPost.setLayoutManager(layoutManager);
//        ImagesPostsAdapter adapter = new ImagesPostsAdapter(context);
////        adapter.setList(list.get(position).getImagesList());
//        holder.binding.rvImgPost.setAdapter(adapter);
//    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

}