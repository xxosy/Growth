package com.growth.graph.presenter;

import android.util.Log;

import com.growth.domain.graph.GraphList;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.graph.view.ValueTpye;
import com.growth.network.SensorDataAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SSL-D on 2016-08-29.
 */

public class GraphPresenterImpl implements GraphPresenter {
  private GraphPresenter.View view;
  private SensorDataAPI sensorDataAPI;
  private String serial;
  private String sDate;
  private long lDate;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  private CompositeSubscription subscriptions = new CompositeSubscription();
  @Inject
  GraphPresenterImpl(GraphPresenter.View view, SensorDataAPI sensorDataAPI) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
  }

  @Override
  public void enterFragment(String serial, int index) {
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
    lDate = lDate - (24 * 60 * 60 * 1000);
    sDate = sdf.format(lDate);
    view.refreshGraphDate(sDate);
    requestGraphData(index);
  }

  @Override
  public void dateNextButtonClick(int index) {
    Date d = new Date();
    Log.i(sdf.format(lDate), sdf.format(d.getTime()));
    if (sdf.format(lDate).equals(sdf.format(d.getTime()))) {
      view.displayToast("오늘의 날짜는 " + sdf.format(lDate)+" 입니다.");
    } else {
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

  private void requestGraphData(int index) {
    view.startProgress();
    Observable<GraphList> observable = sensorDataAPI.getTemperatureList(serial, sDate);
    if (index == ValueTpye.TEMPERATURE) {
      observable = sensorDataAPI.getTemperatureList(serial, sDate);
    } else if (index == ValueTpye.HUMIDITY) {
      observable = sensorDataAPI.getHumidityList(serial, sDate);
    } else if (index == ValueTpye.CO2) {
      observable = sensorDataAPI.getCo2List(serial, sDate);
    } else if (index == ValueTpye.EC) {
      observable = sensorDataAPI.getEcList(serial, sDate);
    } else if (index == ValueTpye.PH) {
      observable = sensorDataAPI.getPhList(serial, sDate);
    } else if (index == ValueTpye.LIGHT) {
      observable = sensorDataAPI.getLightList(serial, sDate);
    } else if (index == ValueTpye.SOIL_MOISTURE) {
      observable = sensorDataAPI.getSoilMoistureList(serial, sDate);
    }
    Subscription subscription = observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(list -> {
          list.decrypt();
          view.refreshChart(list);
          view.stopProgress();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    subscriptions.add(subscription);
  }

  private void requestPageData4Serial() {
    view.startProgress();
    sensorDataAPI.getValue(serial)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          result.decrypt();
          view.refreshPage(result);
          view.stopProgress();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
  }

  @Override
  public void unSubscribe(){
    subscriptions.unsubscribe();
  }
}
