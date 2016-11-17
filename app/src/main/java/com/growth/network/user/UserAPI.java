package com.growth.network.user;

import com.growth.domain.user.User;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-08-31.
 */

public interface UserAPI {
  @GET("/user/{usercode}")
  Observable<User> getUserCode(@Path("usercode") String usercode);

  @POST("/user/{usercode}")
  Observable<Void> insertUserCode(@Path("usercode") String usercode);

  interface Service {
    Observable<User> getUserCode(String usercode);

    Observable<Void> insertUserCode(String usercode);
  }
}
