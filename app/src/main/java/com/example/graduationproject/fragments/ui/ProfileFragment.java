package com.example.graduationproject.fragments.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.activities.MainActivity;
import com.example.graduationproject.adapters.CategoryAdapter;
import com.example.graduationproject.adapters.PostsAdapter;
import com.example.graduationproject.adapters.ProfilePostsAdapter;
import com.example.graduationproject.database.DatabaseClient;
import com.example.graduationproject.databinding.FragmentProfileBinding;
import com.example.graduationproject.dialog.Dialoginterface;
import com.example.graduationproject.dialog.MyDialogAddOrder;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.MyTitleEventBus;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.listener.CategoryInterface;
import com.example.graduationproject.listener.PostAddOrderInterface;
import com.example.graduationproject.listener.PostDetialsInterface;
import com.example.graduationproject.listener.PostImageShowInterface;
import com.example.graduationproject.listener.PostMenuInterface;
import com.example.graduationproject.listener.PostProfileAddOrderInterface;
import com.example.graduationproject.listener.PostProfileMenuInterface;
import com.example.graduationproject.listener.PostProfileRemoveOrderInterface;
import com.example.graduationproject.listener.PostRemoveOrderInterface;
import com.example.graduationproject.listener.UserIdtRequestInterface;
import com.example.graduationproject.model.PostOrdersInfo;
import com.example.graduationproject.retrofit.Creator;
import com.example.graduationproject.retrofit.ServiceApi;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.profile.donation.posts.PostsList;
import com.example.graduationproject.retrofit.profile.donation.posts.ProfilePosts;
import com.example.graduationproject.retrofit.profile.user.info.UserProfileInfo;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.FileUtil;
import com.example.graduationproject.utils.UtilMethods;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kodmap.app.library.PopopDialogBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "ProfileFragment";
    Context context;
    FragmentProfileBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private FragmentSwitcher fragmentSwitcher;
    MyDialogAddOrder myDialogAddOrder;
    ProfilePostsAdapter adapter;
    Dialog imageDialog;
    Bitmap bitmap;
    // TODO: Rename and change types of parameters
    private int userId;
    private SweetAlertDialog pDialog;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int param1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();
        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.PROFILE, "profile"));
        serviceApi = Creator.getClient().create(ServiceApi.class);
        sharedPreferences = new AppSharedPreferences(getActivity().getApplicationContext());
        token = sharedPreferences.readString(AppSharedPreferences.AUTHENTICATION);
        getProfileData();
        binding.btnDonationPost.setOnClickListener(this::onClick);
        binding.btnRequestPost.setOnClickListener(this::onClick);
        binding.editUserName.setOnClickListener(this::onClick);
        binding.saveEditUserName.setOnClickListener(this::onClick);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        if (userId == 0)
            binding.editUserName.setVisibility(View.VISIBLE);

        return view;
    }

    private void getProfileData() {
        if (userId == 0) {
            changeUserImage();
            setMyProfileInfo();
        } else {
            binding.changeProfileImageBtn.setVisibility(View.INVISIBLE);
            setUserProfileInfo();
        }
    }

    private void setMyProfileInfo() {

        Call<UserProfileInfo> call = serviceApi.getMyProfileInfo("Bearer " + token);
        call.enqueue(new Callback<UserProfileInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<UserProfileInfo> call, Response<UserProfileInfo> response) {
                Log.d("response1 code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    Glide.with(context).load(response.body().getData().getUserImage()).circleCrop()
                            .placeholder(R.drawable.usericon).into(binding.profileImage);
                    binding.userNameText.setText(response.body().getData().getUserName());
                    binding.tvDonationPostsNum.setText(response.body().getData().getNumDonationPost() + "");
                    binding.tvRequestPostsNum.setText(response.body().getData().getNumRequestPost() + "");
                    getMyRequestPosts();
                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<UserProfileInfo> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void setUserProfileInfo() {

        Call<UserProfileInfo> call = serviceApi.getUserProfileInfo(userId, "Bearer " + token);
        call.enqueue(new Callback<UserProfileInfo>() {
            @Override
            public void onResponse(Call<UserProfileInfo> call, Response<UserProfileInfo> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    Glide.with(context).load(response.body().getData().getUserImage()).circleCrop()
                            .placeholder(R.drawable.usericon).into(binding.profileImage);
                    binding.userNameText.setText(response.body().getData().getUserName());
                    binding.tvDonationPostsNum.setText(response.body().getData().getNumDonationPost() + "");
                    binding.tvRequestPostsNum.setText(response.body().getData().getNumRequestPost() + "");
                    getUserRequestPosts(userId);
                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<UserProfileInfo> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getMyRequestPosts() {
        Call<ProfilePosts> call = serviceApi.getMyRequestsPosts("Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
                    binding.containerAll.setVisibility(View.VISIBLE);
                    binding.shimmerView.setVisibility(View.GONE);
                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getMyDonationPosts() {
        Call<ProfilePosts> call = serviceApi.getMyDonationPosts("Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());

                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getUserRequestPosts(int userId) {
        Call<ProfilePosts> call = serviceApi.getUserRequestPosts(userId, "Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
                    binding.containerAll.setVisibility(View.VISIBLE);
                    binding.shimmerView.setVisibility(View.GONE);
                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getUserDonationPosts(int userId) {
        Call<ProfilePosts> call = serviceApi.getUserDonationPosts(userId, "Bearer " + token);
        call.enqueue(new Callback<ProfilePosts>() {
            @Override
            public void onResponse(Call<ProfilePosts> call, Response<ProfilePosts> response) {
                Log.d("response1 code", response.code() + "");

                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    setRvData(response.body().getData().getPostsList());
//                    binding.progressBar.setVisibility(View.GONE);
//                    binding.getRoot().setVisibility(View.VISIBLE);

                } else {
//                    String errorMessage
//                    = parseError(response);
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<ProfilePosts> call, Throwable t) {
                Log.d("onFailure2", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void setRvData(List<PostsList> data) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                context, RecyclerView.VERTICAL, false);
        binding.rvPosts.setLayoutManager(layoutManager);

        adapter = new ProfilePostsAdapter(context, new PostDetialsInterface() {
            @SuppressLint("CheckResult")
            @Override
            public void layout(int id) {

                UtilMethods.getPostDetails(id, context, serviceApi, token,fragmentSwitcher);

            }
        }, new PostProfileAddOrderInterface() {
            @Override
            public void layout(PostsList post, int position) {
                if (post.getIsHeTheOwnerOfThePost()) {
                    PostOrdersInfo info = new PostOrdersInfo(post.getId(), post.getIsCompleted(), post.getIsDonation(), post.getSecondUserId());
                    fragmentSwitcher.switchFragment(PagesFragment.POST_ORDERS, info, null);
                } else {
//                    createAddDialog(post.getId(), post, position);
                    createAddOrderDialog(post.getId(), post, position);
                    myDialogAddOrder.show();
                }
            }
        }, new PostProfileRemoveOrderInterface() {
            @Override
            public void layout(PostsList post, int position) {
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
                imageDialog.show();


            }
        }, new PostProfileMenuInterface() {
            @Override
            public void layoutPost(PostsList post, int position, View v) {
                showPopupMenuForPost(v, post, post.getId(), position);
            }

            @Override
            public void layoutOrder(PostsList post, int position, View v) {
                showPopupMenuForOrder(v, post, post.getId(), position);
            }
        });

        adapter.setList(data);
        binding.rvPosts.setAdapter(adapter);
    }

    @Override
    public int getFragmentTitle() {
        return R.string.see_your_profile;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //when user click rating button
            case R.id.btn_donation_post:
                if (userId == 0)
                    getMyDonationPosts();
                else
                    getUserDonationPosts(userId);
                adapter.clearItems();
                Animation anim2 = AnimationUtils.loadAnimation(context, R.anim.anim2);
                binding.lineView.startAnimation(anim2);
                break;

            case R.id.btn_request_post:
                if (userId == 0)
                    getMyRequestPosts();
                else
                    getUserRequestPosts(userId);
                adapter.clearItems();
                Animation anim = AnimationUtils.loadAnimation(context, R.anim.anim);
                binding.lineView.startAnimation(anim);
                break;
            case R.id.edit_user_name:
                binding.userNameText.setClickable(true);
                binding.userNameText.setEnabled(true);
                binding.editUserName.setVisibility(View.GONE);
                binding.saveEditUserName.setVisibility(View.VISIBLE);
                break;
            case R.id.save_edit_user_name:
                binding.userNameText.setClickable(false);
                binding.userNameText.setEnabled(false);
                binding.editUserName.setVisibility(View.VISIBLE);
                binding.saveEditUserName.setVisibility(View.GONE);
//                binding.progressBar.setVisibility(View.VISIBLE);
                String newName = binding.userNameText.getText().toString();
                updateUserName(newName);
                break;
        }
    }

    private void updateUserName(String newName) {
        Call<RegisterResponse> call = serviceApi.updateUserName(
                "Bearer " + token, newName
        );

        call.enqueue(new Callback<RegisterResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("response5 code", response.code() + "");
                User user = response.body().getData().getUser();
                Gson gson = new Gson();
                String userString = gson.toJson(user);
                sharedPreferences.writeString(AppSharedPreferences.USER, userString);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;

    }
    ///-----------------------------add request ----------------------------------


    private void createAddOrderDialog(int id, PostsList post, int position) {
        myDialogAddOrder = new MyDialogAddOrder(context, "", new Dialoginterface() {
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
        myDialogAddOrder.show();
    }

    public void AddRequest(int id_post, String massage, PostsList post, int position) {

        RequestBody post_id = RequestBody.create(MediaType.parse("multipart/form-data"), id_post + "");
        RequestBody mPost = RequestBody.create(MediaType.parse("multipart/form-data"), massage);

        Call<Order> call = serviceApi.addRequest(
                "Bearer " + token
                , post_id
                , mPost);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
//                Log.d("response5 code", response.body().getData() + "");
                pDialog.dismiss();
                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                post.setOrderId(response.body().getData().getId());
                changeAddButton(post, position);
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void changeAddButton(PostsList post, int position) {
        post.setIsOrdered(true);
        adapter.modifyBtn(post, position);
    }

    //--------------------------remove request-----------------------------
    private void removeOrderDialog(int order_id, PostsList post, int position) {
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


    private void changeRemoveButton(PostsList post, int position) {
        post.setIsOrdered(false);
        adapter.modifyBtn(post,position);
//        adapter.restorePost(post, position);
    }

    private void removeRequest(int order_id, PostsList post, int position) {
        Call<MessageResponse> call = serviceApi.deleteOrder(order_id,
                "Bearer " + token
        );
        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                changeRemoveButton(post, position);
//                adapter.modifyBtn(post,position);
//                binding.progressBar.setVisibility(View.GONE);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toasty.error(context, R.string.filed_operation).show();
            }
        });
    }

    private void createDialog(int id, PostsList post, int position) {
        myDialogAddOrder = new MyDialogAddOrder(context, "", new Dialoginterface() {
            @Override
            public void yes(String massage) {
                AddRequest(id, massage, post, position);
            }
        });
    }

    private void postImageDialog(List<String> url_list) {
        imageDialog = new PopopDialogBuilder(context)
                // Set list like as option1 or option2 or option3
                .setList(url_list, 0)
                // or setList with initial position that like .setList(list,position)
                // Set dialog header color
                .setHeaderBackgroundColor(R.color.color_app)
                // Set dialog background color
                .setDialogBackgroundColor(R.color.color_dialog_bg)
                // Set close icon drawable
                .setCloseDrawable(R.drawable.ic_close_white_24dp)
                // Set loading view for pager image and preview image
            .setLoadingView(R.layout.crop_image_view)
                // Set dialog style
            .setDialogStyle(R.style.alert_dialog_dark)
                // Choose selector type, indicator or thumbnail
                .showThumbSlider(true)
                // Set image scale type for slider image
                .setSliderImageScaleType(ImageView.ScaleType.FIT_XY)
                // Set indicator drawable
//            .setSelectorIndicator(R.drawable.sample_indicator_selector)
                // Enable or disable zoomable
                .setIsZoomable(true)
                // Build Km Slider Popup Dialog
                .build();
    }


    // change user image
    File file;

    private void changeUserImage() {
        binding.editUserImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else
                        new ImagePicker.Builder(getActivity())
                                .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start();

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new ImagePicker.Builder(this)
                .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }


    private void changeUserImageRequest() {
        //showDialog();
        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        Call<RegisterResponse> call = serviceApi.updateUserImage("application/json", body, "Bearer " + token);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful() || response.code() == 200) {
                    assert response.body() != null;
                    UtilMethods.launchLoadingLottieDialogDismiss(context);
                    binding.profileImage.setImageBitmap(bitmap);
                    User user = response.body().getData().getUser();
                    Gson gson = new Gson();
                    String userString = gson.toJson(user);
                    sharedPreferences.writeString(AppSharedPreferences.USER, userString);
                    //progressDialog.dismiss();
                    Log.e("image link", user.getImageLink() + "");
                } else {
//                    String errorMessage = parseError2(response);
//                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
//                    //progressDialog.dismiss();
//                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
                //progressDialog.dismiss();
            }
        });
    }

    private void uploadImage() {
        UtilMethods.launchLoadingLottieDialog(context);
        changeUserImageRequest();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                file = FileUtil.from(context, uri);
                uploadImage();
            } catch (IOException e) {
                Log.w(TAG, "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE => ", e);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            Uri uri = data.getData();
            //this is written from a fragment.
            CropImage.activity(uri).setAspectRatio(83, 100)
                    .setRequestedSize(233, 280)
                    .setGuidelines(CropImageView.Guidelines.ON).start(getActivity());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), resultUri);
                    uploadImage();
                } catch (IOException e) {
                    Log.w(TAG, "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE => ", e);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.w(TAG, "onActivityResult: ", error);
            }
        }
    }

    //-------------------------------------delete post and undo-----------------------------------------
    private void deletePostAndUndo(PostsList post, int id, int position) {
        createDeletePostDialog(post, id, position);
    }

    private void createDeletePostDialog(PostsList post, int id, int position) {
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

    private void deletePostRequest(PostsList post, int id, int position) {
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

    private void removePostFromRv(PostsList post, int id, int position) {
        adapter.removeItem(position);
        showSnakBarToUndoPost(post, id, position);
//        UtilMethods.launchLoadingLottieDialogDismiss(context);
    }

    private void showSnakBarToUndoPost(PostsList post, int id, int position) {
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.restorePost(post, position);
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
                binding.rvPosts.scrollToPosition(position);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });

    }

    //-------------------------------------delete order and undo-----------------------------------------
    private void deleteOrderAndUndo(PostsList post, int id, int position) {
        createDeleteOrderDialog(post, id, position);
    }

    private void createDeleteOrderDialog(PostsList post, int id, int position) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure you wanna delete this order ?")
//                .setContentText("you Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        UtilMethods.launchLoadingLottieDialog(context);
                        deleteOrderRequest(post, id, position);
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void deleteOrderRequest(PostsList post, int id, int position) {
        Call<MessageResponse> call = serviceApi.deleteOrder(id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                UtilMethods.launchLoadingLottieDialogDismiss(context);
                removeOrderFromRv(post, id, position);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });
    }

    private void removeOrderFromRv(PostsList post, int id, int position) {
        adapter.removeItem(position);
        showSnakBarToUndoOrder(post, id, position);
    }

    private void showSnakBarToUndoOrder(PostsList post, int id, int position) {
        Snackbar snackbar = Snackbar
                .make(binding.getRoot(), "Item was removed from the list.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.restorePost(post, position);
                restoreOrderRequest(id, position);
                UtilMethods.launchLoadingLottieDialog(context);
            }
        });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void restoreOrderRequest(int id, int position) {
        Call<MessageResponse> call = serviceApi.restoreOrder(id,
                "Bearer " + token
        );

        call.enqueue(new Callback<MessageResponse>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                UtilMethods.launchLoadingLottieDialogDismiss(context);
                binding.rvPosts.scrollToPosition(position);
            }

            @SuppressLint("CheckResult")
            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
            }
        });
    }
    //----------------------------------show pop up menu ------------------------------

    @SuppressLint("RestrictedApi")
    public void showPopupMenuForPost(View v, PostsList post, int id, int position) {
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
                    EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.EDIT, postString));

                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    @SuppressLint("RestrictedApi")
    public void showPopupMenuForOrder(View v, PostsList post, int id, int position) {
        PopupMenu menu = new PopupMenu(context, v);
        menu.inflate(R.menu.post_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_post) {
                    deleteOrderAndUndo(post, id, position);
                } else if (item.getItemId() == R.id.edit_post) {
                    createEditOrderDialog(id, post, position);
//                    myDialogAddOrder.show();
                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) menu.getMenu(), v);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    //------------------------------edit order ------------------------------------------------------
    private void createEditOrderDialog(int id, PostsList post, int position) {
        String message=post.getMassage()+"";
        myDialogAddOrder = new MyDialogAddOrder(context,message , new Dialoginterface() {
            @Override
            public void yes(String massage) {
                EditRequest(id, post.getPost().getId(), massage, post, position);
                myDialogAddOrder.dismiss();
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
                pDialog.setTitleText("Loading ...");
                pDialog.setCancelable(true);
                pDialog.show();
            }
        });
        myDialogAddOrder.show();

    }

    private void EditRequest(int id_order, int id_post, String massage, PostsList post, int position) {

        Call<MessageResponse> call = serviceApi.editOrder(id_order,
                "Bearer " + token
                , massage);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                post.setMassage(massage);
                adapter.modifyBtn(post,position);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.getMessage();
                myDialogAddOrder.dismiss();
            }
        });
    }

//    private void storeInOrdersTable(List<Post> posts) {
//        Log.e("read1", "read1");
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("read1", "read1");
//
//
//                long[] i = DatabaseClient.getInstance(context)
//                        .getAppDatabase()
//                        .postDao()
//                        .insertToPostList(posts);
//                Log.e("inserst " + i, "Inserted");
//            }
//        });
//        thread.start();
//    }
//
//    private void clearDataFromTable() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                DatabaseClient.getInstance(context)
//                        .getAppDatabase()
//                        .postDao()
//                        .clearAllData();
//                Log.e("clearAllData ", "Done");
//
//            }
//        });
//        thread.start();
//    }
//
//    private void readFromOrdersTable() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Post> ordersList = DatabaseClient
//                        .getInstance(context)
//                        .getAppDatabase()
//                        .postDao()
//                        .readAll();
//                Log.e("read2", ordersList.size() + "");
//                if (ordersList.size() != 0) {
//                    //this code is to run my code in main thread
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.addToList(ordersList);
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();
//    }

}