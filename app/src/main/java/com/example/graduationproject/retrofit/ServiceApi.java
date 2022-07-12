package com.example.graduationproject.retrofit;

import com.example.graduationproject.retrofit.categories.AllCategories;
import com.example.graduationproject.retrofit.change.password.ChangePassword;
import com.example.graduationproject.retrofit.login.SendLogin;
import com.example.graduationproject.retrofit.logout.LogOut;
import com.example.graduationproject.retrofit.notifiction.Notification;
import com.example.graduationproject.retrofit.post.AllPosts;
import com.example.graduationproject.retrofit.post.Post;
import com.example.graduationproject.retrofit.post.PostDetails;
import com.example.graduationproject.retrofit.profile.donation.posts.ProfilePosts;
import com.example.graduationproject.retrofit.profile.user.info.UserProfileInfo;
import com.example.graduationproject.retrofit.register.RegisterResponse;
import com.example.graduationproject.retrofit.request.GetAllOrder;
import com.example.graduationproject.retrofit.request.Order;
import com.example.graduationproject.retrofit.token.MessageResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {
    String ENDPOINT = "http://54.209.160.242/api/";

    //  get

    @GET("Category")
    Call<AllCategories> getAllCategories(
            @Header("Authorization") String token
            , @Query("lang") String lang);

    @GET("post")
    Call<AllPosts> getAllPosts(
            @Header("Authorization") String token);

    //  post
    @Multipart
    @POST("post")
    Call<AllPosts> addPost(
            @Header("Authorization") String token
            , @Query("lang") String lang
            , @Part("title") RequestBody title
            , @Part("description") RequestBody description
            , @Part("is_donation") RequestBody is_donation
            , @Part("category_id") RequestBody category_id
            , @Part List<MultipartBody.Part> resources);

    @Multipart
    @POST("PostByCategory/{id}")
    Call<AllPosts> getPostByCategory(
            @Header("Authorization") String token
            , @Part("category_id") RequestBody category_id
            , @Path("id") int checkedId
            , @Part("postStatus") RequestBody postStatus
            , @Query("page") int page
            , @Query("limit") int limit
    );

    @POST("PostDividedByIsDonation/{id}")
    Call<AllPosts> getPostDividedByIsDonation(
            @Header("Authorization") String token,
            @Path("id") int id
            , @Query("postStatus") int postStatus
            , @Query("page") int page
            , @Query("limit") int limit);

    @POST("login")
    Call<RegisterResponse> login
            (@Header("Accept") String accept
                    , @Query("lang") String lang
                    , @Body SendLogin fields);

    @POST("changePassword")
    Call<RegisterResponse> changePassword
            (@Header("Authorization") String token
                    , @Query("lang") String lang
                    , @Body ChangePassword fields);

    @POST("logout")
    Call<LogOut> logout(
            @Header("Authorization") String token);

    @Multipart
    @POST("order")
    Call<Order> addRequest(
            @Header("Authorization") String token
            , @Part("post_id") RequestBody post_id
            , @Part("massage") RequestBody massage);


    @Multipart
    @POST("register")
    Call<RegisterResponse> register(@Header("Accept") String accept
            , @Query("lang") String lang
            , @Part("name") RequestBody name
            , @Part("email") RequestBody email
            , @Part("phone_number") RequestBody phoneNumber
            , @Part("address") RequestBody address
            , @Part("password") RequestBody password
            , @Part("password_confirmation") RequestBody passwordConfirmation
            , @Part("fcm_token") RequestBody fcm_token
            , @Part MultipartBody.Part image);


    @Multipart
    @POST("PostOrders")
    Call<GetAllOrder> getPostOrder(
            @Header("Authorization") String token
            , @Part("id") RequestBody id);

    @Multipart
    @POST("editPost/{post}")
    Call<MessageResponse> updatePost(
            @Path("post") int post
            , @Header("Authorization") String token
            , @Query("lang") String lang
            , @Part("title") RequestBody title
            , @Part("description") RequestBody description
            , @Part("is_donation") RequestBody is_donation
            , @Part("category_id") RequestBody category_id
            , @Part List<MultipartBody.Part> resources);

    @PUT("changePostStatus/{id}")
    Call<Post> changePostStatus(@Path("id") int id,
                                @Header("Authorization") String token
            , @Query("second_user") int second_user);

    @GET("sendDeviceToken/{id}")
    Call<MessageResponse> sendDeviceToken(@Path("id") String id, @Header("Authorization") String token);

    @Multipart
    @POST("updateUserImage")
    Call<RegisterResponse> updateUserImage(@Header("Accept") String accept, @Part MultipartBody.Part image
            , @Header("Authorization") String token);

    // profile

    @POST("myDonationPosts")
    Call<ProfilePosts> getMyDonationPosts(
            @Header("Authorization") String token);

    @POST("myRequestsPosts")
    Call<ProfilePosts> getMyRequestsPosts(
            @Header("Authorization") String token);

    @POST("UserDonationPosts/{id}")
    Call<ProfilePosts> getUserDonationPosts(
            @Path("id") int userId,
            @Header("Authorization") String token);

    @POST("UserRequestPosts/{id}")
    Call<ProfilePosts> getUserRequestPosts(
            @Path("id") int userId,
            @Header("Authorization") String token);

    @POST("myProfileInfo")
    Call<UserProfileInfo> getMyProfileInfo(
            @Header("Authorization") String token);

    @POST("userProfileInfo/{id}")
    Call<UserProfileInfo> getUserProfileInfo(
            @Path("id") int userId,
            @Header("Authorization") String token);


    @GET("notification")
    Call<Notification> getNotification(
            @Header("Authorization") String token);

    @GET("post/{id}")
    Call<PostDetails> getPostDetails(
            @Header("Authorization") String token, @Path("id") int userId
            , @Query("lang") String lang);

    @DELETE("order/{id}")
    Call<MessageResponse> deleteOrder(@Path("id") int id, @Header("Authorization") String token);

    @POST("UpdateUserData")
    Call<RegisterResponse> updateUserData(@Header("Authorization") String token
            , @Query("name") String name
            , @Query("email") String email
            , @Query("phone_number") String phone_number
            , @Query("address") String address
            , @Query("lang") String lang);

    @DELETE("post/{id}")
    Call<MessageResponse> deletePost(@Path("id") int id, @Header("Authorization") String token);

    @POST("restorePost/{id}")
    Call<MessageResponse> restorePost(@Path("id") int id, @Header("Authorization") String token);

    @POST("restoreOrder/{id}")
    Call<MessageResponse> restoreOrder(@Path("id") int id, @Header("Authorization") String token);

    @DELETE("deleteImage")
    Call<MessageResponse> deleteImage(@Query("imageName") String imageName, @Header("Authorization") String token);

    //    @Multipart
    @PUT("order/{id}")
    Call<MessageResponse> editOrder(@Path("id") int id
            , @Header("Authorization") String token
            , @Query("massage") String massage);

    @Multipart
    @POST("searchPost")
    Call<AllPosts> searchPost(@Part("is_donation") RequestBody postDonation,
                              @Part("category") RequestBody postCategory,
                              @Part("AllPosts") RequestBody postStatus,
                              @Part("data") RequestBody postData,
                              @Header("Authorization") String token);
}