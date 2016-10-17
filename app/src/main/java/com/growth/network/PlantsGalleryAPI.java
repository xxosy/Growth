package com.growth.network;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by SSL-D on 2016-10-07.
 */

public interface PlantsGalleryAPI {
  @GET("/gallery/{serial}")
  Observable<String[]> getPlantsPictureList(@Path("serial") String serial);

  interface Service {
    Observable<String[]> getPlantsPictureList(String serial);
  }
}
