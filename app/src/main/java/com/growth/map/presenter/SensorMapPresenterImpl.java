package com.growth.map.presenter;

import android.util.Log;

import com.growth.GpsInfo;
import com.growth.SensorDataDisplay.view.SensorDataDisplayFragment;
import com.growth.domain.UpdateSensorData;
import com.growth.domain.sensor.SensorItem;
import com.growth.domain.user.User;
import com.growth.graph.view.GraphFragment;
import com.growth.home.PageChangeUtil;
import com.growth.network.SensorDataAPI;
import com.growth.views.PageChange;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-08-23.
 */

public class SensorMapPresenterImpl implements SensorMapPresenter{
    private SensorMapPresenter.View view;
    private SensorDataAPI sensorDataAPI;
    private SensorItem[] sensorItems;
    private SensorItem currentSsensorItem;
    private boolean isUpdate = false;
    private int zoomIndex = 11;
    @Inject
    public SensorMapPresenterImpl(SensorMapPresenter.View view, SensorDataAPI sensorDataAPI){
        this.view = view;
        this.sensorDataAPI = sensorDataAPI;
    }

    @Override
    public void enterFragment() {
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
        PageChangeUtil.newInstance().getPageChange().pageChange(new SensorDataDisplayFragment().newInstance(currentSsensorItem.getSerial(),""));
    }

    @Override
    public void infoWindowDeleteSensorClick() {
        sensorDataAPI.deleteMapSensor(currentSsensorItem.getSerial(),User.getInstance().getUserCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result ->{
                    view.clearMap();
                    constituteMap();
                    view.hideInfoWindow();
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
    public void infoWindowGraphClick() {
        view.hideInfoWindow();
        PageChangeUtil.newInstance().getPageChange().pageChange(new GraphFragment().newInstance(currentSsensorItem.getSerial(),""));
    }

    /////////////////////////////////
    ////////////AddWindow////////////
    /////////////////////////////////
    @Override
    public void addWindowCheckSerialClick(String serial) {
        sensorDataAPI.getSensor(serial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result ->{
                    if(result.getName().equals("null")){
                        view.checkSerialFail();
                    }else
                        view.checkSerialSuccess();
                });
    }

    @Override
    public void addWindowGetLocationClick() {
        view.fillEditLocation();
    }

    private void constituteMap(){
        sensorDataAPI.getMapSensors(User.getInstance().getUserCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result ->{
                    sensorItems = result;
                    for(SensorItem sensorItem:sensorItems){
                        view.addMarker(sensorItem);
                    }
                });
    }

    @Override
    public void addWindowOkClick(String serial, String title, String lat, String lng) {
        UpdateSensorData data = new UpdateSensorData(lat, lng, title);
        if(lat.equals("")||lng.equals("")){
            view.showToast("Please, Touch 'Get Location'");
        }else {
            if (isUpdate) {
                sensorDataAPI.updateMapSensor(serial, User.getInstance().getUserCode(), data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            view.clearMap();
                            constituteMap();
                            view.hideAddSensorWindow();
                            view.clearAddWindow();
                        });
            } else {
                sensorDataAPI.insertMapSensor(serial, User.getInstance().getUserCode(), data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            view.clearMap();
                            constituteMap();
                            view.hideAddSensorWindow();
                            view.clearAddWindow();
                        });
            }
        }
    }

    @Override
    public void addWindowCancelClick() {
        view.clearAddWindow();
        view.hideAddSensorWindow();
    }

    @Override
    public void markerClick(String title) {
        for(SensorItem sensorItem:sensorItems) {
            if (sensorItem.getTitle().equals(title)){
                currentSsensorItem = sensorItem;
                sensorDataAPI.getHumidity(sensorItem.getSerial())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result ->{
                            view.showInfoWindow(sensorItem.getTitle(),sensorItem.getSerial(),result.getHumidity());
                        });
            }
        }
    }

    @Override
    public void onInfoWindowDetailButtonClick(String serial) {

    }

    @Override
    public void btnZoomInClick() {
        zoomIndex++;
        view.refreshZoomButtom(zoomIndex);
        view.refreshMapZoom(+1);
    }

    @Override
    public void btnZoomOutClick() {
        zoomIndex--;
        view.refreshZoomButtom(zoomIndex);
        view.refreshMapZoom(-1);
    }

    @Override
    public void btnLocationClick(GpsInfo gps) {

        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            view.refreshMapLocation(latitude,longitude);
        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }
    }
}
