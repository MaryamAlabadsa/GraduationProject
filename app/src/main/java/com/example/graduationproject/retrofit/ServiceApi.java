package com.example.graduationproject.retrofit;

import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.change.password.ChangePassword;
import com.example.graduationproject.retrofit.login.SendLogin;
import com.example.graduationproject.retrofit.logout.LogOut;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.profile.donation.posts.AllDonationsPosts;
import com.example.graduationproject.retrofit.register.RegisterResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceApi {
    String ENDPOINT = "http://54.234.87.126/api/";

    // TODO: get
    @GET("Category")
    Call<AllCategories> getAllCategories(
            @Header("Authorization") String token);

    @GET("post")
    Call<AllPosts> getAllPosts(
            @Header("Authorization") String token);

    @GET("donationPosts")
    Call<AllDonationsPosts> getDonationPosts(
            @Header("Authorization") String token);

    // TODO: post
    @POST("post")
    Call<AllPosts> addPost(
            @Header("Authorization") String token,
            @Part("title") RequestBody title
            , @Part("description") RequestBody description
            , @Part("is_donation") RequestBody is_donation
            , @Part("category_id") RequestBody category_id
            , @Part MultipartBody.Part image);

    @POST("login")
    Call<RegisterResponse> login
            (@Header("Accept") String accept
                    , @Body SendLogin fields);

    @POST("changePassword")
    Call<RegisterResponse> changePassword
            (@Header("Authorization") String token
                    , @Body ChangePassword fields);

    @POST("logout")
    Call<LogOut> logout(
            @Header("Authorization") String token);


    @Multipart
    @POST("register")
    Call<RegisterResponse> register(@Header("Accept") String accept
            , @Part("name") RequestBody name
            , @Part("email") RequestBody email
            , @Part("phone_number") RequestBody phoneNumber
            , @Part("address") RequestBody address
            , @Part("password") RequestBody password
            , @Part("password_confirmation") RequestBody passwordConfirmation
            , @Part MultipartBody.Part image);

}
