package com.example.graduationproject.fragments.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.adapters.CategoriesAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.databinding.FragmentAddPostBinding;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPostsFragment extends BaseFragment {
    AppSharedPreferences sharedPreferences;
    String token;
    ServiceApi serviceApi;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ALL_POSTS";
    FragmentAllPostsBinding binding;
    Context context;
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
        context = getActivity();
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
//        setCategoryRv();
        getAllCategories();
        getAllPosts();
        return view;
    }

    @Override
    public int getFragmentTitle() {
        return R.string.allPosts;
    }

    List<Category> data;

    private void getAllCategories() {
//        data = new ArrayList<>();
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
                    binding.progressBar2.setVisibility(View.GONE);
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllCategories> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getAllPosts() {
//        data = new ArrayList<>();
        Call<AllPosts> call = serviceApi.getAllPosts(
                "Bearer " + token);
        Toast.makeText(context, "posts", Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response1 code", response.code() + "");
                Toast.makeText(context, response.code() +"", Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
                    List<Post> posts = getAllPosts.getData();
                    setPostsRv(posts);
                    binding.progressBar2.setVisibility(View.GONE);
                } else {
                    String errorMessage = parseError(response);
//                    Toast.makeText(context, errorMessage + "o", Toast.LENGTH_SHORT).show();
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
        PostsAdapter adapter = new PostsAdapter(context, new PostRequestInterface() {
            @Override
            public void layout(Post post) {

            }
        });
        adapter.setList(postList);
        binding.rvPost.setAdapter(adapter);
    }

    private void setCategoryRv(List<Category> data) {
//        Log.e("setCategoryRv", data.get(0).getName());
//        getAllCategories();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        CategoriesAdapter adapter = new CategoriesAdapter(context, new CategoryInterface() {
            @Override
            public void layout(int id) {
//                category_id = id;
//                Toast.makeText(context, id + "", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setList(data);
        binding.rvCategory.setAdapter(adapter);
    }
}