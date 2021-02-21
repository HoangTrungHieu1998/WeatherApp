package com.example.weatherapp.api;

import com.example.weatherapp.model.Example;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequest {
    @GET("weather")
    Call<Example> getWeather(@Query("q") String cityname,
                              @Query("appid") String API);
}
