package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutPostItemBinding;
import com.example.graduationproject.databinding.LayoutProfilePostItemBinding;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostDetialsInterface;
import com.example.graduationproject.listener.PostImageShowInterface;
import com.example.graduationproject.listener.PostMenuInterface;
import com.example.graduationproject.listener.PostProfileAddOrderInterface;
import com.example.graduationproject.listener.PostProfileMenuInterface;
import com.example.graduationproject.listener.PostProfileRemoveOrderInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.SliderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.SliderItem;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.profile.donation.posts.PostsList;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class ProfilePostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<PostsList> list;
    public static final int ITEM1 = 0;
    public static final int ITEM2 = 1;

    PostProfileAddOrderInterface addOrderInterface;
    PostProfileRemoveOrderInterface removeOrderInterface;
    UserIdtRequestInterface userIdtRequestInterface;
    PostDetialsInterface postDetialsInterface;
    PostImageShowInterface postImageShowInterface;
    PostProfileMenuInterface postMenuInterface;

    public ProfilePostsAdapter(Context context,
                               PostDetialsInterface postDetialsInterface,
                               PostProfileAddOrderInterface addOrderInterface,
                               PostProfileRemoveOrderInterface removeOrderInterface,
                               UserIdtRequestInterface userIdtRequestInterface,
                               PostImageShowInterface postImageShowInterface,
                               PostProfileMenuInterface postMenuInterface) {
        list = new ArrayList<>();
        this.context = context;
        this.postDetialsInterface = postDetialsInterface;
        this.addOrderInterface = addOrderInterface;
        this.removeOrderInterface = removeOrderInterface;
        this.userIdtRequestInterface = userIdtRequestInterface;
        this.postImageShowInterface = postImageShowInterface;
        this.postMenuInterface = postMenuInterface;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<PostsList> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public void modifyBtn(PostsList item, int position) {
        list.remove(position);
        list.add(position, item);
        notifyDataSetChanged();

    }

    public void restoreItem(Post item, int position) {
        list.get(position).setPost(item);
        notifyDataSetChanged();

    }

    public void restorePost(PostsList item, int position) {
        list.add(position, item);
        notifyDataSetChanged();

    }

    public void clearItems() {
        list.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    static class MyPostViewHolder extends RecyclerView.ViewHolder {
        LayoutPostItemBinding binding;

        public MyPostViewHolder(LayoutPostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class MyOrderViewHolder extends RecyclerView.ViewHolder {
        LayoutProfilePostItemBinding binding;

        public MyOrderViewHolder(LayoutProfilePostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM1) {
            LayoutPostItemBinding binding = LayoutPostItemBinding.inflate(LayoutInflater
                            .from(parent.getContext())
                    , parent, false);
            return new MyPostViewHolder(binding);
        } else {
            LayoutProfilePostItemBinding binding = LayoutProfilePostItemBinding.inflate(LayoutInflater
                            .from(parent.getContext())
                    , parent, false);
            return new MyOrderViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        postsList = list.get(position);

        if (getItemViewType(position) == ITEM1) {
            // ITEM1
            setPostHolder((MyPostViewHolder) holder, position);
        } else {
            setOrderHolder((MyOrderViewHolder) holder, position);

        }

    }

    private void setOrderHolder(MyOrderViewHolder holder, int position) {
        holder.binding.uNamePost.setText(list.get(position).getUserName());
        holder.binding.uDatePost.setText(list.get(position).getPublishedAt());
        Glide.with(context).load(list.get(position).getUserImage()).circleCrop()
                .placeholder(R.drawable.usericon).into(holder.binding.uImgPost);
        LayoutPostItemBinding layoutPostItemBinding = holder.binding.layoutPost;
        holder.binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_Id;
                if (list.get(position).getTheOwnerIsLogin())
                    user_Id = 0;
                else
                    user_Id = list.get(position).getUserId();

                userIdtRequestInterface.layout(user_Id);
//                userIdtRequestInterface.layout(list.get(position).getUserId());
            }
        });
        setOrderPostHolder(layoutPostItemBinding, position);
        if (list.get(position).getPost().getThe_owner_is_login())
            holder.binding.orderMenu.setVisibility(View.INVISIBLE);
        holder.binding.orderMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMenuInterface.layoutOrder(list.get(position), position, holder.binding.orderMenu);
            }
        });
    }

    PostsList postsList;

    //order post
    private void setOrderPostHolder(LayoutPostItemBinding binding, int position) {

        Post post = postsList.getPost();
        binding.postMenu.setVisibility(View.INVISIBLE);
        binding.commentBtn.setVisibility(View.INVISIBLE);

        binding.postMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMenuInterface.layoutOrder(list.get(position), position, binding.postMenu);
            }
        });
//        binding.numberRequestsPost.setText(post.getNumberOfRequests() + " request   ");
        binding.uNamePost.setText("" + post.getFirstUserName());
        binding.uDatePost.setText("" + post.getPublishedAt());
        Glide.with(context).load(post.getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.usericon).into(binding.uImgPost);
        setOrderPostImages(binding, post);
        setOrderPostOrderButton(binding, post, position);
        setOrderPostStatus(binding, post);

        binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_Id;
                if (list.get(position).getTheOwnerIsLogin())
                    user_Id = 0;
                else
                    user_Id = list.get(position).getPost().getFirstUserId();

                userIdtRequestInterface.layout(user_Id);
