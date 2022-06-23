package com.example.graduationproject.fragments.ui;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.graduationproject.fragments.PagesFragment.ADD_POSTS;
import static com.example.graduationproject.fragments.PagesFragment.ALL_POSTS;
import static com.example.graduationproject.fragments.PagesFragment.CHANGE_PASSWORD;
import static com.example.graduationproject.fragments.PagesFragment.NOTIFICATION;
import static com.example.graduationproject.fragments.PagesFragment.PROFILE;
import static com.example.graduationproject.fragments.PagesFragment.SEARCH;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.example.graduationproject.R;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.database.DatabaseClient;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.databinding.LayoutDialogAddOrderBinding;
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
import com.example.graduationproject.listener.PostImageShowInterface;
import com.example.graduationproject.listener.PostMenuInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.Constant;
import com.example.graduationproject.utils.UtilMethods;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kodmap.app.library.PopopDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    LayoutDialogAddOrderBinding bindingD;
    Context context;
    Dialoginterface dialoginterface;
    SweetAlertDialog pDialog;
    FragmentAllPostsBinding binding;
    MyDialogAddOrder myDialogAddOrder;
    MyDialogChecked myDialogChecked;
    private FragmentSwitcher fragmentSwitcher;
    List<Category> data;
    //    int readInt;
    Intent intent;
    int page = 1, limit = 10;

    boolean isLoading = false;
    boolean isLastPage = true;
    int lastVisibleItem;
    int totalItemCount;


    //filter donation
    Constant checkedNum = Constant.ALL_CHECKED;
    Constant postStatus = Constant.ALL_CHECKED;
    Constant selectedCategory = Constant.NO_SELECTED_CATEGORY;
    int categoryId;

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
        context = getActivity();
        EventBus.getDefault().register(this);
        checkNetwork(context);
        binding.shimmerView.setVisibility(View.VISIBLE);

        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ALL_POSTS, "All post"));

        filterDialog();
        setPostsRv(new ArrayList<>());
        swipeToRefresh();
        return view;
    }

    private void filterDialog() {
        binding.filterChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFilterDialog();
            }
        });
    }

    private void swipeToRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = binding.scroll;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                binding.shimmerView.setVisibility(View.VISIBLE);
                binding.dataLayout.setVisibility(View.GONE);
