package com.growth.graph.presenter;

import android.util.Log;

import com.growth.domain.sensor.SensorItem;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.graph.view.ValueTpye;
import com.growth.map.presenter.SensorMapPresenter;
import com.growth.network.SensorDataAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-08-29.
 */

public class GraphPresenterImpl implements  GraphPresenter {
    private GraphPresenter.View view;
    private SensorDataAPI sensorDataAPI;
    private String serial;
    private String sDate;
    private long lDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Inject
    public GraphPresenterImpl(GraphPresenter.View view, SensorDataAPI sensorDataAPI){
        this.view = view;
        this.sensorDataAPI = sensorDataAPI;

    }

    @Override
    public void enterFragment(String serial,int index) {
        this.serial = serial;
        Date d = new Date();
        lDate = d.getTime();
        sDate = sdf.format(d);
        requestPageData4Serial();
        view.refreshTab();
        requestGraphData(index);
    }

    @Override
    public void datePreButtonClick(int index) {
        lDate = lDate - (24*60*60*1000);
        sDate = sdf.format(lDate);
        view.refreshGraphDate(sDate);
        requestGraphData(index);
    }

    @Override
    public void dateNextButtonClick(int index) {
        Date d = new Date();
        Log.i(sdf.format(lDate),sdf.format(d.getTime()));
        if(sdf.format(lDate).equals(sdf.format(d.getTime()))){
            view.displayToast("Today is "+sdf.format(lDate));
        }else {
            lDate = lDate + (24 * 60 * 60 * 1000);
            sDate = sdf.format(lDate);
            view.refreshGraphDate(sDate);
            requestGraphData(index);
        }
    }

    @Override
    public void tabClick(int index) {
        Date d = new Date();
        lDate = d.getTime();
        sDate = sdf.format(d);
        requestPageData4Serial();
        requestGraphData(index);
        view.refreshTab();
    }

    private void requestGraphData(int index){
        view.startProgress();
        if(index== ValueTpye.TEMPERATURE){
            sensorDataAPI.getTemperatureList(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }else if(index==ValueTpye.HUMIDITY){
            sensorDataAPI.getHumidityList(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }else if(index==ValueTpye.CO2){
            sensorDataAPI.getCo2List(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }else if(index==ValueTpye.EC){
            sensorDataAPI.getEcList(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }else if(index==ValueTpye.PH){
            sensorDataAPI.getPhList(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }else if(index==ValueTpye.LIGHT){
            sensorDataAPI.getLightList(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }else if(index==ValueTpye.SOIL_MOISTURE){
            sensorDataAPI.getSoilMoistureList(serial,sDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        view.refreshChart(list);
                        view.stopProgress();
                    },error->{
                        MyNetworkExcetionHandling.excute(error,view,view);
                    });
        }
    }
    private void requestPageData4Serial(){
        view.startProgress();
        sensorDataAPI.getValue(serial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    view.refreshPage(result);
                    view.stopProgress();
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                });
    }

    public void setDate(String date) {
        this.sDate = date;
    }

    public String getDate() {
        return sDate;
    }
}
