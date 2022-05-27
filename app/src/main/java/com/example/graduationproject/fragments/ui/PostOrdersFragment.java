package com.example.graduationproject.fragments.ui;

import static com.example.graduationproject.fragments.ui.AllPostsFragment.parseError;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.adapters.PostOrdersAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.adapters.SwipeToDeleteCallback;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.databinding.FragmentPostOrdersBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.MyTitleEventBus;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.PostOrderRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.request.Data;
import com.example.graduationproject.retrofit.request.GetAllOrder;
import com.example.graduationproject.retrofit.request.Order;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostOrdersFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    Context context;
    FragmentPostOrdersBinding binding;
    PostOrdersAdapter adapter;
    private FragmentSwitcher fragmentSwitcher;

    // TODO: Rename and change types of parameters
    private int postId,secondUser;
    private Boolean isCompleted,isDonation;

    public PostOrdersFragment() {
        // Required empty public constructor
    }

    public static PostOrdersFragment newInstance(PostOrdersInfo param1) {
        PostOrdersFragment fragment = new PostOrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1.getPostId());
        args.putBoolean(ARG_PARAM2, param1.isCompleted());
        args.putInt(ARG_PARAM3, param1.getSecondUser());
        args.putBoolean(ARG_PARAM4, param1.isDonation());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getInt(ARG_PARAM1);
            secondUser = getArguments().getInt(ARG_PARAM3);
            isCompleted = getArguments().getBoolean(ARG_PARAM2);
            isDonation = getArguments().getBoolean(ARG_PARAM4);
        }
    }

    @Override
    public int getFragmentTitle() {
        return R.string.orders;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostOrdersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();

        //event bus
        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ALL_POSTS, "Your Order"));

        showDialog();
        getPostsOrdersRequest();
        enableSwipeToContactAndUndo();
        return view;
    }

    private void getPostsOrdersRequest() {
        RequestBody post_id = RequestBody.create(MediaType.parse("multipart/form-data"), postId + "");

        Call<GetAllOrder> call = serviceApi.getPostOrder(
                "Bearer " + token, post_id);
        call.enqueue(new Callback<GetAllOrder>() {
            @Override
            public void onResponse(Call<GetAllOrder> call, Response<GetAllOrder> response) {
                Log.d("response3 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    GetAllOrder getPostOrders = response.body();
                    setOrdersRv(getPostOrders);
                    progressDialog.dismiss();

                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<GetAllOrder> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void changePostStatusRequest(int user_id) {

        Call<Post> call = serviceApi.changePostStatus(postId,
                "Bearer " + token, user_id);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d("response3 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    fragmentSwitcher.switchFragment(PagesFragment.ALL_POSTS, null);

                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }
    AlertDialog.Builder builder;

    private void createAcceptOrderDialog(int userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("")
                        .setTitle(R.string.confirmation_message);

        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changePostStatusRequest(userId);
            }
        });
        builder.setNegativeButton(R.string.finish, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void setOrdersRv(GetAllOrder order) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        binding.postOrdersRecycle.setLayoutManager(layoutManager);

        adapter = new PostOrdersAdapter(context, new PostOrderRequestInterface() {
            @Override
            public void layout(int userId) {
                createAcceptOrderDialog(userId);
            }
        },isCompleted,isDonation,secondUser);
        adapter.setList(order.getData());
        binding.postOrdersRecycle.setAdapter(adapter);
        Log.e("rv2", order.getData().size() + "");

    }

    private void enableSwipeToContactAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Data item = adapter.getData().get(position);
                final int number = Integer.parseInt(adapter.getData().get(position).getUserPhoneNumber());

                String url = "https://api.whatsapp.com/send?phone=" + number;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

                adapter.restoreItem(item, position);
                binding.postOrdersRecycle.scrollToPosition(position);


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(binding.postOrdersRecycle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;
    }
}