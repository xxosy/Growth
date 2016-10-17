package com.growth.network.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class RetrofitCreator {
  private static String BASE_URL = "http://14.55.158.73:3000";

  public static Retrofit createRetrofit() {

    return new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  public static String getBaseUrl() {
    return BASE_URL;
  }
}