package com.growth.map.presenter;

import com.growth.domain.sensor.SensorItem;
import com.growth.utils.GpsInfo;
import com.growth.utils.ProgressControl;
import com.growth.utils.ToastControl;

/**
 * Created by SSL-D on 2016-08-23.
 */

public interface SensorMapPresenter {
  void onCreatedView();

  void markerClick(String title);

  void floatingActionButtonClick();

  void infoWindowDetailClick();

  void infoWindowDeleteSensorClick();

  void infoWindowUpdateSensorClick();

  void infoWindowActuatorClick();

  void infoWindowGraphClick();

  void addWindowCheckSerialClick(String serial);

  void addWindowGetLocationClick();

  void addWindowOkClick(String serial, String title, String lat, String lng);

  void addWindowCancelClick();

  void btnZoomInClick();

  void btnZoomOutClick();

  void btnLocationClick(GpsInfo gps);

  void unSubscribe();
  interface View extends ProgressControl, ToastControl {

    void showInfoWindow(String title, String serial, String humidity);

    void hideInfoWindow();

    void clearMap();

    void showAddSensorWindow();

    void hideAddSensorWindow();

    void checkSerialFail();

    void checkSerialSuccess();

    void fillEditLocation();

    void clearAddWindow();

    void refreshAddWindowUpdateSensor(String serial, String title, String lat, String lng);

    void addMarker(SensorItem sensorItem, boolean isUpdating);

    void refreshMapZoom(int index);

    void refreshZoomButton(int index);

    void refreshMapLocation(double lat, double lng, int index);

    void showToast(String msg);
  }
}
