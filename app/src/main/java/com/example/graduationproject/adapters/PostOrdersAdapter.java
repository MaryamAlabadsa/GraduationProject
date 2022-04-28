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
import com.example.graduationproject.databinding.LayoutPostOrderBinding;
import com.example.graduationproject.listener.PostOrderRequestInterface;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.retrofit.request.Data;
import com.example.graduationproject.retrofit.request.Order;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

public class PostOrdersAdapter extends RecyclerView.Adapter<PostOrdersAdapter.MyViewHolder> {

    Context context;
    List<Data> list;
    Order order;
    PostOrderRequestInterface postOrderRequestInterface;
    Boolean isCompleted, isDonation;
    int userId;

    public PostOrdersAdapter(Context context, PostOrderRequestInterface postOrderRequestInterface, Boolean isCompleted, Boolean isDonation, int userId) {
        this.context = context;
        this.postOrderRequestInterface = postOrderRequestInterface;
        this.isCompleted = isCompleted;
        this.isDonation = isDonation;
        this.userId = userId;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(Order order) {
        this.order = order;
        this.list = order.getData();
        notifyDataSetChanged();

    }

    public List<Data> getData() {
        return list;
    }

    public void restoreItem(Data item, int position) {
        removeItem(position);
        list.add(position, item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutPostOrderBinding binding;

        public MyViewHolder(LayoutPostOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public PostOrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPostOrderBinding binding = LayoutPostOrderBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        return new MyViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostOrdersAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.binding.userName.setText(list.get(position).getUserName());
        holder.binding.postMassage.setText(list.get(position).getMassage());
        Glide.with(context).load(list.get(position).getUserImage()).circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.userImage);

        if (isCompleted) {
            if (list.get(position).getUserId() == userId) {
                if (isDonation)
                    holder.binding.acceptOrderBtn.setText(R.string.beneficiary);
                else
                    holder.binding.acceptOrderBtn.setText(R.string.donor);
            } else
                holder.binding.acceptOrderBtn.setVisibility(View.INVISIBLE);

        }else {
            holder.binding.acceptOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int userId = list.get(position).getUserId();
                    postOrderRequestInterface.layout(userId);
                }
            });
        }
        //        setOrderTime(holder, position);
    }

//    private void setOrderTime(PostOrdersAdapter.MyViewHolder holder, int position) {
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        String dateString = formatter.format(new Date(Long.parseLong(list.get(position).getCreatedAt())));
//        holder.binding.postTime.setText(dateString);
//    }
////"2022-04-11T15:23:28.000000Z"
//    private void getTime(){
//
//
//    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}