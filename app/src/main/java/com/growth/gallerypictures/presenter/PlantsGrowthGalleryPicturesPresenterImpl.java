package com.growth.gallerypictures.presenter;

import com.growth.gallerypictures.adapter.PlantsGrowthGalleryPicturesAdapterModel;
import com.growth.network.SensorDataAPI;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-10-06.
 */

public class PlantsGrowthGalleryPicturesPresenterImpl implements PlantsGrowthGalleryPicturesPresenter {
  PlantsGrowthGalleryPicturesPresenter.View view;
  SensorDataAPI sensorDataAPI;
  PlantsGrowthGalleryPicturesAdapterModel mPlantsGrowthGalleryPicturesAdapterModel;

  @Inject
  PlantsGrowthGalleryPicturesPresenterImpl(PlantsGrowthGalleryPicturesPresenter.View view, SensorDataAPI sensorDataAPI, PlantsGrowthGalleryPicturesAdapterModel plantsGrowthGalleryPicturesAdapterModel) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
    mPlantsGrowthGalleryPicturesAdapterModel = plantsGrowthGalleryPicturesAdapterModel;
  }

  @Override
  public void onCreated(String serial) {
    sensorDataAPI.getPlantsPictureList(serial)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          mPlantsGrowthGalleryPicturesAdapterModel.clear();
          for (String item : result) {
            mPlantsGrowthGalleryPicturesAdapterModel.add(item);
          }
          view.refreshRecycler();
        });
  }

  @Override
  public void OnRecyclerPlantsGrowthGalleryItemClick(int position) {

  }
}
