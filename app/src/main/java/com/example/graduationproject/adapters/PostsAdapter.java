package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutPostItemBinding;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostDetialsInterface;
import com.example.graduationproject.listener.PostImageShowInterface;
import com.example.graduationproject.listener.PostMenuInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.PostShowOrdersInterface;
import com.example.graduationproject.listener.SliderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.retrofit.post.Post;
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
    PostImageShowInterface postImageShowInterface;
    PostMenuInterface postMenuInterface;
    PostShowOrdersInterface postShowOrdersInterface;

    public PostsAdapter(Context context,
                        PostDetialsInterface postDetialsInterface,
                        PostShowOrdersInterface postShowOrdersInterface,
                        PostAddOrderInterface addOrderInterface,
                        PostRemoveOrderInterface removeOrderInterface,
                        UserIdtRequestInterface userIdtRequestInterface,
                        PostImageShowInterface postImageShowInterface,
                        PostMenuInterface postMenuInterface) {
        list = new ArrayList<>();
        this.context = context;
        this.postDetialsInterface = postDetialsInterface;
        this.postShowOrdersInterface = postShowOrdersInterface;
        this.addOrderInterface = addOrderInterface;
        this.removeOrderInterface = removeOrderInterface;
        this.userIdtRequestInterface = userIdtRequestInterface;
        this.postImageShowInterface = postImageShowInterface;
        this.postMenuInterface = postMenuInterface;
    }


    public void restoreItem(Post item, int position) {
        list.add(position, item);
        notifyDataSetChanged();
    }

    public void modifyBtn(Post item, int position) {
        list.remove(position);
        list.add(position, item);
        notifyDataSetChanged();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Post> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addToList(List<Post> myList) {
        list.addAll(myList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
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
        setPostData(holder, position);

    }

    private void setPostData(PostsAdapter.MyViewHolder holder, int position) {
        holder.binding.uDatePost.setText(list.get(position).getPostCreatedAt());
        holder.binding.uNamePost.setText(list.get(position).getFirstUserName());
        holder.binding.tvPostTitleImageSlider.setText(list.get(position).getTitle());
        holder.binding.tvPostDesImageSlider.setText(list.get(position).getDescription());
        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.usericon).into(holder.binding.uImgPost);
        setPostImages(holder, position);
        setPostMenu(holder, position);
        setPostCommentBtn(holder, position);
        setPostDetails(holder, position);
        setUserProfile(holder, position);
        setPostStatus(holder, position);
    }

    private void setUserProfile(MyViewHolder holder, int position) {

        holder.binding.uImgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getFirstUserName().equals("user not found")) {
                    Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
                } else
                    userIdtRequestInterface.layout(list.get(position).getFirstUserId());
            }
        });
        holder.binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getFirstUserName().equals("user not found")) {
                    Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
                } else{
                    if (list.get(position).getTheOwnerIsLogin())
                        userIdtRequestInterface.layout(0);
                    else
                    userIdtRequestInterface.layout(list.get(position).getFirstUserId());

                }
            }
        });


    }

    private void setPostMenu(MyViewHolder holder, int position) {
        if (list.get(position).getIsHeTheOwnerOfThePost() && !list.get(position).getIsCompleted())
            holder.binding.postMenu.setVisibility(View.VISIBLE);
        else if (!list.get(position).getIsHeTheOwnerOfThePost() || list.get(position).getIsCompleted())
            holder.binding.postMenu.setVisibility(View.INVISIBLE);
        holder.binding.postMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMenuInterface.layout(list.get(position), position, holder.binding.postMenu);
            }
        });
    }

    private void setPostImages(PostsAdapter.MyViewHolder holder, int position) {
        SliderView sliderView = holder.binding.imageSlider;
        SliderAdapter adapter = new SliderAdapter(context, new SliderInterface() {
            @Override
            public void layout() {
                postImageShowInterface.layout(list.get(position).getPostMedia());
            }
        });
        sliderView.setSliderAdapter(adapter);
        adapter.addItem(list.get(position).getPostMedia());
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(context.getColor(R.color.pink));
        sliderView.setIndicatorUnselectedColor(context.getColor(R.color.colorPrimaryDark));
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void setPostDetails(PostsAdapter.MyViewHolder holder, int position) {
        holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                postDetialsInterface.layout(list.get(position).getId());
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setPostCommentBtn(PostsAdapter.MyViewHolder holder, int position) {
        if (list.get(position).getFirstUserName().equals("user not found")) {
            holder.binding.commentBtn.setVisibility(View.INVISIBLE);
        } else {
            if (list.get(position).getIsHeTheOwnerOfThePost()) {
                holder.binding.commentBtn.setText("Show Requests");
                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postShowOrdersInterface.layout(list.get(position), position);
                    }
                });
            } else {
                if (list.get(position).getIsCompleted()) {
                    holder.binding.commentBtn.setVisibility(View.INVISIBLE);
                } else {
                    holder.binding.commentBtn.setVisibility(View.VISIBLE);
                    if (list.get(position).getIsOrdered()) {
                        holder.binding.commentBtn.setText("Remove Request");
                        holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removeOrderInterface.layout(list.get(position), position);
                            }
                        });
                    } else {
                        holder.binding.commentBtn.setText("Add Requests");
                        holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addOrderInterface.layout(list.get(position), position);
                            }
                        });
                    }
                }
            }

        }

    }

    private void setPostStatus(PostsAdapter.MyViewHolder holder, int position) {
        if (list.get(position).getSecondUserName().equals("not found")) {
            holder.binding.postStatus.setBackgroundResource(R.drawable.deadline);
            holder.binding.postStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "pending", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.binding.postStatus.setBackgroundResource(R.drawable.clipboard);
            holder.binding.postStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}