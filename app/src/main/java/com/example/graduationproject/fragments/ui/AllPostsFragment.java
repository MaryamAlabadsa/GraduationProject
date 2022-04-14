package com.example.graduationproject.fragments.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostRequestInterface;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
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
        showDialog();
        getAllCategories();
        getAllPosts();

        return view;



    }

    int isDonation = -1;


    public void rbClick() {
        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
        int radioButtonID = binding.radioGroup.getCheckedRadioButtonId();
//        RadioButton radioButton =(RadioButton) findViewById(radioButtonID);
//        int idx = binding.radioGroup.indexOfChild(radioButton);
//        RadioButton r = (RadioButton) binding.radioGroup.getChildAt(idx);
//        String selectedtext = radioButton.getText().toString();
//        if (selectedtext.equals("Donation")) {
//            Toast.makeText(context, "Donation", Toast.LENGTH_SHORT).show();
//            showDialog();
//            getPostDividedByIsDonation(1);
//        } else if (selectedtext.equals("Request")) {
//            Toast.makeText(context, "Request", Toast.LENGTH_SHORT).show();
//            showDialog();
//            getPostDividedByIsDonation(0);
//        }
        Toast.makeText(context, radioButtonID+"1", Toast.LENGTH_SHORT).show();


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
            }
        });
    }

    private void getAllPosts() {
        Call<AllPosts> call = serviceApi.getAllPosts("Bearer " + token);
        Toast.makeText(context, "posts", Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
                    setPostsRv(response.body().getData());
                    Log.e("maryam", getAllPosts.getData().size() + "");
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
        Log.e("rv2", postList.size() + "");



    }

    private void setCategoryRv(List<Category> data) {
//        Log.e("setCategoryRv", data.get(0).getName());
//        getAllCategories();

        data.add(0, new Category(0, "All"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(context, new CategoryInterface() {
            @Override
            public void layout(int id) {
                Toast.makeText(context, id + "", Toast.LENGTH_SHORT).show();
                if (id == 0)
                    getAllPosts();
                else
                    getPostsByCategory(id);
            }
        });
        adapter.setList(data);
        binding.rvCategory.setAdapter(adapter);
    }




}