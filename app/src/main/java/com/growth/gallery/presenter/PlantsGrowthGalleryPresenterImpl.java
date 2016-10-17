package com.growth.gallery.presenter;

import com.growth.domain.sensor.SensorItem;
import com.growth.domain.user.User;
import com.growth.gallery.adapter.PlantsGrowthGallerySensorListAdapterModel;
import com.growth.network.SensorDataAPI;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-10-06.
 */

public class PlantsGrowthGalleryPresenterImpl implements PlantsGrowthGalleryPresenter {
  PlantsGrowthGalleryPresenter.View view;
  SensorDataAPI sensorDataAPI;
  PlantsGrowthGallerySensorListAdapterModel mPlantsGrowthGallerySensorListAdapterModel;

  @Inject
  PlantsGrowthGalleryPresenterImpl(PlantsGrowthGalleryPresenter.View view, SensorDataAPI sensorDataAPI, PlantsGrowthGallerySensorListAdapterModel plantsGrowthGallerySensorListAdapterModel) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
    this.mPlantsGrowthGallerySensorListAdapterModel = plantsGrowthGallerySensorListAdapterModel;
  }


  @Override
  public void OnCreatedView() {
    sensorDataAPI.getMapSensors(User.getInstance().getUserCode())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          mPlantsGrowthGallerySensorListAdapterModel.clear();
          for (SensorItem sensorItem : result) {
            mPlantsGrowthGallerySensorListAdapterModel.add(sensorItem);
          }
          view.refreshRecyclerPlantsGrowthGalleryList();
        });
  }

  @Override
  public void OnRecyclerPlantsGrowthGalleryItemClick(int position) {
    String serial = mPlantsGrowthGallerySensorListAdapterModel.getItem(position).getSerial();
    view.startActivityPlantGallery(serial);
  }
}
