package com.example.graduationproject.fragments.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.graduationproject.fragments.PagesFragment;
import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.categories.Category;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.register.User;
import com.example.graduationproject.utils.AppSharedPreferences;
import com.example.graduationproject.utils.FileUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostFragment extends BaseFragment {
    FragmentAddPostBinding binding;
    public static final String TAG = "ADD_POSTS";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    List<Category> categories;
    ArrayList<File> imagesList;
    int imageNum = 0;
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

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();
        categories = new ArrayList<>();
        imagesList = new ArrayList<>();
        showDialog();
        getAllCategories();

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
        //Spinner Code
//        spinnerCode();

//        Log.e("category", category);
        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                pTitle = binding.titlePost.getText().toString();
                pDescription = binding.descriptionPost.getText().toString();
                int radioButtonID = binding.radioGroup.getCheckedRadioButtonId();
                View radioButton = binding.radioGroup.findViewById(radioButtonID);
                int idx = binding.radioGroup.indexOfChild(radioButton);
                RadioButton r = (RadioButton) binding.radioGroup.getChildAt(idx);


                if (ValidationAllFields().equals("")) {
                    String selectedtext = r.getText().toString();
                    if (selectedtext.equals("Donation"))
                        isDonation = 1;
                    else if (selectedtext.equals("Request"))
                        isDonation = 0;
                    addPostRequest(imagesList, pTitle, pDescription, category, isDonation);
                } else {
                    progressDialog.dismiss();

                }


            }
        });
        return view;
    }


    String pTitle, pDescription;
    int isDonation=-1;
    Integer category;

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
                Toast.makeText(context, category + "", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    public String ValidationAllFields() {

        if (binding.spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            Toast.makeText(context, "Error Spinner", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "PLEASE SELECTED REQUEST OR DONATION", Toast.LENGTH_SHORT).show();
            return "error3";
        } else if (imagesList.isEmpty()) {
            Toast.makeText(context, "PLEASE UPLOAD PHOTO", Toast.LENGTH_SHORT).show();
            return "error4";

        } else {
            Toast.makeText(context, "VALIDATION  SUCCESSFUL", Toast.LENGTH_SHORT).show();
            return "";
        }

    }


    private void addPostRequest(ArrayList<File> imagesList, String uTitle, String uDescription, int pCategory, int pIsDonation) {


        List<MultipartBody.Part> resourceBody = new ArrayList<>();
        for (int i = 0; i < imagesList.size(); i++) {
            MultipartBody.Part body = null;
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data")
                            , imagesList.get(i));
            body = MultipartBody.Part.createFormData(
                    "assets[" + i + "]", imagesList.get(i).getName(), requestFile);
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
                    fragmentSwitcher.switchFragment(PagesFragment.ALL_POSTS, null);
                    Toast.makeText(context, "tamm", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    String errorMessage = parseError(response);
                    Toast.makeText(context, errorMessage + "", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<AllPosts> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
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
                                File file = null;
                                try {
                                    if (imageNum == 1) {
                                        binding.imagePost1.setImageURI(data.getData());
                                    } else if (imageNum == 2) {
                                        binding.imagePost2.setImageURI(data.getData());
                                    } else if (imageNum == 3) {
                                        binding.imagePost3.setImageURI(data.getData());
                                    }
                                    file = FileUtil.from(context, data.getData());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                imagesList.add(file);

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
                    progressDialog.dismiss();

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

}