package com.example.graduationproject;

import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.adapters.SliderAdapter;
import com.example.graduationproject.listener.SliderInterface;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class test {
// holder.binding.uDatePost.setText(list.get(position).getPostCreatedAt());
//        holder.binding.uNamePost.setText("" + list.get(position).getFirstUserName());
//        holder.binding.tvPostTitleImageSlider.setText("" + list.get(position).getTitle());
//        holder.binding.tvPostDesImageSlider.setText("" + list.get(position).getDescription());
//        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
//                .placeholder(R.drawable.usericon).into(holder.binding.uImgPost);
//
//    setPostImages(holder, position);
//    setOrderButton(holder, position);
//    setPostStatus(holder, position);
//
//        holder.binding.uNamePost.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            int user_Id;
//            if (list.get(position).getTheOwnerIsLogin())
//                user_Id = 0;
//            else
//                user_Id = list.get(position).getFirstUserId();
//
//            userIdtRequestInterface.layout(user_Id);
//        }
//    });
//        holder.binding.imageSlider.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            postImageShowInterface.layout(list.get(position).getPostMedia());
//        }
//    });
//    setPostMenu(holder, position);
//        holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
//        @Override
//        public boolean onLongClick(View view) {
//            postDetialsInterface.layout(list.get(position).getId());
//            return true;
//        }
//    });
//
//}
//
//    private void setPostMenu(PostsAdapter.MyViewHolder holder, int position) {
//        holder.binding.postMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                postMenuInterface.layout(list.get(position), position, holder.binding.postMenu);
//            }
//        });
//
//
//        if (!list.get(position).getIsHeTheOwnerOfThePost() || list.get(position).getIsCompleted())
//            holder.binding.postMenu.setVisibility(View.INVISIBLE);
//    }
//
//    private void setPostImages(PostsAdapter.MyViewHolder holder, int position) {
//        SliderView sliderView = holder.binding.imageSlider;
//        SliderAdapter adapter = new SliderAdapter(context, new SliderInterface() {
//            @Override
//            public void layout() {
//                postImageShowInterface.layout(list.get(position).getPostMedia());
//            }
//        });
//
//        sliderView.setSliderAdapter(adapter);
//
//        adapter.addItem(list.get(position).getPostMedia());
//
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderView.setIndicatorSelectedColor(context.getColor(R.color.pink));
//        sliderView.setIndicatorUnselectedColor(context.getColor(R.color.colorPrimaryDark));
//        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//        sliderView.startAutoCycle();
//
//    }
//
//    private void setOrderButton(PostsAdapter.MyViewHolder holder, int position) {
//        if (list.get(position).getIsHeTheOwnerOfThePost()) {
//            holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    addOrderInterface.layout(list.get(position), position);
//                    notifyDataSetChanged();
//                }
//            });
//            holder.binding.commentBtn.setText("Show orders");
//
//        } else {
//            if (list.get(position).getIsCompleted()) {
//                holder.binding.commentBtn.setVisibility(View.INVISIBLE);
//                holder.binding.postMenu.setVisibility(View.INVISIBLE);
//            } else if (list.get(position).getIsOrdered()) {
//                holder.binding.commentBtn.setText("Remove order");
//                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        removeOrderInterface.layout(list.get(position), position);
//                        notifyDataSetChanged();
//                    }
//                });
//            } else {
//                holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        addOrderInterface.layout(list.get(position), position);
//                        notifyDataSetChanged();
//                    }
//                });
//                holder.binding.commentBtn.setText("Add Comment");
//            }
//        }
//    }
//
//    private void setPostStatus(PostsAdapter.MyViewHolder holder, int position) {
//        if (list.get(position).getSecondUserName().equals("not found")) {
//            holder.binding.postStatus.setBackgroundResource(R.drawable.deadline);
//            holder.binding.postStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "pending", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            holder.binding.postStatus.setBackgroundResource(R.drawable.clipboard);
//            holder.binding.postStatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }


    }