//                userIdtRequestInterface.layout(list.get(position).getPost().getFirstUserId());
            }
        });

    }

    private void setOrderPostImages(LayoutPostItemBinding binding, Post list) {
        SliderView sliderView = binding.imageSlider;
        SliderAdapter adapter = new SliderAdapter(context, new SliderInterface() {
            @Override
            public void layout() {
                postImageShowInterface.layout(list.getPostMedia());
            }
        });
        sliderView.setSliderAdapter(adapter);
        SliderItem sliderItem = new SliderItem(list.getTitle(), list.getDescription(), list.getPostMedia());
        adapter.addItem(list.getPostMedia());

        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.BLUE);
        sliderView.setIndicatorUnselectedColor(Color.WHITE);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        sliderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImageShowInterface.layout(list.getPostMedia());
            }
        });
    }

    private void setOrderPostOrderButton(LayoutPostItemBinding binding, Post list, int position) {
        if (list.getIsHeTheOwnerOfThePost()) {
            binding.commentBtn.setText("Show orders");
        } else {
            if (list.getIsCompleted())
                binding.commentBtn.setVisibility(View.INVISIBLE);
            else if (list.getIsOrdered())
                binding.commentBtn.setVisibility(View.INVISIBLE);
            else {
//                binding.commentBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        addOrderInterface.layout(list, position);
//                    }
//                });
//                binding.commentBtn.setText("Add order");
            }
        }

    }

    private void setOrderPostStatus(LayoutPostItemBinding binding, Post list) {
        if (list.getSecondUserName().equals("not found")) {
//           binding.postStatus.setBackgroundColor(Color.WHITE);
            binding.postStatus.setTextColor(Color.RED);
            binding.postStatus.setText("Pending");
        } else {
//           binding.postStatus.setBackgroundColor(Color.red(0));
            binding.postStatus.setTextColor(Color.GREEN);
            binding.postStatus.setText("completed");
        }
    }

    // post
    private void setPostHolder(MyPostViewHolder holder, int position) {
        if (!list.get(position).getTheOwnerIsLogin())
            holder.binding.postMenu.setVisibility(View.INVISIBLE);

//        ((MyPostViewHolder) holder).binding.numberRequestsPost.setText(list.get(position).getNumberOfRequests() + " request   ");
        ((MyPostViewHolder) holder).binding.uNamePost.setText("" + list.get(position).getFirstUserName());
        ((MyPostViewHolder) holder).binding.uDatePost.setText("" + list.get(position).getPublishedAt());
        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.usericon).into(((MyPostViewHolder) holder).binding.uImgPost);
        setPostImages(holder, position);
        setOrderButton(holder, position);
        setPostStatus(holder, position);

        ((MyPostViewHolder) holder).binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int user_Id;
                if (list.get(position).getTheOwnerIsLogin())
                    user_Id = 0;
                else
                    user_Id = list.get(position).getUserId();

                userIdtRequestInterface.layout(user_Id);
            }
        });
    }

    private void setPostImages(ProfilePostsAdapter.MyPostViewHolder holder, int position) {
        SliderView sliderView = holder.binding.imageSlider;
        SliderAdapter adapter = new SliderAdapter(context, new SliderInterface() {
            @Override
            public void layout() {
                postImageShowInterface.layout(list.get(position).getPostMedia());
            }
        });
        sliderView.setSliderAdapter(adapter);
        SliderItem sliderItem = new SliderItem(list.get(position).getTitle(), list.get(position).getDescription(), list.get(position).getPostMedia());
        adapter.addItem(list.get(position).getPostMedia());

        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(context.getColor(R.color.bink));
        sliderView.setIndicatorUnselectedColor(Color.WHITE);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
        sliderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImageShowInterface.layout(list.get(position).getPostMedia());
            }
        });
    }

    private void setOrderButton(ProfilePostsAdapter.MyPostViewHolder holder, int position) {

        if (list.get(position).getIsHeTheOwnerOfThePost()) {
            holder.binding.commentBtn.setText("Show orders");
        } else {
            if (list.get(position).getIsCompleted())
                holder.binding.commentBtn.setVisibility(View.INVISIBLE);
            else if (list.get(position).getIsOrdered()) {
                holder.binding.commentBtn.setText("Remove order");
                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeOrderInterface.layout(list.get(position), position);
                    }
                });
            } else {
                holder.binding.commentBtn.setText("Add");
                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addOrderInterface.layout(list.get(position), position);
                    }
                });
            }
        }
        holder.binding.postMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMenuInterface.layoutPost(list.get(position), position, holder.binding.postMenu);
            }
        });
    }

    private void setPostStatus(ProfilePostsAdapter.MyPostViewHolder holder, int position) {
        if (list.get(position).getSecondUserName().equals("not found")) {
            holder.binding.postStatus.setTextColor(Color.RED);
            holder.binding.postStatus.setText("Pending");
        } else {
//            holder.binding.postStatus.setBackgroundColor(Color.red(0));
            holder.binding.postStatus.setTextColor(Color.GREEN);
            holder.binding.postStatus.setText("completed");
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getStatus() == ITEM1) {
            return ITEM1;
        } else {
            return ITEM2;
        }
    }
}