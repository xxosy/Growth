package com.growth.map.presenter;

import android.util.Log;

import com.growth.SensorDataDisplay.view.SensorDataDisplayFragment;
import com.growth.actuator.view.ActuatorFragment;
import com.growth.domain.UpdateSensorData;
import com.growth.domain.sensor.SensorItem;
import com.growth.domain.user.User;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.graph.view.GraphFragment;
import com.growth.graph.view.ValueTpye;
import com.growth.home.PageChangeUtil;
import com.growth.network.SensorDataAPI;
import com.growth.utils.GpsInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


// Created by SSL-D on 2016-08-23.

public class SensorMapPresenterImpl implements SensorMapPresenter {
  private static String TAG = SensorMapPresenterImpl.class.getName();
  private SensorMapPresenter.View view;
  private SensorDataAPI sensorDataAPI;
  private SensorItem[] sensorItems;
  private SensorItem currentSsensorItem;
  private boolean isUpdate = false;
  private boolean serial_validate = false;
  private int zoomIndex = 11;

  @Inject
  SensorMapPresenterImpl(SensorMapPresenter.View view, SensorDataAPI sensorDataAPI) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
  }

  @Override
  public void onCreatedView() {
    constituteMap();
  }

  @Override
  public void floatingActionButtonClick() {
    isUpdate = false;
    view.showAddSensorWindow();
    view.clearAddWindow();
  }

  /////////////////////////////////
  ////////////InfoWindow///////////
  /////////////////////////////////

  @Override
  public void infoWindowDetailClick() {
    view.hideInfoWindow();
    PageChangeUtil.newInstance().getPageChange().pageChange(SensorDataDisplayFragment.newInstance(currentSsensorItem.getSerial(), ""));
  }

  @Override
  public void infoWindowDeleteSensorClick() {
    view.startProgress();
    sensorDataAPI.deleteMapSensor(currentSsensorItem.getSerial(), User.getInstance().getUserCode())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          view.clearMap();
          constituteMap();
          view.hideInfoWindow();
          view.stopProgress();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
  }

  @Override
  public void infoWindowUpdateSensorClick() {
    view.hideInfoWindow();
    isUpdate = true;
    view.refreshAddWindowUpdateSensor(currentSsensorItem.getSerial(),
        currentSsensorItem.getTitle(),
        currentSsensorItem.getLat(),
        currentSsensorItem.getLng());
    view.showAddSensorWindow();
  }

  @Override
  public void infoWindowActuatorClick() {
    view.hideInfoWindow();
    ActuatorFragment fragment = ActuatorFragment.newInstance("", "");
    PageChangeUtil.newInstance().getPageChange().pageChange(fragment);
  }

  @Override
  public void infoWindowGraphClick() {
    view.hideInfoWindow();
    int valueType = ValueTpye.TEMPERATURE;
    GraphFragment fragment = GraphFragment.newInstance(currentSsensorItem.getSerial(), valueType);
    PageChangeUtil.newInstance().getPageChange().pageChange(fragment);
  }

  /////////////////////////////////
  ////////////AddWindow////////////
  /////////////////////////////////

  @Override
  public void addWindowCheckSerialClick(String serial) {
    view.startProgress();
    sensorDataAPI.getSensor(serial)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          if (result.getName().equals("null")) {
            view.checkSerialFail();
            serial_validate = false;
          } else {
            view.checkSerialSuccess();
            serial_validate = true;
          }
          view.stopProgress();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
  }

  @Override
  public void addWindowGetLocationClick() {
    view.fillEditLocation();
  }

  @Override
  public void addWindowOkClick(String serial, String title, String lat, String lng) {
    UpdateSensorData data = new UpdateSensorData(lat, lng, title);

    if(!serial_validate){
      view.showToast("Please, Check your serial");
    }else if(title.equals("")){
      view.showToast("Please, Input title");
    }else if (lat.equals("") || lng.equals("")) {
      String msgEmptyLocation = "Please, Touch 'Get Location'";
      view.showToast(msgEmptyLocation);
    } else {
      view.startProgress();
      if (isUpdate) {
        sensorDataAPI.updateMapSensor(serial, User.getInstance().getUserCode(), data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(result -> {
              reconstituteMap();
            }, error -> {
              MyNetworkExcetionHandling.excute(error, view, view);
            });
      } else {
        sensorDataAPI.insertMapSensor(serial, User.getInstance().getUserCode(), data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(result -> {
              reconstituteMap();
            }, error -> {
              MyNetworkExcetionHandling.excute(error, view, view);
            });
      }
    }
  }

  @Override
  public void addWindowCancelClick() {
    view.clearAddWindow();
    view.hideAddSensorWindow();
  }

  //////////////////////////////////////
  ////////////ConsitituteMap////////////
  //////////////////////////////////////

  private void constituteMap() {
    view.startProgress();
    String template = "yyyy-MM-dd";
    Locale locale = java.util.Locale.getDefault();
    SimpleDateFormat sdf = new SimpleDateFormat(template, locale);
    sensorDataAPI.getMapSensors(User.getInstance().getUserCode())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          sensorItems = result;
          for (SensorItem sensorItem : sensorItems) {
            sensorDataAPI.getValue(sensorItem.getSerial())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result1 -> {
                  Date date = new Date();
                  if (result1.getUpdate_date().equals(sdf.format(date.getTime()))) {
                    view.addMarker(sensorItem, true);
                  } else {
                    view.addMarker(sensorItem, false);
                  }
                }, error -> {
                  MyNetworkExcetionHandling.excute(error, view, view);
                  Log.i("error", error.toString());
                });
          }
          view.stopProgress();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
          Log.i("error", error.toString());
        });
  }

  private void reconstituteMap() {
    view.clearMap();
    constituteMap();
    view.hideAddSensorWindow();
    view.clearAddWindow();
    view.stopProgress();
  }

  @Override
  public void markerClick(String title) {
    for (SensorItem sensorItem : sensorItems) {
      if (sensorItem.getTitle().equals(title)) {
        currentSsensorItem = sensorItem;
        sensorDataAPI.getHumidity(sensorItem.getSerial())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(result -> {
              result.decrypt();
              view.showInfoWindow(sensorItem.getTitle(), sensorItem.getSerial(), result.getHumidity());
            });
      }
    }
  }

  //////////////////////////////////
  ////////////MapControl////////////
  //////////////////////////////////
  @Override
  public void btnZoomInClick() {
    zoomIndex++;
    view.refreshZoomButton(zoomIndex);
    view.refreshMapZoom(1);
  }

  @Override
  public void btnZoomOutClick() {
    zoomIndex--;
    view.refreshZoomButton(zoomIndex);
    view.refreshMapZoom(-1);
  }

  @Override
  public void btnLocationClick(GpsInfo gps) {

    if (gps.isGetLocation()) {
      double latitude = gps.getLatitude();
      double longitude = gps.getLongitude();
      view.refreshMapLocation(latitude, longitude, zoomIndex);
    } else {
      gps.showSettingsAlert();
    }
  }
}
