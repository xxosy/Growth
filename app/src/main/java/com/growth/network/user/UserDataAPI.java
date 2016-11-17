package com.growth.network.user;

import com.growth.domain.user.User;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by SSL-D on 2016-11-17.
 */

public class UserDataAPI implements UserAPI.Service{
  private Retrofit retrofit;

  public  UserDataAPI(Retrofit retrofit){
    this.retrofit = retrofit;
  }
  @Override
  public Observable<User> getUserCode(String usercode) {
    return retrofit.create(UserAPI.class)
        .getUserCode(usercode);
  }

  @Override
  public Observable<Void> insertUserCode(String usercode) {
    return retrofit.create(UserAPI.class)
        .insertUserCode(usercode);
  }
}