//                getAllCategories();
                checkNetwork(context);
            }, 1000);
        });
    }

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

                //progressDialog.dismiss();
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
                    dismissWaitingImage();
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    //progressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
                //progressDialog.dismiss();


            }
        });
    }

    private void getPostsByCategory(Constant checkedNum, int CategoryId, int page, int limit) {

        int donationId = 2, postStatusId = 2;
        if ((checkedNum == Constant.DONATION_CHECKED)) {
            donationId = 1;
        } else if (checkedNum == Constant.REQUEST_CHECKED) {
            donationId = 0;
        }
        if ((postStatus == Constant.PENDING_POST)) {
            postStatusId = 0;
        } else if ((postStatus == Constant.COMPLETE_POST)) {
            postStatusId = 1;
        }

        RequestBody selectedCategoryId = RequestBody.create(MediaType.parse("multipart/form-data"), CategoryId + "");
        RequestBody postStatusO = RequestBody.create(MediaType.parse("multipart/form-data"), postStatusId + "");

        Call<AllPosts> call = serviceApi.getPostByCategory(
                "Bearer " + token, selectedCategoryId, donationId, postStatusO, page, limit);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response3 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    AllPosts getAllPosts = response.body();
                    setPostsRv(getAllPosts.getData());
                    dismissWaitingImage();
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
                    fragmentSwitcher.switchFragment(PagesFragment.POST_ORDERS, info, null);
                } else {
                    createAddOrderDialog(post.getId(), post, position);
                    myDialogAddOrder.show();
                }

            }
        }, new PostRemoveOrderInterface() {
            @Override
            public void layout(Post post, int position) {
                //showDialog();
//                RemoveRequest(post.getOrderId(), post, position);
                removeOrderDialog(post.getOrderId(), post, position);
            }
        }, new UserIdtRequestInterface() {
            @Override
            public void layout(int userId) {

                fragmentSwitcher.switchFragment(PagesFragment.PROFILE, new PostOrdersInfo(userId), null);
            }
        }, new PostImageShowInterface() {
            @Override
            public void layout(List<String> images) {
                postImageDialog(images);

            }
        }, new PostMenuInterface() {
            @Override
            public void layout(Post post, int position, View v) {
                showPopupMenu(v, post, post.getId(), position);
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

                if (lastVisibleItem == (totalItemCount - 1) && !isLoading && totalItemCount != 0 && !isLastPage) {
                    page++;
                    setSelectedCategory(checkedNum, postStatus);
                }
            }
        });
        binding.rvPost.setAdapter(adapter);
        binding.shimmerView.setVisibility(View.INVISIBLE);
        binding.dataLayout.setVisibility(View.VISIBLE);
        Log.e("rv2", postList.size() + "");
    }


    private void getPostDividedByIsDonation(Constant checkedNum, Constant postStatus, int page, int limit) {
        isLoading = true;
        int donationId = 2, postStatusId = 2;
        if ((checkedNum == Constant.DONATION_CHECKED)) {
            donationId = 1;
        } else if (checkedNum == Constant.REQUEST_CHECKED) {
            donationId = 0;
        }

        if ((postStatus == Constant.PENDING_POST)) {
            postStatusId = 0;
        } else if ((postStatus == Constant.COMPLETE_POST)) {
            postStatusId = 1;
        }

        Call<AllPosts> call = serviceApi.getPostDividedByIsDonation(
                "Bearer " + token, donationId, postStatusId, page, limit);
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
                    dismissWaitingImage();
//                    storeInOrdersTable(getAllPosts.getData());
//                    binding.shimmerView.setVisibility(View.INVISIBLE);
//                    binding.dataLayout.setVisibility(View.VISIBLE);
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
                categoryId = id;

                if (id == 0) {
                    selectedCategory = Constant.NO_SELECTED_CATEGORY;
                    setSelectedCategory(checkedNum, postStatus);
                } else {
                    selectedCategory = Constant.SELECTED_CATEGORY;
                    setSelectedCategory(checkedNum, postStatus);
                }


            }
        });
        setSelectedCategory(checkedNum, postStatus);

        adapter.setList(data);
        binding.rvCategory.setAdapter(adapter);
    }

    //---------------------------------onAttach--------------------------------------------
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;

    }


    //----------------------------remove order ------------------------------------------------
    private void changeRemoveButton(Post post, int position) {
        post.setIsOrdered(false);
        adapter.modifyBtn(post, position);
    }

    private void removeOrderDialog(int order_id, Post post, int position) {
        Button button = new Button(context);
        button.setText("Retry");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        int purpleColor = ContextCompat.getColor(context, R.color.green);
        button.setBackgroundTintList(ColorStateList.valueOf(purpleColor));
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("Deleted!")
                                .setContentText("Your imaginary file has been deleted!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        removeRequest(order_id, post, position);
                    }
                })
                .show();
    }

    private void removeRequest(int order_id, Post post, int position) {

        Call<MessageResponse> call = serviceApi.deleteOrder(order_id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                changeRemoveButton(post, position);

            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });
    }
    //----------------------------add order ------------------------------------------------

    private void createAddOrderDialog(int id, Post post, int position) {
        myDialogAddOrder = new MyDialogAddOrder(context,"", new Dialoginterface() {
            @Override
            public void yes(String massage) {
                AddRequest(id, massage, post, position);
                myDialogAddOrder.dismiss();
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
                pDialog.setTitleText("Loading ...");
                pDialog.setCancelable(true);
                pDialog.show();
            }
        });

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
                pDialog.dismiss();
                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                changeAddButton(post, position);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void changeAddButton(Post post, int position) {
        post.setIsOrdered(true);
        adapter.modifyBtn(post, position);
    }

    //--------------------------------filter dialog -------------------------------

    private void createFilterDialog() {
        myDialogChecked = new MyDialogChecked(context, checkedNum, postStatus, new DialogCheckedInterface() {
            @Override
            public void yes(Constant isDonation, Constant postStatusDialog) {
                checkedNum = isDonation;
                postStatus = postStatusDialog;
                setSelectedCategory(isDonation, postStatus);
                showWaitingImage();
            }
        });
        myDialogChecked.show();
    }

    private void setSelectedCategory(Constant checkedBtn, Constant postStatus) {
        binding.dataLayout.setVisibility(View.VISIBLE);
        binding.shimmerView.setVisibility(View.GONE);
        showWaitingImage();
        getCheckedBtn(checkedBtn);
        if (selectedCategory == Constant.NO_SELECTED_CATEGORY) {
            if (checkedBtn == Constant.ALL_CHECKED && postStatus == Constant.ALL_CHECKED) {
                getAllPosts();
            } else {
                getPostDividedByIsDonation(checkedBtn, postStatus, page, limit);
            }
        } else {
            //if category selected
            getPostsByCategory(checkedNum, categoryId, page, limit);
        }

    }

    private void getCheckedBtn(Constant checkedBtn) {
        String mainTextTitle, secondaryTitle;
        if (checkedBtn == Constant.ALL_CHECKED) {
            mainTextTitle = "All Posts";
        } else if (checkedBtn == Constant.DONATION_CHECKED) {
            mainTextTitle = "Donation Posts";
        } else {
            mainTextTitle = "Request Posts";
        }
        if (postStatus == Constant.ALL_CHECKED) {
            secondaryTitle = "";
        } else if (postStatus == Constant.COMPLETE_POST) {
            secondaryTitle = " / complete post";
        } else {
            secondaryTitle = " / pending post";
        }
        binding.filterText.setText(mainTextTitle + secondaryTitle);

    }

    //--------------------------------image view piger
    private void postImageDialog(List<String> url_list) {
        Dialog dialog = new PopopDialogBuilder(context)
                .setList(url_list, 0)
                .setHeaderBackgroundColor(android.R.color.holo_blue_light)
                .setDialogBackgroundColor(R.color.color_dialog_bg)
                .setCloseDrawable(R.drawable.ic_close_white_24dp)
                .showThumbSlider(true)
                .setSliderImageScaleType(ImageView.ScaleType.FIT_XY)
                .setIsZoomable(true)
                .build();
        dialog.show();
    }

    //-------------------------------------delete post and undo-----------------------------------------
    private void deletePostAndUndo(Post post, int id, int position) {
        createDeletePostDialog(post, id, position);
    }

    private void createDeletePostDialog(Post post, int id, int position) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you ?")
//                .setContentText("you Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        UtilMethods.launchLoadingLottieDialog(context);
                        deletePostRequest(post, id, position);
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void deletePostRequest(Post post, int id, int position) {
        Call<MessageResponse> call = serviceApi.deletePost(id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                UtilMethods.launchLoadingLottieDialogDismiss(context);
                removePostFromRv(post, id, position);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });
    }

    private void removePostFromRv(Post post, int id, int position) {
        adapter.removeItem(position);
        showSnakBarToUndoPost(post, id, position);
//        UtilMethods.launchLoadingLottieDialogDismiss(context);
    }

    private void showSnakBarToUndoPost(Post post, int id, int position) {
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.restoreItem(post, position);
                restorePostRequest(id, position);
                UtilMethods.launchLoadingLottieDialog(context);
            }
        });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void restorePostRequest(int id, int position) {
        Call<MessageResponse> call = serviceApi.restorePost(id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                UtilMethods.launchLoadingLottieDialogDismiss(context);
                binding.rvPost.scrollToPosition(position);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });

    }
    //----------------------------------show pop up menu ------------------------------

    @SuppressLint("RestrictedApi")
    public void showPopupMenu(View v, Post post, int id, int position) {
        PopupMenu menu = new PopupMenu(context, v);
        menu.inflate(R.menu.post_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_post) {
                    deletePostAndUndo(post, id, position);
                } else if (item.getItemId() == R.id.edit_post) {
                    Gson gson = new Gson();
                    String postString = gson.toJson(post);
//                    EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.EDIT, postString));
                    fragmentSwitcher.switchFragment(ADD_POSTS,null, postString);

//                    openFragment(fragmentA);
//                    fragmentSwitcher.switchFragment(ADD_POSTS,null);

                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    //-----------------------------check internet-------------------------------
    public void launchInternetLottieDialog(Context context) {
        Button button = new Button(context);
        button.setText("Retry");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        int purpleColor = ContextCompat.getColor(context, R.color.green);
        button.setBackgroundTintList(ColorStateList.valueOf(purpleColor));

        LottieDialog dialog = new LottieDialog(context)
                .setAnimation(R.raw.no_internet)
                .setAutoPlayAnimation(true)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setMessage("You have no internet connection")
                .addActionButton(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                checkNetwork(context);
            }
        });

        dialog.show();
    }

    public void checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            getAllCategories();

        } else {
            launchInternetLottieDialog(context);
//            connected = false;

        }
    }

    //-------------------------------------search---------------------------------------
    @Subscribe
    public void onEvent(MyTitleEventBus event) {
        Log.e("onEvent Main activity", "onEvent");
        if (event.getType() == SEARCH) {
            showWaitingImage();
            searchPostRequest(event.getText());
        }
    }

    private void searchPostRequest(String data) {

        Toast.makeText(context, "search", Toast.LENGTH_SHORT).show();
        int donationId = 2, postStatusId = 2;
        if ((checkedNum == Constant.DONATION_CHECKED)) {
            donationId = 1;
        } else if (checkedNum == Constant.REQUEST_CHECKED) {
            donationId = 0;
        }
        if ((postStatus == Constant.PENDING_POST)) {
            postStatusId = 0;
        } else if ((postStatus == Constant.COMPLETE_POST)) {
            postStatusId = 1;
        }

        RequestBody postCategory = RequestBody.create(MediaType.parse("multipart/form-data"), categoryId + "");
        RequestBody postStatus = RequestBody.create(MediaType.parse("multipart/form-data"), postStatusId + "");
        RequestBody postDonation = RequestBody.create(MediaType.parse("multipart/form-data"), donationId + "");
        RequestBody postData = RequestBody.create(MediaType.parse("multipart/form-data"), data + "");

        Call<AllPosts> call = serviceApi.searchPost(postDonation
                , postCategory
                , postStatus
                , postData,
                "Bearer " + token);

        call.enqueue(new Callback<AllPosts>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("Success", new Gson().toJson(response.body()));
                AllPosts getAllPosts = response.body();
                assert response.body() != null;
                if (getAllPosts.getData().size() == 0) {
                    showSearchImage();
                } else {
                    dismissWaitingImage();
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
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
            }
        });

    }

    //--------------------------------waiting------------------------------
    private void showSearchImage() {
        binding.lottieImg.setAnimation(R.raw.no_result);
        binding.lottieImg.loop(true);
        binding.lottieImg.playAnimation();
        binding.lottieImg.setVisibility(View.VISIBLE);
        binding.rvPost.setVisibility(View.GONE);
    }

    private void showWaitingImage() {
        binding.lottieImg.setAnimation(R.raw.loading_pink);
        binding.lottieImg.loop(true);
        binding.lottieImg.playAnimation();
        binding.lottieImg.setVisibility(View.VISIBLE);
        binding.rvPost.setVisibility(View.GONE);
    }

    private void dismissWaitingImage() {
        binding.lottieImg.setVisibility(View.GONE);
        binding.rvPost.setVisibility(View.VISIBLE);
    }



}