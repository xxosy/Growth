package com.growth.network.standard;

import com.growth.domain.standard.StandardInformation;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by SSL-D on 2016-11-21.
 */

public class StandardInformationDataAPI implements StandardInformationAPI {
  private Retrofit retrofit;

  public StandardInformationDataAPI(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  @Override
  public Observable<StandardInformation[]> getStandardInformationList() {
    return retrofit.create(StandardInformationAPI.class)
        .getStandardInformationList();
  }
}
