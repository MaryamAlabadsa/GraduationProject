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
import com.example.graduationproject.databinding.LayoutPostItemBinding;
import com.example.graduationproject.databinding.LayoutProfilePostItemBinding;
import com.example.graduationproject.listener.PostDetialsInterface;
import com.example.graduationproject.listener.PostImageShowInterface;
import com.example.graduationproject.listener.PostProfileAddOrderInterface;
import com.example.graduationproject.listener.PostProfileMenuInterface;
import com.example.graduationproject.listener.PostProfileRemoveOrderInterface;
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
        holder.binding.uDateOrder.setText(list.get(position).getOrderUpdatedAt());
        Glide.with(context).load(list.get(position).getUserImage()).circleCrop()
                .placeholder(R.drawable.usericon).into(holder.binding.uImgPost);
        LayoutPostItemBinding layoutPostItemBinding = holder.binding.layoutPost;
        holder.binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getFirstUserName().equals("user not found")) {
                    Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
                } else {
                    int user_Id;
                    if (list.get(position).getTheOwnerIsLogin())
                        user_Id = 0;
                    else
                        user_Id = list.get(position).getUserId();

                    userIdtRequestInterface.layout(user_Id);
                }
            }
        });
        setOrderPostHolder(layoutPostItemBinding, position);
        if (!list.get(position).getIsMyProfile() || list.get(position).getPost().getIsCompleted())
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
        binding.commentBtn.setVisibility(View.INVISIBLE);
        binding.tvPostTitleImageSlider.setText(post.getTitle());
        binding.tvPostDesImageSlider.setText(post.getTitle());
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDetialsInterface.layout(list.get(position).getId());
            }
        });

        binding.uNamePost.setText("" + post.getFirstUserName());
        binding.uDatePost.setText("" + post.getPostCreatedAt());
        Glide.with(context).load(post.getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.usericon).into(binding.uImgPost);
        setOrderPostImages(binding, post);
        setOrderPostOrderButton(binding, post, position);
        setOrderPostStatus(binding, post);

        binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getPost().getFirstUserName().equals("user not found")) {
                    Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
                } else {
                    int user_Id;
                    if (list.get(position).getPost().getIsHeTheOwnerOfThePost())
                        user_Id = 0;
                    else
                        user_Id = list.get(position).getPost().getFirstUserId();

                    userIdtRequestInterface.layout(user_Id);
                }
            }
        });

    }

    private void setOrderPostImages(LayoutPostItemBinding binding, Post list) {
        if (list.getPostMedia().get(0).equals("")) {
            binding.imageSlider.setVisibility(View.GONE);
        } else {
            binding.imageSlider.setVisibility(View.VISIBLE);
            SliderView sliderView = binding.imageSlider;
            SliderAdapter adapter = new SliderAdapter(context, new SliderInterface() {
                @Override
                public void layout() {
                    postImageShowInterface.layout(list.getPostMedia());
                }
            });
            binding.postMenu.setVisibility(View.INVISIBLE);
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
    }

    private void setOrderPostOrderButton(LayoutPostItemBinding binding, Post list, int position) {
        if (list.getFirstUserName().equals("user not found")) {
            Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
        } else {
            if (list.getIsHeTheOwnerOfThePost()) {
                binding.commentBtn.setText("Show Orders");
                binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                    addOrderInterface.layout();
                    }
                });
            } else {
                if (list.getIsCompleted())
                    binding.commentBtn.setVisibility(View.INVISIBLE);
                else if (list.getIsOrdered())
                    binding.commentBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setOrderPostStatus(LayoutPostItemBinding binding, Post list) {
        if (list.getSecondUserName().equals("not found")) {
//           binding.postStatus.setBackgroundColor(Color.WHITE);
//            binding.postStatus.setTextColor(Color.RED);
            binding.postStatus.setBackgroundResource(R.drawable.deadline);
            binding.postStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "pending", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.postStatus.setBackgroundResource(R.drawable.clipboard);
            binding.postStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
//--------------------------------------------------------------------------------------------

    //---------------------------------------normal post-------------------------------------------------
    private void setPostHolder(MyPostViewHolder holder, int position) {
        ((MyPostViewHolder) holder).binding.uNamePost.setText("" + list.get(position).getFirstUserName());
        ((MyPostViewHolder) holder).binding.uDatePost.setText("" + list.get(position).getPostCreatedAt());
        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.usericon).into(((MyPostViewHolder) holder).binding.uImgPost);
        setPostImages(holder, position);
        setOrderButton(holder, position);
        setPostStatus(holder, position);

        ((MyPostViewHolder) holder).binding.uNamePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getFirstUserName().equals("user not found")) {
                    Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
                } else {
                    int user_Id;
                    if (list.get(position).getTheOwnerIsLogin())
                        user_Id = 0;
                    else
                        user_Id = list.get(position).getUserId();

                    userIdtRequestInterface.layout(user_Id);
                }
            }
        });
        setPostMenu(holder, position);
    }

    private void setPostImages(ProfilePostsAdapter.MyPostViewHolder holder, int position) {
        if (list.get(position).getPostMedia().get(0).equals("")) {
            holder.binding.imageSlider.setVisibility(View.GONE);
        } else {
            holder.binding.imageSlider.setVisibility(View.VISIBLE);
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
            sliderView.setIndicatorSelectedColor(context.getColor(R.color.pink));
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
    }

    private void setOrderButton(ProfilePostsAdapter.MyPostViewHolder holder, int position) {

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postDetialsInterface.layout(list.get(position).getId());
            }
        });
        if (list.get(position).getFirstUserName().equals("user not found")) {
            holder.binding.commentBtn.setVisibility(View.INVISIBLE);
//            Toast.makeText(context, context.getText(R.string.deletedUser), Toast.LENGTH_SHORT).show();
        } else {
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
                    holder.binding.commentBtn.setText("Add Comment");
                }
            }
            holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addOrderInterface.layout(list.get(position), position);
                }
            });

        }
    }

    private void setPostStatus(ProfilePostsAdapter.MyPostViewHolder holder, int position) {
        if (list.get(position).getSecondUserName().equals("not found")) {
//            holder.binding.postStatus.setTextColor(Color.RED);
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
        holder.binding.tvPostTitleImageSlider.setText(list.get(position).getTitle());
        holder.binding.tvPostDesImageSlider.setText(list.get(position).getTitle());
    }

    private void setPostMenu(MyPostViewHolder holder, int position) {
        holder.binding.postMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMenuInterface.layoutPost(list.get(position), position, holder.binding.postMenu);
            }
        });

        if (!list.get(position).getIsHeTheOwnerOfThePost() || list.get(position).getIsCompleted())
            holder.binding.postMenu.setVisibility(View.INVISIBLE);
    }

    //--------------------------------------------------------------------------------------------------
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