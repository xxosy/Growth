package com.growth.network;

import com.growth.domain.humidity.HumidityItem;
import com.growth.domain.humidity.HumidityList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-07-22.
 */

public interface HumidityAPI {
  @GET("/humidity/list")
  Observable<HumidityList> getHumidityList();

  @GET("/humidity/recent")
  Observable<HumidityItem> getHumidity();

  @GET("/humidity/recent/{serial}")
  Observable<HumidityItem> getHumidity(@Path("serial") String serial);

  interface Service {
    Observable<HumidityItem> getHumidity();

    Observable<HumidityItem> getHumidity(String serial);

    Observable<HumidityList> getHumidityList();
  }
}
