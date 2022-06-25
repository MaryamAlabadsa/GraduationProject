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
import com.example.graduationproject.model.MyImage;
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
import java.util.Objects;

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
    //    HashMap<String, Uri> imagesList;
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
    HashMap<String, MyImage> myImageHashMap;
    String mapKey;

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

        myImageHashMap = new HashMap<String, MyImage>();
//        imagesList = new HashMap<String, Uri>();
//        post = new Post();
        getAllCategories();
        if (isEdit)
            setFields();
        else
            setAddImagesAction();

        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ADD_POSTS, "Add Post"));

//
//        String user = sharedPreferences.readUser(AppSharedPreferences.USER);
//        Gson gson = new Gson();
//        if (!user.isEmpty()) {
//            User user1 = gson.fromJson(user, User.class);
//            Glide.with(context).load(user1.getImageLink()).circleCrop()
//                    .placeholder(R.drawable.ic_launcher_foreground).into(binding.imageProfile);
//            binding.userName.setText(user1.getName());
//        }


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
                            editPostRequest(pTitle, pDescription, category, isDonation);
                        else addPostRequest(pTitle, pDescription, category, isDonation);
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
        } else if (isDonation == -1) {
            Toasty.error(context, "PLEASE SELECTED REQUEST OR DONATION", Toast.LENGTH_SHORT).show();
            return "error 5 ";
        } else {
            return "";
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void clearAllFields() {
//        binding.image1.setImageDrawable(context.getDrawable(R.drawable.adapterdd));
//        binding.image2.setImageDrawable(context.getDrawable(R.drawable.add));
//        binding.image3.setImageDrawable(context.getDrawable(R.drawable.add));
        for (int i = 0; i <myImageHashMap.size() ; i++) {
            String mapKey=createMapKey(i+1);
            if (!myImageHashMap.get(mapKey).getUploaded()){
                myImageHashMap.remove(mapKey);
            }
        }
        setImageView();

//        binding.imagePost2.setVisibility(View.GONE);
//        binding.imagePost3.setVisibility(View.GONE);
        imageNum = 0;
        binding.titlePost.setText(null);
        binding.descriptionPost.setText(null);
        if (binding.radioDon.isChecked())
            binding.radioDon.setChecked(false);
        else if (binding.radioReq.isChecked())
            binding.radioReq.setChecked(false);
    }


    private void addPostRequest(String uTitle, String uDescription, int pCategory, int pIsDonation) {
        List<MultipartBody.Part> resourceBody = new ArrayList<>();
        MultipartBody.Part body = null;
        if (myImageHashMap.get(IMAGE1) != null) {

            uploadUriImage1(body, resourceBody);
            uploadUriImage2(body, resourceBody);
            uploadUriImage3(body, resourceBody);

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
        } else {
            UtilMethods.launchLoadingLottieDialogDismiss(context);
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
            pDialog.setTitleText("you must choose one image at least ");
            pDialog.setCancelable(true);
            pDialog.show();
        }

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

    private void uploadUriImage1(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {
        if (!isImage1Uploaded()) {
            Uri uri = myImageHashMap.get(IMAGE1).getImageUri();
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
    }

    private void uploadUriImage2(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {

        if (myImageHashMap.get(IMAGE2) != null) {
            if (!isImage2Uploaded()) {
                Uri uri = myImageHashMap.get(IMAGE2).getImageUri();
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
        }
    }

    private void uploadUriImage3(MultipartBody.Part body, List<MultipartBody.Part> resourceBody) {

        if (myImageHashMap.get(IMAGE3) != null) {
            if (!isImage3Uploaded()) {
                Uri uri = myImageHashMap.get(IMAGE3).getImageUri();
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
        }
    }


    private void editPostRequest(String pTitle, String pDescription, Integer category, int isDonation) {
        List<MultipartBody.Part> resourceBody = new ArrayList<>();
        MultipartBody.Part body = null;
        if (myImageHashMap.get(IMAGE1) != null) {

            uploadUriImage1(body, resourceBody);
            uploadUriImage2(body, resourceBody);
            uploadUriImage3(body, resourceBody);

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
        } else {
            UtilMethods.launchLoadingLottieDialogDismiss(context);
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
            pDialog.setTitleText("you must choose one image at least ");
            pDialog.setCancelable(true);
            pDialog.show();
        }
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
                                        myImageHashMap.put(IMAGE1, new MyImage(data.getData(), false));
                                    } else if (imageNum == 2) {
                                        binding.image2.setImageURI(data.getData());
                                        myImageHashMap.put(IMAGE2, new MyImage(data.getData(), false));
                                    } else if (imageNum == 3) {
                                        binding.image3.setImageURI(data.getData());
                                        myImageHashMap.put(IMAGE3, new MyImage(data.getData(), false));
                                    }
                                    setImageView();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });


    //-------------------------------edit images-----------------------------------------------
    private void setFields() {
        binding.descriptionPost.setText(post.getDescription());
        binding.titlePost.setText(post.getTitle());
        if (post.getIsDonation())
            binding.radioDon.setChecked(true);
        else
            binding.radioDon.setChecked(true);
        appendEditImageToMap(post.getPostMedia());
//        editImages = post.getPostMedia();
        setImageView();
        setDeleteImagesAction();
        setAddImagesAction();
    }

    private void appendEditImageToMap(List<String> editImage) {
        for (int i = 0; i < editImage.size(); i++) {
            MyImage myImage = new MyImage(editImage.get(i), true);
            String mapKey = "image" + (i + 1);
            myImageHashMap.put(mapKey, myImage);
        }
    }

    private void setImageView() {
        switch (myImageHashMap.size()) {
            case 3:
                //image 1
                isImage1Uploaded();
//               image 2
                isImage2Uploaded();
//                image 3
                isImage3Uploaded();
                binding.imagePost2.setVisibility(View.VISIBLE);
                binding.imagePost3.setVisibility(View.VISIBLE);
                binding.delete1.setVisibility(View.VISIBLE);
                binding.delete2.setVisibility(View.VISIBLE);
                binding.delete3.setVisibility(View.VISIBLE);
                break;
            case 2:
                //image 1
                isImage1Uploaded();
                // image 2
                isImage2Uploaded();
                Glide.with(context).load(R.drawable.hourglass_split).into(binding.image3);
                binding.imagePost3.setVisibility(View.VISIBLE);
                binding.imagePost2.setVisibility(View.VISIBLE);
                binding.delete1.setVisibility(View.VISIBLE);
                binding.delete2.setVisibility(View.VISIBLE);
                binding.delete3.setVisibility(View.GONE);
                binding.addImage3.setVisibility(View.VISIBLE);
                break;
            case 1:
                //image 1
                isImage1Uploaded();
                Glide.with(context).load(R.drawable.hourglass_split).into(binding.image2);
                Glide.with(context).load(R.drawable.hourglass_split).into(binding.image3);
                binding.imagePost2.setVisibility(View.VISIBLE);
                binding.imagePost3.setVisibility(View.GONE);
                binding.delete1.setVisibility(View.VISIBLE);
                binding.delete2.setVisibility(View.GONE);
                binding.delete3.setVisibility(View.GONE);
                binding.addImage2.setVisibility(View.VISIBLE);
                break;
            case 0:
                binding.imagePost1.setVisibility(View.VISIBLE);
                binding.imagePost2.setVisibility(View.GONE);
                binding.imagePost3.setVisibility(View.GONE);
                binding.addImage1.setVisibility(View.VISIBLE);
//                Glide.with(context).load(R.drawable.add).into(binding.image1);

        }
    }

    private void setDeleteImagesAction() {
        binding.delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImage1Uploaded()) {
                    createDialogConfirmDeleteUrlImage(0);
                } else {
                    deleteUriImageFromMap(0);
                }
            }
        });
        binding.delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImage2Uploaded()) {
                    createDialogConfirmDeleteUrlImage(1);
                } else {
                    deleteUriImageFromMap(1);
                }
            }
        });
        binding.delete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isImage3Uploaded()) {
                    createDialogConfirmDeleteUrlImage(2);
                } else {
                    deleteUriImageFromMap(2);
                }
            }
        });
    }

    private String createMapKey(int i) {
        String mapKey = "image" + i;
        return mapKey;
    }

    private void deleteUriImageFromMap(int i) {
        mapKey = createMapKey(i + 1);
        myImageHashMap.remove(mapKey);
        changePositionAfterDelete(i);
        setImageView();
    }

    private void createDialogConfirmDeleteUrlImage(int position) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#E60F5DAB"));
        pDialog.setTitleText("you will lose this image if you continue ");
        pDialog.setCancelable(true);
        pDialog.setConfirmButton("sure", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                mapKey = createMapKey(position + 1);
                String finalImageName = myImageHashMap.get(mapKey).getImageUrl().replace("http://54.209.160.242/storage/", "");
                deleteImageRequest(finalImageName, position);
            }
        });
        pDialog.show();


    }

    private void deleteImageRequest(String imageName, int position) {
        Call<MessageResponse> call = serviceApi.deleteImage(imageName, "Bearer " + token);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.d("response5 code", response.code() + "");
                mapKey = createMapKey(position + 1);
                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                pDialog.dismiss();
                myImageHashMap.remove(mapKey);
                changePositionAfterDelete(position);
                setImageView();
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void changePositionAfterDelete(int i) {
        switch (myImageHashMap.size()) {
            case 2:
                if (i == 0) {
                    myImageHashMap.put(IMAGE1, myImageHashMap.get(IMAGE2));
                    myImageHashMap.put(IMAGE2, myImageHashMap.get(IMAGE3));
                    myImageHashMap.remove(IMAGE3);
                } else if (i == 1) {
                    myImageHashMap.put(IMAGE2, myImageHashMap.get(IMAGE3));
                    myImageHashMap.remove(IMAGE3);
                }
                break;
            case 1:
                if (i == 0) {
                    myImageHashMap.put(IMAGE1, myImageHashMap.get(IMAGE2));
                    myImageHashMap.remove(IMAGE2);
                }
                break;
        }
    }

    private void setAddImagesAction() {
        binding.imagePost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myImageHashMap.get(IMAGE1) != null) {
                    if (!isImage1Uploaded())
                        openGallery(1);
                } else
                    openGallery(1);
            }
        });
        binding.imagePost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myImageHashMap.get(IMAGE2) != null) {
                    if (!isImage2Uploaded())
                        openGallery(2);
                } else
                    openGallery(2);
            }
        });
        binding.imagePost3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myImageHashMap.get(IMAGE3) != null) {
                    if (!isImage3Uploaded())
                        openGallery(3);
                } else
                    openGallery(3);
            }
        });
    }

    private void openGallery(int i) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
        imageNum = i;

    }

    private Boolean isImage1Uploaded() {
        binding.addImage1.setVisibility(View.INVISIBLE);
    if (myImageHashMap.get(IMAGE1).getUploaded()) {
            Glide.with(context).load(myImageHashMap.get(IMAGE1).getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
            return true;
        } else {
            Glide.with(context).load(myImageHashMap.get(IMAGE1).getImageUri()).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
            return false;
        }
    }

    private Boolean isImage2Uploaded() {
        binding.addImage2.setVisibility(View.INVISIBLE);
        if (myImageHashMap.get(IMAGE2).getUploaded()) {
            Glide.with(context).load(myImageHashMap.get(IMAGE2).getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
            return true;
        } else {
            Glide.with(context).load(myImageHashMap.get(IMAGE2).getImageUri()).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
            return false;
        }

    }

    private Boolean isImage3Uploaded() {
        binding.addImage3.setVisibility(View.INVISIBLE);

        if (myImageHashMap.get(IMAGE3).getUploaded()) {
            Glide.with(context).load(myImageHashMap.get(IMAGE3).getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).into(binding.image3);
            return true;
        } else {
            Glide.with(context).load(myImageHashMap.get(IMAGE3).getImageUri()).placeholder(R.drawable.ic_launcher_foreground).into(binding.image3);
            return false;
        }

    }
}