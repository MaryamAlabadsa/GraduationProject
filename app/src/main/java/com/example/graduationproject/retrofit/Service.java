package com.example.graduationproject.retrofit;

import com.example.graduationproject.models.ChangePassResponse;
import com.example.graduationproject.models.LoginResponse;
import com.example.graduationproject.requests.ChangePassBody;
import com.example.graduationproject.requests.LoginRequestBody;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {
//    @POST("register")
//    Call<RegisterResponse> register(@Body PostRequestBody user);
   @POST("login")
   Call<LoginResponse> login(@Body LoginRequestBody user);
    @POST("changePassword")
    Call<ChangePassResponse> changePassword(@Body ChangePassBody changePassBody);

    class RetrofitClient {
        private static final String BASE_URL = "http://54.210.44.4/api/";

        public static Service getRetrofitInstance() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(Service.class);
        }
    }


}
