package com.growth.SensorDataDisplay.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.growth.SensorDataDisplay.StateTag;
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapterDataModel;
import com.growth.SensorValueGuide;
import com.growth.domain.Value;
import com.growth.domain.harmful.HarmfulData;
import com.growth.domain.weather.WeatherItem;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.graph.view.GraphFragment;
import com.growth.graph.view.ValueTpye;
import com.growth.home.PageChangeUtil;
import com.growth.network.SensorDataAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by SSL-D on 2016-08-25.
 */

public class SensorDataDisplayPresenterImpl implements SensorDataDisplayPresenter {
  private SensorDataDisplayPresenter.View view;
  private SensorDataAPI sensorDataAPI;
  private HarmfulListAdapterDataModel mHarmfulListAdapterDataModel;
  private HashMap<String, Boolean> states;
  private String serial;
  private int stateBtn = 0;
  private boolean isBtnsShow = false;
  private WeatherItem weather;
  private CompositeSubscription subscriptions = new CompositeSubscription();
  @Inject
  SensorDataDisplayPresenterImpl(SensorDataDisplayPresenter.View view, SensorDataAPI sensorDataAPI, HarmfulListAdapterDataModel harmfulListAdapterDataModel) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
    states = new HashMap<>();
    this.mHarmfulListAdapterDataModel = harmfulListAdapterDataModel;
  }

  @Override
  public void onCreatedView(String serial) {
    view.startProgress();
    swipePage(serial);
  }
  private void getWeatherItem(){
    Subscription subscription = sensorDataAPI.getWeather()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result ->{
          weather = result;
          String strWeather = weather.getWeather()[0].getMain();
          double temp = (double) weather.getMain().getTemp() - 272.15;
          temp = Double.parseDouble(String.format("%.1f", temp));
          String humid = String.valueOf(weather.getMain().getHumidity());
          String icon = weather.getWeather()[0].getIcon();
          String urldisplay = "http://openweathermap.org/img/w/" + icon + ".png";
          view.refreshWhether(strWeather,String.valueOf(temp),humid,urldisplay);
          view.refreshSwipe();
          view.stopProgress();
        });
    subscriptions.add(subscription);
  }

  @Override
  public void swipePage(String serial) {
    this.serial = serial;
    Subscription subscription = sensorDataAPI.getValue(serial)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          result.decrypt();
          states = getStates(result);
          view.refreshState(states);
          view.refreshStateView(result);
          view.refreshData(result);
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    getWeatherItem();
    subscriptions.add(subscription);
  }

  @Override
  public void OnRecyclerItemClick(int position) {
    HarmfulData item = mHarmfulListAdapterDataModel.getItem(position);
    view.showHarmfulDetail(item.getTitle(), item.getDescription(), item.getImgurl());
  }

  @Override
  public void btnChangeClick() {
    if (isBtnsShow) {
      isBtnsShow = false;
      view.hideButton();
    } else {
      isBtnsShow = true;
      view.showButton();
    }
  }

  @Override
  public void btnMosquitoClick() {
    Subscription subscription = sensorDataAPI.getHarmfulData("insect")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          stateBtn = 2;
          clearAndAddHarmfulList(result);
          getMosquitoImage(serial);
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    subscriptions.add(subscription);
  }

  @Override
  public void btnViewClick() {
    stateBtn = 0;
    view.hideCameraFrame();
    view.changeBtn(stateBtn);
    isBtnsShow = false;
    view.hideButton();
    view.hideHarmfulList();
    view.hideHarmfulDetail();
  }

  @Override
  public void btnCameraClick() {
    Subscription subscription = sensorDataAPI.getHarmfulData("none")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          stateBtn = 1;
          clearAndAddHarmfulList(result);
          view.refreshCameraImage(serial);
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    subscriptions.add(subscription);
  }
  private void clearAndAddHarmfulList(HarmfulData[] data){
    mHarmfulListAdapterDataModel.clear();
    for (HarmfulData item : data) {
      mHarmfulListAdapterDataModel.add(item);
    }
    view.refreshHarmfulList();
    view.showCameraFrame();
    view.changeBtn(stateBtn);
    isBtnsShow = false;
    view.hideHarmfulDetail();
    view.hideButton();
    view.showHarmfulList();
  }
  private void getMosquitoImage(String serial) {
    Subscription subscription = sensorDataAPI.getSensor(serial)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          new AsyncTask<Void, Void, Void>() {
            Bitmap bmp = null;

            @Override
            protected Void doInBackground(Void... params) {
              try {
                Log.i("url", result.getMosquito_url());
                URL url = new URL(result.getMosquito_url() + "/tmpfs/auto.jpg");
                URLConnection uc = url.openConnection();
                String userpass = "admin" + ":" + "admin";
                String basicAuth = "Basic " + new String(Base64.encode(userpass.getBytes(), Base64.DEFAULT));
                uc.setRequestProperty("Authorization", basicAuth);
                InputStream in = uc.getInputStream();
                bmp = BitmapFactory.decodeStream(in);

              } catch (MalformedURLException e) {
                e.printStackTrace();
              } catch (IOException e) {
                e.printStackTrace();
              } catch (Exception e) {
                Log.i("error", e.toString());
              }
              return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
              super.onPostExecute(aVoid);
              view.refreshMosquitoImage(bmp);
            }
          }.execute();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    subscriptions.add(subscription);
  }

  @Override
  public void btnGraphTempClick() {
    goGraph(ValueTpye.TEMPERATURE);
  }

  @Override
  public void btnGraphHumidityClick() {
    goGraph(ValueTpye.HUMIDITY);
  }

  @Override
  public void btnGraphLightClick() {
    goGraph(ValueTpye.LIGHT);
  }

  @Override
  public void btnGraphCo2Click() {
    goGraph(ValueTpye.CO2);
  }

  @Override
  public void btnGraphEcClick() {
    goGraph(ValueTpye.EC);
  }

  @Override
  public void btnGraphPhClick() {
    goGraph(ValueTpye.PH);
  }

  private void goGraph(int index) {
    PageChangeUtil.newInstance().getPageChange().pageChange(new GraphFragment().newInstance(serial, index));
  }

  private HashMap<String, Boolean> getStates(Value result) {
    HashMap<String, Boolean> states = new HashMap<>();
    float temp = Float.parseFloat(result.getTemperature());
    float humidity = Float.parseFloat(result.getHumidity());
    float light = Float.parseFloat(result.getLight());
    float ph = Float.parseFloat(result.getPh());
    float ec = Float.parseFloat(result.getEc());
    float co2 = Float.parseFloat(result.getCo2());
    if (temp > SensorValueGuide.GUIDE_TEMP_MAX || temp < SensorValueGuide.GUIDE_TEMP_MIN) {
      states.put(StateTag.TEMPERATURE, false);
    } else {
      states.put(StateTag.TEMPERATURE, true);
    }
    if (humidity > SensorValueGuide.GUIDE_HUMIDITY_MAX || humidity < SensorValueGuide.GUIDE_HUMIDITY_MIN) {
      states.put(StateTag.HUMIDITY, false);
    } else {
      states.put(StateTag.HUMIDITY, true);
    }
    if (light > SensorValueGuide.GUIDE_LIGHT_MAX || light < SensorValueGuide.GUIDE_LIGHT_MIN) {
      states.put(StateTag.LIGHT, false);
    } else {
      states.put(StateTag.LIGHT, true);
    }
    if (co2 > SensorValueGuide.GUIDE_CO2_MAX || co2 < SensorValueGuide.GUIDE_CO2_MIN) {
      states.put(StateTag.CO2, false);
    } else {
      states.put(StateTag.CO2, true);
    }
    if (ph > SensorValueGuide.GUIDE_PH_MAX || ph < SensorValueGuide.GUIDE_PH_MIN) {
      states.put(StateTag.PH, false);
    } else {
      states.put(StateTag.PH, true);
    }
    if (ec > SensorValueGuide.GUIDE_EC_MAX || ec < SensorValueGuide.GUIDE_EC_MIN) {
      states.put(StateTag.EC, false);
    } else {
      states.put(StateTag.EC, true);
    }
    return states;
  }

  @Override
  public void unSubscribe() {
    subscriptions.unsubscribe();
  }
}
