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

import com.example.graduationproject.listener.NotificationInterface;
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
    NotificationInterface notificationInterface;

    public NotificationAdapter(Context context, NotificationInterface notificationInterface) {
        list = new ArrayList<>();
        this.context = context;
        this.notificationInterface = notificationInterface;
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

    String message;

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String s;
        if (list.get(position).getSenderName().equals("sender name not found")){
            list.remove(position);
        }else{
            if (list.get(position).getType().equals("accept_request")) {
                message = list.get(position).getSenderName() + context.getString(R.string.accept_request_msg);
                holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_shape_peach));
            } else if (list.get(position).getType().equals("add_request")) {
                message = list.get(position).getSenderName() + context.getString(R.string.add_request_msg);
                holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_green_peach));
            } else if (list.get(position).getType().equals("update_Post")) {
                message = list.get(position).getSenderName() + context.getString(R.string.update_post_msg);
                holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_shape_orange_yellow));
            } else if (list.get(position).getType().equals("delete_post")) {
                message = list.get(position).getSenderName() + context.getString(R.string.delete_post_msg);
                holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_shape_red));
            } else if (list.get(position).getType().equals("delete_request")) {
                message = list.get(position).getSenderName() + context.getString(R.string.delete_request_msg);
                holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_shape_pink));
            } else if (list.get(position).getType().equals("delete_post_admin")) {
                message = list.get(position).getSenderName() + context.getString(R.string.delete_post_admin_msg);
                holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_shape_red));
            }
//        else if (list.get(position).getType().equals("admin")) {
//            holder.binding.view.setBackground(context.getDrawable(R.drawable.notification_view_shape_red));
//            if (list.get(position).getPostId() != 0) {
//                message = list.get(position).getSenderName() + " add post ";
//            } else {
//                message = list.get(position).getSenderName() + " create new account";
//            }
//        }
            else {
                holder.binding.view.setBackground(context.getDrawable(R.drawable.button_complete2));
                message = list.get(position).getType();
            }
//        else {
//            if (list.get(position).getIsDeleted()==null)
//                message = list.get(position).getSenderName() + " send you a request";
//            else
//                message = list.get(position).getSenderName() + " canceled his/her request";
//        }
            holder.binding.tvNotificationText.setText(message);
            holder.binding.tvNotificationDate.setText(list.get(position).getSentTime());
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationInterface.layout(list.get(position).getPostId() + "", list.get(position).getSenderId() + "");
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}