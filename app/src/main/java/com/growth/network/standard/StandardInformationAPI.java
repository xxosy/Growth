package com.growth.network.standard;

import com.growth.domain.standard.StandardInformation;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by SSL-D on 2016-11-21.
 */

public interface StandardInformationAPI {
  @GET("/standard/information")
  Observable<StandardInformation[]> getStandardInformationList();
}
