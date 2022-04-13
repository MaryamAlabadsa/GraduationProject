package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.categoryName.setText("" + list.get(position).getName());

        setUpActions(holder, position);
        if (position == selectedPosition) {
            holder.binding.card.setBackgroundColor(context.getColor(R.color.colorPrimary));
        } else {
            holder.binding.card.setBackgroundColor(context.getColor(R.color.primaryLight));
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