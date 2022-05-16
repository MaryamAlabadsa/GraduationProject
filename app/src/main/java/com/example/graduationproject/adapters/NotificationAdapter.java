package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutNotificationItemBinding;

import com.example.graduationproject.retrofit.notifiction.Datum;
import com.example.graduationproject.retrofit.post.Post;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    List<Datum> list;


    public NotificationAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Datum> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutNotificationItemBinding binding;

        public MyViewHolder(LayoutNotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutNotificationItemBinding binding = LayoutNotificationItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        return new MyViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String message;
        if (list.get(position).getType().equals("accept_request")) {
            message = list.get(position).getSenderName() + "accept your request";

        } else {
            message = list.get(position).getSenderName() + " send you a request";

        }
        holder.binding.tvNotificationText.setText(message);

    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}