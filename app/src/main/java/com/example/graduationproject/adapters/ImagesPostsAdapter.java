package com.example.graduationproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ImagesPostItemBinding;

import java.util.ArrayList;

public class ImagesPostsAdapter extends RecyclerView.Adapter<ImagesPostsAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> list;

    public ImagesPostsAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImagesPostItemBinding binding;

        public MyViewHolder(ImagesPostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public ImagesPostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImagesPostItemBinding binding = ImagesPostItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(binding);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesPostsAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.imagePost);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

}