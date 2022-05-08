package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutPostItemBinding;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.retrofit.post.Post;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    Context context;
    List<Post> list;
    PostRequestInterface postRequestInterface;
    BottomSheetDialog dialog;


    public PostsAdapter(Context context, PostRequestInterface postRequestInterface) {
        list = new ArrayList<>();
        this.context = context;
        this.postRequestInterface = postRequestInterface;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Post> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutPostItemBinding binding;

        public MyViewHolder(LayoutPostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public PostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPostItemBinding binding = LayoutPostItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        return new MyViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.binding.titlePost.setText("" + list.get(position).getTitle());
        holder.binding.descriptionPost.setText("" + list.get(position).getDescription());
        holder.binding.numberRequestsPost.setText( list.get(position).getNumberOfRequests() +" request   " );
        holder.binding.uNamePost.setText("" + list.get(position).getFirstUserName());
        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.uImgPost);

        setPostImages(holder, position);
        if (list.get(position).getSecondUserName().equals("not found")) {
            holder.binding.isAvailable.setBackgroundColor(Color.WHITE);
            holder.binding.isAvailable.setTextColor(Color.BLACK);
            holder.binding.isAvailable.setText("Available");
        } else {
            holder.binding.isAvailable.setBackgroundColor(Color.red(0));
            holder.binding.isAvailable.setTextColor(Color.RED);
            holder.binding.isAvailable.setText("not Available");
        }

        if (list.get(position).getIsHeTheOwnerOfThePost()) {
            holder.binding.commentBtn.setText("Show orders");
        } else {
            if (list.get(position).getIsCompleted())
                holder.binding.commentBtn.setVisibility(View.INVISIBLE);
            else if (list.get(position).getOrdered())
                holder.binding.commentBtn.setVisibility(View.INVISIBLE );
            else
                holder.binding.commentBtn.setText("Add");

        }
        holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postRequestInterface.layout(list.get(position));
            }
        });
    }

    private void setPostImages(MyViewHolder holder, int position) {
        EasySlider easySlider = holder.binding.slider;

        List<SliderItem> sliderItems = new ArrayList<>();
        for (String images : list.get(position).getPostMedia()) {
            sliderItems.add(new SliderItem("", images));
        }
        easySlider.setPages(sliderItems);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}