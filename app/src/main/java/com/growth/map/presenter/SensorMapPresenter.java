package com.growth.map.presenter;

import com.growth.domain.sensor.SensorItem;
import com.growth.views.PageChange;

/**
 * Created by SSL-D on 2016-08-23.
 */

public interface SensorMapPresenter {
    void floatingActionButtonClick();
    void infoWindowDetailClick();
    void infoWindowDeleteSensorClick();
    void infoWindowUpdateSensorClick();
    void infoWindowGraphClick();
    void addWindowCheckSerialClick(String serial);
    void addWindowGetLocationClick();
    void addWindowOkClick(String serial, String title, String lat, String lng);
    void addWindowCancelClick();
    void enterFragment();
    void markerClick(String title);
    void onInfoWindowDetailButtonClick(String serial);

    interface View{
        void clearAddWindow();
        void refreshMarker();
        void refreshInfoWindow();
        void addMarker(SensorItem sensorItem);
        void showInfoWindow(String title, String serial, String humidity);
        void hideInfoWindow();
        void showAddSensorWindow();
        void hideAddSensorWindow();
        void fillEditLocation();
        void checkSerialFail();
        void checkSerialSuccess();
        void clearMap();
        void refreshAddWindowUpdateSensor(String serial,String title, String lat, String lng);
    }
}
