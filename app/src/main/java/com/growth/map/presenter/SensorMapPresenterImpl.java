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

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


// Created by SSL-D on 2016-08-23.

public class SensorMapPresenterImpl implements SensorMapPresenter {
  private static final String TAG = SensorMapPresenterImpl.class.getName();
  private SensorMapPresenter.View view;
  private SensorDataAPI sensorDataAPI;
  private SensorItem[] sensorItems;
  private SensorItem currentSensorItem;
  private boolean isUpdate = false;
  private boolean serial_validate = false;
  private int zoomIndex = 11;
  private CompositeSubscription subscriptions = new CompositeSubscription();

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
    SensorDataDisplayFragment fragment = SensorDataDisplayFragment.newInstance(currentSensorItem.getSerial(), "");
    PageChangeUtil.newInstance()
        .getPageChange()
        .pageChange(fragment);
  }

  @Override
  public void infoWindowDeleteSensorClick() {
    view.startProgress();
    Subscription subscription = sensorDataAPI.deleteMapSensor(currentSensorItem.getSerial(), User.getInstance().getUserCode())
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
    subscriptions.add(subscription);
  }

  @Override
  public void infoWindowUpdateSensorClick() {
    view.hideInfoWindow();
    isUpdate = true;
    view.refreshAddWindowUpdateSensor(currentSensorItem.getSerial(),
        currentSensorItem.getTitle(),
        currentSensorItem.getLat(),
        currentSensorItem.getLng());
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
    GraphFragment fragment = GraphFragment.newInstance(currentSensorItem.getSerial(), valueType);
    PageChangeUtil.newInstance()
        .getPageChange()
        .pageChange(fragment);
  }

  /////////////////////////////////
  ////////////AddWindow////////////
  /////////////////////////////////

  @Override
  public void addWindowCheckSerialClick(String serial) {
    view.startProgress();
    Subscription subscription = sensorDataAPI.getSensor(serial)
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
    subscriptions.add(subscription);
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
        updateMapSensor(serial, data);
      } else {
        insertMapSensor(serial, data);
      }
    }
  }

  private void updateMapSensor(String serial, UpdateSensorData data){
    Subscription subscription = sensorDataAPI.updateMapSensor(serial, User.getInstance().getUserCode(), data)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          reconstituteMap();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    subscriptions.add(subscription);
  }

  private void insertMapSensor(String serial, UpdateSensorData data){
    Subscription subscription = sensorDataAPI.insertMapSensor(serial, User.getInstance().getUserCode(), data)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          reconstituteMap();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    subscriptions.add(subscription);
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
    String template = new StringBuilder("yyyy-MM-dd").toString();
    Locale locale = java.util.Locale.getDefault();
    SimpleDateFormat sdf = new SimpleDateFormat(template, locale);

    Subscription subscription = sensorDataAPI.getMapSensors(User.getInstance().getUserCode())
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
    subscriptions.add(subscription);
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
        currentSensorItem = sensorItem;
        Subscription subscription = sensorDataAPI.getHumidity(sensorItem.getSerial())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(result -> {
              result.decrypt();
              view.showInfoWindow(sensorItem.getTitle(), sensorItem.getSerial(), result.getHumidity());
            });
        subscriptions.add(subscription);
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

  @Override
  public void unSubscribe() {
    subscriptions.unsubscribe();
  }
}
