package com.example.graduationproject.fragments.ui;

import static com.example.graduationproject.fragments.PagesFragment.EDIT;
import static com.example.graduationproject.fragments.PagesFragment.SEARCH;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    HashMap<String, File> imagesList;
    //    ArrayList<File> imagesList;
    int imageNum = 0;
    String pTitle, pDescription;
    int isDonation = 2;
    Integer category;
    private FragmentSwitcher fragmentSwitcher;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
            isEdit=false;
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
        imagesList = new HashMap<String, File>();
//        post = new Post();
        getAllCategories();
        if (isEdit)
            setFields();

        EventBus.getDefault().post(new MyTitleEventBus(PagesFragment.ADD_POSTS, "Add Post"));

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
                        addPostRequest(imagesList, pTitle, pDescription, category, isDonation);
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
        } else if (imagesList.isEmpty()) {
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


    private void addPostRequest(HashMap<String, File> imagesList, String uTitle, String uDescription, int pCategory, int pIsDonation) {

        List<MultipartBody.Part> resourceBody = new ArrayList<>();
        for (int i = 0; i < imagesList.size(); i++) {
            int num = i + 1;
            MultipartBody.Part body = null;
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data")
                            , imagesList.get("image" + num));
            body = MultipartBody.Part.createFormData(
                    "assets[" + i + "]", imagesList.get("image" + num).getName(), requestFile);
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
                                        imagesList.put(IMAGE1, file_posts);
                                    } else if (imageNum == 2) {
                                        imagesList.put(IMAGE2, file_posts);
                                        binding.imagePost3.setVisibility(View.VISIBLE);
                                        binding.image2.setImageURI(data.getData());
                                    } else if (imageNum == 3) {
                                        imagesList.put(IMAGE3, file_posts);
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


    private void setFields() {
        binding.descriptionPost.setText(post.getDescription());
        binding.titlePost.setText(post.getTitle());
        if (post.getIsDonation())
            binding.radioDon.setChecked(true);
        else
            binding.radioDon.setChecked(true);
        Toast.makeText(context, post.getPostMedia().size() + "", Toast.LENGTH_SHORT).show();

        switch (post.getPostMedia().size()) {
            case 3:
                Glide.with(context).load(post.getPostMedia().get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
                Glide.with(context).load(post.getPostMedia().get(1)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
                Glide.with(context).load(post.getPostMedia().get(2)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image3);
                binding.image3.setVisibility(View.VISIBLE);
                binding.image2.setVisibility(View.VISIBLE);
                break;
            case 2:
                Glide.with(context).load(post.getPostMedia().get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
                Glide.with(context).load(post.getPostMedia().get(1)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image2);
                binding.image3.setVisibility(View.VISIBLE);
                binding.image2.setVisibility(View.VISIBLE);
                break;
            case 1:
                Glide.with(context).load(post.getPostMedia().get(0)).placeholder(R.drawable.ic_launcher_foreground).into(binding.image1);
                binding.image2.setVisibility(View.VISIBLE);
                binding.image3.setVisibility(View.GONE);
                break;


        }

    }
}