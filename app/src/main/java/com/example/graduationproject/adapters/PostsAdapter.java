package com.example.graduationproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.databinding.PostItemBinding;
import com.example.graduationproject.fragments.ui.AllPostsFragment;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.retrofit.post.Post;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    Context context;
    List<Post> list;
    PostRequestInterface postRequestInterface;
    BottomSheetDialog dialog;


    public PostsAdapter(Context context, PostRequestInterface postRequestInterface) {
        list = new ArrayList<>();
        this.context = context;
        this.postRequestInterface = postRequestInterface;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Post> list) {
        this.list=list;
        notifyDataSetChanged();

    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        PostItemBinding binding;

        public MyViewHolder(PostItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public PostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemBinding binding = PostItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        return new MyViewHolder(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.binding.titlePost.setText("" + list.get(position).getTitle());
//        holder.binding.uNamePost.setText("number of request" + list.get(position).getPostFirstUser());
        holder.binding.descriptionPost.setText("" + list.get(position).getDescription());
        holder.binding.numberRequestsPost.setText("number of request =  " + list.get(position).getNumberOfRequests());
        holder.binding.uNamePost.setText("" + list.get(position).getPostFirstUser());
        Glide.with(context).load(list.get(position).getFirstUserImageLink()).circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.uImgPost);
//        Glide.with(context).load(list.get(position).getPostMedia().get(0))
//                .placeholder(R.drawable.ic_launcher_foreground).into(holder.binding.postImage);
//        setImagesRv(holder, position);
        setPostImages(holder, position);
        if (list.get(position).getPostSecondUser().equals("not found")) {
            holder.binding.isAvailable.setBackgroundColor(Color.WHITE);
            holder.binding.isAvailable.setTextColor(Color.BLACK);
            holder.binding.isAvailable.setText("Available");

        } else {
            holder.binding.isAvailable.setBackgroundColor(Color.red(0));
            holder.binding.isAvailable.setTextColor(Color.RED);
            holder.binding.isAvailable.setText("not Available");

        }



        dialog = new BottomSheetDialog(context);
        // inflate view
        createDialog();


        holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                postRequestInterface.layout(list.get(position));
            }
        });
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


    }

    private void setPostImages(PostsAdapter.MyViewHolder holder, int position) {
        EasySlider easySlider = holder.binding.slider;

        List<SliderItem> sliderItems = new ArrayList<>();
        for (String images : list.get(position).getPostMedia()) {
            sliderItems.add(new SliderItem("", images));

        }
        easySlider.setPages(sliderItems);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    // Dialog Comment
    private void createDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.botton_dialog,null,false);
        Button submit = view.findViewById(R.id.submit);
        EditText comment = view.findViewById(R.id.comment_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                Toast.makeText(AllPostsFragment.class, comment.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setContentView(view);
    }
}