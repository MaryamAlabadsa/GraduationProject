package com.example.graduationproject.fragments.ui;

import static com.example.graduationproject.fragments.PagesFragment.EDIT;
import static com.example.graduationproject.fragments.PagesFragment.SEARCH;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.FragmentAddPostBinding;
import com.example.graduationproject.fragments.BaseFragment;
import com.example.graduationproject.fragments.FragmentSwitcher;
import com.example.graduationproject.fragments.MyTitleEventBus;
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.post.PostDetails;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.FileUtil;
import com.example.graduationproject.utils.UtilMethods;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
AddPostFragment extends BaseFragment {
    private static Post post;
    private static boolean isEdit = false;
    FragmentAddPostBinding binding;
    public static final String TAG = "ADD_POSTS";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String IMAGE1 = "image1";
    private static final String IMAGE2 = "image2";
    private static final String IMAGE3 = "image3";
    Context context;
    List<Category> categories;
    HashMap<String, Uri> imagesList;
    //    ArrayList<File> imagesList;
    int imageNum = 0;
    String pTitle, pDescription;
    int isDonation = 2;
    Integer category;
    private FragmentSwitcher fragmentSwitcher;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SweetAlertDialog pDialog;

    public AddPostFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        if (!param1.equals("")) {
            isEdit = true;
            Gson gson = new Gson();
            post = gson.fromJson(param1, Post.class);
        } else {
            post = new Post();
            isEdit = false;
        }

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentSwitcher = (FragmentSwitcher) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = getActivity();
        categories = new ArrayList<>();
        imagesList = new HashMap<String, Uri>();
//        post = new Post();
        getAllCategories();
        if (isEdit)
            setFields();
        else
            setAddImagesAction();

        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ADD_POSTS, "Add Post"));


        String user = sharedPreferences.readUser(AppSharedPreferences.USER);
        Gson gson = new Gson();
        if (!user.isEmpty()) {
            User user1 = gson.fromJson(user, User.class);
            Glide.with(context).load(user1.getImageLink()).circleCrop()
                    .placeholder(R.drawable.ic_launcher_foreground).into(binding.imageProfile);
            binding.userName.setText(user1.getName());
        }


        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //showDialog();
                pTitle = binding.titlePost.getText().toString();
                pDescription = binding.descriptionPost.getText().toString();

                if (validationAllFields().equals("")) {
                    if (binding.radioDon.isChecked())
                        isDonation = 1;
                    else if (binding.radioReq.isChecked())
                        isDonation = 0;
                    if (isDonation != 2) {
                        UtilMethods.launchLoadingLottieDialog(context);
                        if (isEdit)
                            editPostRequest(imagesList, pTitle, pDescription, category, isDonation);
                        else addPostRequest(imagesList, pTitle, pDescription, category, isDonation);
                    } else {
                        Toast.makeText(context, "What is the post status?", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFields();
            }
        });
        return view;
    }

    private void uploadImage1(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
        Uri uri = imagesList.get("image" + 1);
        File file = null;
        try {
            file = FileUtil.from(context, uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data")
                            , file);
            body = MultipartBody.Part.createFormData(
                    "assets[" + 1 + "]", file.getName(), requestFile);
            resourceBody.add(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadImage2(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
        Uri uri = imagesList.get("image" + 2);
        File file = null;
        try {
            file = FileUtil.from(context, uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data")
                            , file);
            body = MultipartBody.Part.createFormData(
                    "assets[" + 2 + "]", file.getName(), requestFile);
            resourceBody.add(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadImage3(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
        Uri uri = imagesList.get("image" + 3);
        File file = null;
        try {
            file = FileUtil.from(context, uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data")
                            , file);
            body = MultipartBody.Part.createFormData(
                    "assets[" + 3 + "]", file.getName(), requestFile);
            resourceBody.add(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void uploadImage(MultipartBody.Part body, List<MultipartBody.Part> resourceBody,int init) {
       Log.e("loream_edit_size",editImages.size()+"");
       Log.e("loream_images_list",imagesList.get("image" + 1)+"");
       Log.e("loream_images_list",imagesList.get("image" + 2)+"");
       Log.e("loream_images_list",imagesList.get("image" + 3)+"");
       Log.e("loream_initt",init+"");
        for (int i = 0; i <imagesList.size() ; i++) {
            init++;
            Uri uri = imagesList.get("image" + init);
            File file = null;
            try {
                file = FileUtil.from(context, uri);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data")
                                , file);
                body = MultipartBody.Part.createFormData(
                        "assets[" + init + "]", file.getName(), requestFile);
                resourceBody.add(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void editPostRequest(HashMap<String, Uri> imagesList, String pTitle, String pDescription, Integer category, int isDonation) {
        List<MultipartBody.Part> resourceBody = new ArrayList<>();
        MultipartBody.Part body = null;
        int editImagesSize = editImages.size(), addImagesSize = imagesList.size();
        if (imagesList.size() > 0) {
            if (editImagesSize == 0) {
                if (addImagesSize==1){
                    uploadImage1(body,resourceBody);
                } else {
                    uploadImage(body,resourceBody,0);
                }

            } else if (editImagesSize == 1) {
                if (addImagesSize==1){
                    uploadImage2(body,resourceBody);
                } else {
                    uploadImage(body,resourceBody,1);
                }

            } else if (editImagesSize == 2) {
                if (addImagesSize==1){
                    uploadImage3(body,resourceBody);
                } else {
                    uploadImage(body,resourceBody,2);
                }
            }
        }


        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), pTitle);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), pDescription);
        RequestBody is_donation = RequestBody.create(MediaType.parse("multipart/form-data"), isDonation + "");
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), category + "");
        Call<MessageResponse> call = serviceApi.updatePost(post.getId(), "Bearer " + token
                , title
                , description
                , is_donation
                , category_id
                , resourceBody);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    fragmentSwitcher.switchFragment(PagesFragment.ALL_POSTS, null, null);
                    UtilMethods.launchLoadingLottieDialogDismiss(context);

                } else {
                    String errorMessage = parseError(response);
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    UtilMethods.launchLoadingLottieDialogDismiss(context);

                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                UtilMethods.launchLoadingLottieDialogDismiss(context);

                call.cancel();
            }
        });
    }


    private void spinnerCode(List<Category> categories) {
        ArrayAdapter<Category> adapter =
                new ArrayAdapter<Category>(context, android.R.layout.simple_spinner_dropdown_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                category = categories.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    public String validationAllFields() {

        if (binding.spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            Toasty.error(context, "Error Spinner", Toast.LENGTH_SHORT).show();
            return "error1";
        } else if (pTitle.isEmpty()) {
            binding.titlePost.requestFocus();
            binding.titlePost.setError("FIELD CANNOT BE EMPTY IN TITTLE");
            return "error1";

        } else if (pDescription.isEmpty()) {
            binding.descriptionPost.requestFocus();
            binding.descriptionPost.setError("FIELD CANNOT BE EMPTY IN DESCRIPTION");
            return "error2";
        } else if (imagesList.isEmpty() && !isEdit) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("ALERT");
            alert.setMessage("PLEASE UPLOAD PHOTO");
            alert.setPositiveButton("ok", null);
            alert.show();
            Toasty.error(context, "PLEASE UPLOAD PHOTO", Toast.LENGTH_SHORT).show();
            return "error4";
        } else if (isDonation == -1) {
            Toasty.error(context, "PLEASE SELECTED REQUEST OR DONATION", Toast.LENGTH_SHORT).show();
            return "error 5 ";
        } else {
            return "";
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void clearAllFields() {
        binding.image1.setImageDrawable(context.getDrawable(R.drawable.add));
        binding.image2.setImageDrawable(context.getDrawable(R.drawable.add));
        binding.image3.setImageDrawable(context.getDrawable(R.drawable.add));

        binding.imagePost2.setVisibility(View.GONE);
        binding.imagePost3.setVisibility(View.GONE);
        imageNum = 0;
        imagesList.clear();
        binding.titlePost.setText(null);
        binding.descriptionPost.setText(null);
        if (binding.radioDon.isChecked())
            binding.radioDon.setChecked(false);
        else if (binding.radioReq.isChecked())
            binding.radioReq.setChecked(false);
    }


    private void addPostRequest(HashMap<String, Uri> imagesList, String uTitle, String uDescription, int pCategory, int pIsDonation) {

        List<MultipartBody.Part> resourceBody = new ArrayList<>();
        for (int i = 0; i < imagesList.size(); i++) {
            int num = i + 1;
            MultipartBody.Part body = null;
            Uri uri = imagesList.get("image" + num);
            File file = null;
            try {
                file = FileUtil.from(context, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data")
                            , file);
            body = MultipartBody.Part.createFormData(
                    "assets[" + i + "]", file.getName(), requestFile);
            resourceBody.add(body);

        }

        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), uTitle);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), uDescription);
        RequestBody is_donation = RequestBody.create(MediaType.parse("multipart/form-data"), pIsDonation + "");
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), pCategory + "");
        Call<AllPosts> call = serviceApi.addPost("Bearer " + token
                , title
                , description
                , is_donation
                , category_id
                , resourceBody);
        call.enqueue(new Callback<AllPosts>() {
            @Override
            public void onResponse(Call<AllPosts> call, Response<AllPosts> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("Success", new Gson().toJson(response.body()));
                    fragmentSwitcher.switchFragment(PagesFragment.ALL_POSTS, null, null);
                    UtilMethods.launchLoadingLottieDialogDismiss(context);

                } else {
                    String errorMessage = parseError(response);
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    UtilMethods.launchLoadingLottieDialogDismiss(context);

                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                UtilMethods.launchLoadingLottieDialogDismiss(context);

                call.cancel();
            }
        });
    }

    public static String parseError(Response<?> response) {
        String errorMsg = null;
        try {
            assert response.errorBody() != null;
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            errorMsg = jObjError.getString("message");
            return errorMsg;
        } catch (Exception ignored) {

        }
        return errorMsg;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) { // There are no request codes
                                Intent data = result.getData();
                                Log.e("data", data.getDataString() + "");
                                File file_posts = null;
                                try {
                                    file_posts = FileUtil.from(context, data.getData());

                                    if (imageNum == 1) {
                                        binding.image1.setImageURI(data.getData());
//                                        binding.image2.setColorFilter(ContextCompat.getColor(context, R.color.bink), android.graphics.PorterDuff.Mode.MULTIPLY);
                                        binding.imagePost2.setVisibility(View.VISIBLE);
                                        imagesList.put(IMAGE1, data.getData());
                                    } else if (imageNum == 2) {
                                        imagesList.put(IMAGE2, data.getData());
                                        binding.imagePost3.setVisibility(View.VISIBLE);
                                        binding.image2.setImageURI(data.getData());
                                    } else if (imageNum == 3) {
                                        imagesList.put(IMAGE3, data.getData());
                                        binding.image3.setImageURI(data.getData());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

    @Override
    public int getFragmentTitle() {
        return R.string.add_post;
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
                    assert getAllCategories != null;
                    categories = getAllCategories.getData();
                    spinnerCode(categories);
                    //progressDialog.dismiss();

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

    //-------------------------------edit images-----------------------------------------------
    List<String> editImages;

    private void setFields() {
        binding.descriptionPost.setText(post.getDescription());
        binding.titlePost.setText(post.getTitle());
        if (post.getIsDonation())
            binding.radioDon.setChecked(true);
        else
            binding.radioDon.setChecked(true);
        editImages = post.getPostMedia();
        setEditImage();
        setEditImagesAction(editImages);
    }

    private void setEditImage() {
        switch (editImages.size()) {
            case 3:
                Glide.with(context).load(editImages.get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
                Glide.with(context).load(editImages.get(1)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
                Glide.with(context).load(editImages.get(2)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image3);
                binding.imagePost2.setVisibility(View.VISIBLE);
                binding.imagePost3.setVisibility(View.VISIBLE);
                binding.delete1.setVisibility(View.VISIBLE);
                binding.delete2.setVisibility(View.VISIBLE);
                binding.delete3.setVisibility(View.VISIBLE);
                break;
            case 2:
                Glide.with(context).load(editImages.get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
                Glide.with(context).load(editImages.get(1)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
                Glide.with(context).load(R.drawable.add).into(binding.image3);
                binding.imagePost3.setVisibility(View.VISIBLE);
                binding.imagePost2.setVisibility(View.VISIBLE);
                binding.delete1.setVisibility(View.VISIBLE);
                binding.delete2.setVisibility(View.VISIBLE);
                break;
            case 1:
                Glide.with(context).load(editImages.get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
                binding.imagePost2.setVisibility(View.VISIBLE);
                binding.delete1.setVisibility(View.VISIBLE);
                if (imagesList.size() == 0) {
                    Glide.with(context).load(R.drawable.add).into(binding.image2);
                    Glide.with(context).load(R.drawable.add).into(binding.image3);
                    binding.imagePost3.setVisibility(View.GONE);
                } else if (imagesList.size() == 1) {
                    Glide.with(context).load(imagesList.get(IMAGE3)).into(binding.image2);
                    Glide.with(context).load(R.drawable.add).into(binding.image3);
                    imagesList.put(IMAGE2, imagesList.get(IMAGE3));
                    imagesList.put(IMAGE3, null);
                }

                break;
            case 0:
                if (imagesList.size() == 1) {
                    Glide.with(context).load(imagesList.get(IMAGE2)).into(binding.image1);
                    Glide.with(context).load(R.drawable.add).into(binding.image2);
                    Glide.with(context).load(R.drawable.add).into(binding.image3);
                    binding.imagePost3.setVisibility(View.GONE);
                    imagesList.put(IMAGE1, imagesList.get(IMAGE2));
                    imagesList.put(IMAGE2, null);
                } else if (imagesList.size() == 2) {
                    Glide.with(context).load(imagesList.get(IMAGE2)).into(binding.image1);
                    Glide.with(context).load(imagesList.get(IMAGE3)).into(binding.image2);
                    binding.imagePost3.setVisibility(View.VISIBLE);
                    Glide.with(context).load(R.drawable.add).into(binding.image3);
                    imagesList.put(IMAGE1, imagesList.get(IMAGE2));
                    imagesList.put(IMAGE2, imagesList.get(IMAGE3));
                    imagesList.put(IMAGE3, null);

                } else {
                    Glide.with(context).load(imagesList.get(IMAGE1)).into(binding.image1);
                    Glide.with(context).load(imagesList.get(IMAGE2)).into(binding.image2);
                    Glide.with(context).load(imagesList.get(IMAGE3)).into(binding.image3);

                }
                break;
        }
    }

    private void setEditImagesAction(List<String> imageName) {
        binding.imagePost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editImages.size() == 0) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    someActivityResultLauncher.launch(intent);
                    imageNum = 1;
                } else
                    createDialogConfirmDelete(0);
            }
        });
        binding.imagePost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editImages.size() < 2) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    someActivityResultLauncher.launch(intent);
                    imageNum = 2;
                } else
                    createDialogConfirmDelete(1);
            }
        });
        binding.imagePost3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editImages.size() < 3) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    someActivityResultLauncher.launch(intent);
                    imageNum = 3;
                } else
                    createDialogConfirmDelete(2);
            }
        });

    }

    private void createDialogConfirmDelete(int position) {


        if (editImages.size() == 1 && imagesList.size() == 0) {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
            pDialog.setTitleText("you cannot do this!");
            pDialog.setCancelable(true);
            pDialog.show();
        } else {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
            pDialog.setTitleText("you will lose this image if you continue ");
            pDialog.setCancelable(true);
            pDialog.setConfirmButton("sure", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    String finalImageName = editImages.get(position).replace("http://54.209.160.242/storage/", "");
                    deleteImageRequest(finalImageName, position);

                    setEditImage();
                }
            });
            pDialog.show();
        }

    }

    private void deleteImageRequest(String imageName, int position) {
        Call<MessageResponse> call = serviceApi.deleteImage(imageName, "Bearer " + token);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
//                resetEditImages();
                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                pDialog.dismiss();
                editImages.remove(position);
                setEditImage();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void setAddImagesAction() {
        binding.imagePost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(intent);
                imageNum = 1;

            }
        });
        binding.imagePost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(intent);
                imageNum = 2;
            }
        });
        binding.imagePost3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                someActivityResultLauncher.launch(intent);
                imageNum = 3;
            }
        });
    }
}