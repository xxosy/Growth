package com.growth.standard.presenter;

import android.util.Log;

import com.growth.domain.standard.StandardInformation;
import com.growth.network.standard.StandardInformationDataAPI;
import com.growth.standard.adapter.StandardInformationAdapterModel;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-11-21.
 */

public class StandardInformationPresenterImpl implements StandardInformationPresenter {
  StandardInformationPresenter.View view;
  StandardInformationDataAPI mStandardInformationDataAPI;
  StandardInformationAdapterModel mStandardInformationAdapterModel;

  @Inject
  public StandardInformationPresenterImpl(StandardInformationPresenter.View view, StandardInformationDataAPI standardInformationDataAPI,StandardInformationAdapterModel standardInformationAdapterModel){
    this.view= view;
    mStandardInformationDataAPI = standardInformationDataAPI;
    mStandardInformationAdapterModel = standardInformationAdapterModel;
  }

  @Override
  public void OnCreatedView() {
    mStandardInformationDataAPI.getStandardInformationList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          Log.i("asd",result[0].getDescription());
          for(StandardInformation item:result) {
            mStandardInformationAdapterModel.add(item);
          }
          view.refreshRecyclerView();
        });
  }
}
