package com.example.graduationproject.retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Creator {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(10000, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "multipart/form-data")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Accept-Language", "en")
                        .build();
                return chain.proceed(newRequest);
            }
        }).readTimeout(10000, TimeUnit.SECONDS).addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "multipart/form-data")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Accept-Language", "en").build();
                return chain.proceed(newRequest);
            }
        }).build();
        retrofit = new Retrofit.Builder().baseUrl(ServiceApi.ENDPOINT).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        return retrofit;
    }
}