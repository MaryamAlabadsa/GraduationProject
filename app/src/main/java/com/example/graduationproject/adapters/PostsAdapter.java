package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutPostItemBinding;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostDetialsInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.SliderItem;
import com.example.graduationproject.retrofit.post.Post;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    Context context;
    List<Post> list;
    PostAddOrderInterface addOrderInterface;
    PostRemoveOrderInterface removeOrderInterface;
    UserIdtRequestInterface userIdtRequestInterface;
PostDetialsInterface postDetialsInterface;

    public PostsAdapter(Context context,
                        PostDetialsInterface postDetialsInterface,
                        PostAddOrderInterface addOrderInterface,
                        PostRemoveOrderInterface removeOrderInterface,
                        UserIdtRequestInterface userIdtRequestInterface) {
        list = new ArrayList<>();
        this.context = context;
        this.postDetialsInterface = postDetialsInterface;
        this.addOrderInterface = addOrderInterface;
        this.removeOrderInterface = removeOrderInterface;
        this.userIdtRequestInterface = userIdtRequestInterface;
    }


    public void resetItem(Post item, int position) {
        list.add(position,item);
        notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Post> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addToList(List<Post> myList){
        list.addAll(myList);
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
    public void onBindViewHolder(@NonNull PostsAdapter.MyViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        holder.binding.numberRequestsPost.setText(list.get(position).getNumberOfRequests() + " request   ");
        holder.binding.uDatePost.setText(list.get(position).getPublishedAt());
        holder.binding.uNamePost.setText("" + list.get(position).getFirstUserName());
        holder.binding.tvPostTitleImageSlider.setText("" + list.get(position).getTitle());
        holder.binding.tvPostDesImageSlider.setText("" + list.get(position).getDescription());
        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.uImgPost);

        setPostImages(holder, position);
        setOrderButton(holder, position);
        setPostStatus(holder, position);

        holder.binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIdtRequestInterface.layout(list.get(position).getFirstUserId());
            }
        });
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDetialsInterface.layout(list.get(position).getId());
            }
        });


    }

    private void setPostImages(MyViewHolder holder, int position) {
        SliderView sliderView = holder.binding.imageSlider;
        SliderAdapter adapter = new SliderAdapter(context);

        sliderView.setSliderAdapter(adapter);

        adapter.addItem(list.get(position).getPostMedia());

        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(context.getColor(R.color.bink));
        sliderView.setIndicatorUnselectedColor(context.getColor(R.color.colorPrimaryDark));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void setOrderButton(MyViewHolder holder, int position) {
        if (list.get(position).getIsHeTheOwnerOfThePost()) {
            holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addOrderInterface.layout(list.get(position),position);
                    notifyDataSetChanged();
                }
            });
            holder.binding.commentBtn.setText("Show orders");
        } else {
            if (list.get(position).getIsCompleted())
                holder.binding.commentBtn.setVisibility(View.INVISIBLE);
            else if (list.get(position).getIsOrdered()) {
                holder.binding.commentBtn.setText("Remove order");
                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeOrderInterface.layout(list.get(position),position);
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addOrderInterface.layout(list.get(position),position);
                        notifyDataSetChanged();

                    }
                });
                holder.binding.commentBtn.setText("Add order");
            }
        }
    }

    private void setPostStatus(MyViewHolder holder, int position) {
        if (list.get(position).getSecondUserName().equals("not found")) {
            holder.binding.postStatus.setBackgroundColor(Color.WHITE);
            holder.binding.postStatus.setTextColor(Color.RED);
            holder.binding.postStatus.setText("Pending");
        } else {
            holder.binding.postStatus.setBackgroundColor(Color.red(0));
            holder.binding.postStatus.setTextColor(context.getColor(R.color.colorAccent));
            holder.binding.postStatus.setText("completed");
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}