package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.CategoryItemBinding;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.retrofit.categories.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    List<Category> list;
    CategoryInterface categoryInterface;
    int selectedPosition = 0;

    public CategoryAdapter(Context context, CategoryInterface categoryInterface) {
        this.context = context;
        this.categoryInterface = categoryInterface;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CategoryItemBinding binding;

        public MyViewHolder(CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemBinding binding = CategoryItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(binding);
        return myViewHolder;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.categoryName.setText("" + list.get(position).getName());
        Glide.with(context)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.categoryImage);

        setUpActions(holder, position);
        if (position == selectedPosition) {
            holder.binding.card.setBackground(context.getDrawable(R.drawable.button_profile1));
            holder.binding.categoryImage.setVisibility(View.VISIBLE);
            holder.binding.categoryName.setTextColor(context.getColor(R.color.white));


        } else {
            holder.binding.card.setBackground(context.getDrawable(R.drawable.button_profile2));
            holder.binding.categoryImage.setVisibility(View.GONE);
            holder.binding.categoryName.setTextColor(context.getColor(R.color.color_app));

        }
    }

    private void setUpActions(CategoryAdapter.MyViewHolder holder, int position) {
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                categoryInterface.layout(list.get(position).getId());
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

}