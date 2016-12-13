package com.growth.monitor.presenter;

import com.growth.domain.sensor.SensorItem;
import com.growth.domain.user.User;
import com.growth.monitor.adapter.MonitorSensorListAdapterModel;
import com.growth.network.SensorDataAPI;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-10-06.
 */

public class MonitorPresenterImpl implements MonitorPresenter {
  View view;
  SensorDataAPI sensorDataAPI;
  MonitorSensorListAdapterModel mMonitorSensorListAdapterModel;

  @Inject
  MonitorPresenterImpl(View view, SensorDataAPI sensorDataAPI, MonitorSensorListAdapterModel MonitorSensorListAdapterModel) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
    this.mMonitorSensorListAdapterModel = MonitorSensorListAdapterModel;
  }


  @Override
  public void OnCreatedView() {
    sensorDataAPI.getMapSensors(User.getInstance().getUserCode())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          mMonitorSensorListAdapterModel.clear();
          for (SensorItem sensorItem : result) {
            mMonitorSensorListAdapterModel.add(sensorItem);
          }
          view.refreshRecyclerMonitorList();
        });
  }

  @Override
  public void OnRecyclerMonitorItemClick(int position) {
    String serial = mMonitorSensorListAdapterModel.getItem(position).getSerial();
    view.startActivityPlantGallery(serial);
  }
}
