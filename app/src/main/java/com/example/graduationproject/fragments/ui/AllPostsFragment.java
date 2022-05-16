package com.example.graduationproject.fragments.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.databinding.ButtonDialogBinding;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.dialog.Dialoginterface;
import com.example.graduationproject.dialog.MyDialogAddOrder;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPostsFragment extends BaseFragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ALL_POSTS";
    FragmentAllPostsBinding binding;
    Context context;
    MyDialogAddOrder myDialogAddOrder;
    private FragmentSwitcher fragmentSwitcher;
    List<Category> data;

    Intent intent;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllPostsFragment() {
        // Required empty public constructor
    }

    public static AllPostsFragment newInstance(String param1, String param2) {
        AllPostsFragment fragment = new AllPostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentAllPostsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        dialogBinding = ButtonDialogBinding.inflate(inflater, container, false);
        context = getActivity();
//        dialog = new BottomSheetDialog(context);
        showDialog();
        getAllCategories();
        getAllPosts();
        return view;
    }

    int isDonation = -1;

    @Override
    public int getFragmentTitle() {
        return R.string.allPosts;
    }

    private void getAllCategories() {
        Call<AllCategories> call = serviceApi.getAllCategories(
                "Bearer " + token);
        call.enqueue(new Callback<AllCategories>() {
            @Override
            public void onResponse(Call<AllCategories> call, Response<AllCategories> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllCategories getAllCategories = response.body();
                    data = getAllCategories.getData();
                    setCategoryRv(data);
                    binding.isDonationLinear.setVisibility(View.VISIBLE);
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllCategories> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
                progressDialog.dismiss();

            }
        });
    }

    private void getAllPosts() {
        Call<AllPosts> call = serviceApi.getAllPosts("Bearer " + token);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
                    assert response.body() != null;
                    setPostsRv(response.body().getData());
                    Log.e("maryam", getAllPosts.getData().size() + "");
                    progressDialog.dismiss();
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
                progressDialog.dismiss();

            }
        });
    }

    private void getPostsByCategory(int id) {
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), id + "");

        Call<AllPosts> call = serviceApi.getPostByCategory(
                "Bearer " + token, category_id);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response3 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
                    setPostsRv(getAllPosts.getData());
                    progressDialog.dismiss();

                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }


    private void getPostDividedByIsDonation(int id) {
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), id + "");

        Call<AllPosts> call = serviceApi.getPostDividedByIsDonation(
                "Bearer " + token, category_id);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
                    setPostsRv(getAllPosts.getData());
                    progressDialog.dismiss();

                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    public static String parseError(Response<?> response) {
        //function that parse error from api in case response code!=200
        String errorMsg = null;
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            errorMsg = jObjError.getString("message");
            return errorMsg;
        } catch (Exception e) {
        }
        return errorMsg;
    }

    private void setPostsRv(List<Post> postList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        binding.rvPost.setLayoutManager(layoutManager);
        PostsAdapter adapter = new PostsAdapter(context, new PostAddOrderInterface() {
            @Override
            public void layout(Post post) {
                if (post.getIsHeTheOwnerOfThePost()) {
                    PostOrdersInfo info = new PostOrdersInfo(post.getId(), post.getIsCompleted(), post.getIsDonation(), post.getSecondUserId());
                    fragmentSwitcher.switchFragment(PagesFragment.POST_ORDERS, info);
                } else{
                    createDialog(post.getId());
                    myDialogAddOrder.show();
                }

            }
        }, new PostRemoveOrderInterface() {
            @Override
            public void layout(Post post) {
                showDialog();
                RemoveRequest(post.getOrderId());
            }
        }, new UserIdtRequestInterface() {
            @Override
            public void layout(int userId) {
                fragmentSwitcher.switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(userId));
            }
        });
        adapter.setList(postList);
        binding.rvPost.setAdapter(adapter);
        Log.e("rv2", postList.size() + "");
    }

//    private void createDialog(int id) {
//        View view = dialogBinding.getRoot();
//        dialogBinding.submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String massage = dialogBinding.editComment.getText().toString();
//                AddRequest(id, massage,dialog);
//            }
//        });
//        dialog.setContentView(dialogBinding.getRoot());
//    }

    private void setCategoryRv(List<Category> data) {

        data.add(0, new Category(0, "All",R.drawable.all_category));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(context, new CategoryInterface() {
            @Override
            public void layout(int id) {
                if (id == 0){
                    showDialog();
                    getAllPosts();

                }
                else{
                    showDialog();
                    getPostsByCategory(id);

                }
            }
        });
        adapter.setList(data);
        binding.rvCategory.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;

    }
    public void AddRequest(int id_post, String massage) {

        RequestBody post_id = RequestBody.create(MediaType.parse("multipart/form-data"), id_post + "");
        RequestBody mPost = RequestBody.create(MediaType.parse("multipart/form-data"), massage);

        Call<Order> call = serviceApi.addRequest(
                "Bearer " + token
                , post_id
                , mPost);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.d("response5 code", response.code() + "");
                myDialogAddOrder.dismiss();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.getMessage();
                Toast.makeText(context, t.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RemoveRequest(int order_id) {

        Call<MessageResponse> call = serviceApi.deleteOrder(order_id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });
    }

    private void createDialog(int id) {
        myDialogAddOrder = new MyDialogAddOrder(context, new Dialoginterface() {
            @Override
            public void yes(String massage) {
                AddRequest(id, massage);

            }
        });

    }
}