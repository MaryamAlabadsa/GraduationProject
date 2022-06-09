package com.example.graduationproject.fragments.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.database.DatabaseClient;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.dialog.DialogCheckedInterface;
import com.example.graduationproject.dialog.Dialoginterface;
import com.example.graduationproject.dialog.MyDialogAddOrder;
import com.example.graduationproject.dialog.MyDialogChecked;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.MyTitleEventBus;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostDetialsInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
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
    MyDialogChecked myDialogChecked;
    private FragmentSwitcher fragmentSwitcher;
    List<Category> data;
    int readInt;
    Intent intent;
    int page = 1, limit = 10;

    boolean isLoading = false;
    boolean isLastPage = true;
    int lastVisibleItem;
    int totalItemCount;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    PostsAdapter adapter;
    private boolean connected;

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

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            clearDataFromTable();
            connected = true;
        } else {
            Log.e("read3", "read1");

            connected = false;
            readFromOrdersTable();
//            showErrorDialog();
        }

        context = getActivity();
        readInt = sharedPreferences.readInt(AppSharedPreferences.IS_DONATION);
        //event bus
        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ALL_POSTS, "All post"));

        isDonation(readInt);

        binding.filterChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFilterDialog();
                Log.e("toasty", "toasty");
//                Toasty.error(context, R.string.filed_operation).show();

            }
        });


        setPostsRv(new ArrayList<>());

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
//                    binding.isDonationLinear.setVisibility(View.VISIBLE);
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

    private void getPostsByCategory(int id, int page, int limit) {

        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), id + "");

        Call<AllPosts> call = serviceApi.getPostByCategory(
                "Bearer " + token, category_id, readInt, page, limit);
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
        adapter = new PostsAdapter(context, new PostDetialsInterface() {
            @SuppressLint("CheckResult")
            @Override
            public void layout(int id) {

                UtilMethods.getPostDetails(id, context, serviceApi, token);

            }
        }, new PostAddOrderInterface() {
            @Override
            public void layout(Post post, int position) {
                if (post.getIsHeTheOwnerOfThePost()) {
                    PostOrdersInfo info = new PostOrdersInfo(post.getId(), post.getIsCompleted(), post.getIsDonation(), post.getSecondUserId());
                    fragmentSwitcher.switchFragment(PagesFragment.POST_ORDERS, info);
                } else {
                    createDialog(post.getId(), post, position);
                    myDialogAddOrder.show();
                }

            }
        }, new PostRemoveOrderInterface() {
            @Override
            public void layout(Post post, int position) {
                showDialog();
                RemoveRequest(post.getOrderId(), post, position);
            }
        }, new UserIdtRequestInterface() {
            @Override
            public void layout(int userId) {
                fragmentSwitcher.switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(userId));
            }
        });
        adapter.setList(postList);
        binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                Log.e("totalItemCount", totalItemCount + "");
                Log.e("lastVisibleItem", lastVisibleItem + "");
                if (lastVisibleItem == (totalItemCount - 1) && !isLoading && totalItemCount != 0 && !isLastPage) {
                    page++;
                    getPostDividedByIsDonation(1, page, limit);
                    Log.e("listSize", "done");
                }
            }
        });
        binding.rvPost.setAdapter(adapter);
        Log.e("rv2", postList.size() + "");
    }


    private void getPostDividedByIsDonation(int id, int page, int limit) {
        isLoading = true;

        Call<AllPosts> call = serviceApi.getPostDividedByIsDonation(
                "Bearer " + token, id, page, limit);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response code", response.code() + "");
                isLoading = false;
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
//                    setPostsRv(getAllPosts.getData());
                    if (page == 1) {
                        adapter.setList(getAllPosts.getData());
                    } else
                        adapter.addToList(getAllPosts.getData());
                    if (response.body().getMeta().getLastPage() == page) {
                        isLastPage = true;
                        Log.e("lastPage", isLastPage + "");
                    } else {
                        isLastPage = false;

                    }
                    storeInOrdersTable(getAllPosts.getData());
                    binding.shimmerView.setVisibility(View.INVISIBLE);
                    binding.dataLayout.setVisibility(View.VISIBLE);
                    //                    binding.progressBar.setVisibility(View.GONE);
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


    private void setCategoryRv(List<Category> data) {

        data.add(0, new Category(0, "All", R.drawable.all_category));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false);
        binding.rvCategory.setLayoutManager(layoutManager);
        CategoryAdapter adapter = new CategoryAdapter(context, new CategoryInterface() {
            @Override
            public void layout(int id) {
                if (id == 0) {
                    sharedPreferences.writeInt(AppSharedPreferences.IS_DONATION, 0);
                    showDialog();
                    getPostDividedByIsDonation(1, page, limit);
                } else {
                    sharedPreferences.writeInt(AppSharedPreferences.IS_DONATION, id);
                    showDialog();
                    getPostsByCategory(id, page, limit);

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

    public void AddRequest(int id_post, String massage, Post post, int position) {

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
                changeAddButton(post, position);
                myDialogAddOrder.dismiss();
                Toasty.success(context, R.string.success_operation);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.getMessage();
                myDialogAddOrder.dismiss();
                Toasty.error(context, R.string.filed_operation);
            }
        });
    }

    private void changeAddButton(Post post, int position) {
        post.setIsOrdered(true);
        adapter.resetItem(post, position);
    }

    private void changeRemoveButton(Post post, int position) {
        post.setIsOrdered(false);
        adapter.resetItem(post, position);
    }


    private void RemoveRequest(int order_id, Post post, int position) {

        Call<MessageResponse> call = serviceApi.deleteOrder(order_id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                changeRemoveButton(post, position);
                progressDialog.dismiss();
                Toasty.success(context, R.string.success_operation);

            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toasty.error(context, R.string.filed_operation);
            }
        });
    }

    private void createDialog(int id, Post post, int position) {
        myDialogAddOrder = new MyDialogAddOrder(context, new Dialoginterface() {
            @Override
            public void yes(String massage) {
                AddRequest(id, massage, post, position);

            }
        });

    }

    private void createFilterDialog() {
        myDialogChecked = new MyDialogChecked(context, new DialogCheckedInterface() {
            @Override
            public void yes(int isDonation) {
                isDonation(isDonation);
                showDialog();
            }
        });
        myDialogChecked.show();
    }

    private void isDonation(int isDonation) {
        int post_category = sharedPreferences.readInt(AppSharedPreferences.POST_CATEGORY);

        if (isDonation == 0) {
            binding.filterText.setText("Donation Posts");
        } else {
            binding.filterText.setText("Request Posts");
        }
        if (post_category == 0)
            getPostDividedByIsDonation(isDonation, page, limit);
        else
            getPostsByCategory(post_category, page, limit);
    }

    private void storeInOrdersTable(List<Post> posts) {
        Log.e("read1", "read1");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("read1", "read1");


                long[] i = DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .postDao()
                        .insertToPostList(posts);
                Log.e("inserst " + i, "Inserted");
            }
        });
        thread.start();
    }

    private void clearDataFromTable() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .postDao()
                        .clearAllData();
                Log.e("clearAllData ", "Done");
                getAllCategories();

            }
        });
        thread.start();
    }

    private void readFromOrdersTable() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Post> ordersList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .postDao()
                        .readAll();
                Log.e("read2", ordersList.size() + "");
                if (ordersList.size() != 0) {
                    //this code is to run my code in main thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addToList(ordersList);
                        }
                    });
                }
            }
        });
        thread.start();
    }
}