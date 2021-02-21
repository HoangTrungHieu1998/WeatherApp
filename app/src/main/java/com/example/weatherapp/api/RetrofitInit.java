package com.example.weatherapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInit {

    private static RetrofitInit mInstance = null;
    private static Retrofit mRetrofit = null;

    private RetrofitInit(){
        mRetrofit = init();
    }

    private Retrofit init() {
        //Khoi tao gson
        Gson gson = new GsonBuilder().setLenient().create();

        // Khởi tạo okHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();

        // Khởi tạo retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create(gson))//Khởi tạo gson
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return mRetrofit;
    }

    public static ApiRequest getInstance(){
        if (mInstance == null){
            mInstance = new RetrofitInit();
        }
        return mRetrofit.create(ApiRequest.class);
    }
}
