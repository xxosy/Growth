package com.growth.SensorDataDisplay.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

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

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-08-25.
 */

public class SensorDataDisplayPresenterImpl implements SensorDataDisplayPresenter {
  SensorDataDisplayPresenter.View view;
  SensorDataAPI sensorDataAPI;
  HarmfulListAdapterDataModel mHarmfulListAdapterDataModel;
  HashMap<String, Boolean> states;
  String serial;
  int stateBtn = 0;
  boolean isBtnsShow = false;
  WeatherItem weather;
  @Inject
  SensorDataDisplayPresenterImpl(SensorDataDisplayPresenter.View view, SensorDataAPI sensorDataAPI, HarmfulListAdapterDataModel harmfulListAdapterDataModel) {
    this.view = view;
    this.sensorDataAPI = sensorDataAPI;
    states = new HashMap<>();
    this.mHarmfulListAdapterDataModel = harmfulListAdapterDataModel;
  }

  @Override
  public void enterFragment(String serial) {
    view.startProgress();
    this.serial = serial;
    sensorDataAPI.getValue(serial)
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
  }
  private void getWeatherItem(){
    sensorDataAPI.getWeather()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result ->{
          weather = result;
          String strWeather = weather.getWeather()[0].getMain();
          double temp = Double.valueOf(weather.getMain().getTemp()) - 272.15;
          temp = Double.parseDouble(String.format("%.1f", temp));
          String humid = String.valueOf(weather.getMain().getHumidity());
          String icon = weather.getWeather()[0].getIcon();
          String urldisplay = "http://openweathermap.org/img/w/" + icon + ".png";
          view.refreshWhether(strWeather,String.valueOf(temp),humid,urldisplay);
          view.refreshSwipe();
          view.stopProgress();
        });
  }

  @Override
  public void swipePage(String serial) {
    this.serial = serial;
    sensorDataAPI.getValue(serial)
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
    sensorDataAPI.getHarmfulData("insect")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          mHarmfulListAdapterDataModel.clear();
          for (HarmfulData item : result) {
            mHarmfulListAdapterDataModel.add(item);
          }
          view.refreshHarmfulList();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    getMosquitoImage(serial);
    stateBtn = 2;
    view.showCameraFrame();
    view.changeBtn(stateBtn);
    isBtnsShow = false;
    view.hideButton();
    view.hideHarmfulDetail();
    view.showHarmfulList();
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
    sensorDataAPI.getHarmfulData("none")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          mHarmfulListAdapterDataModel.clear();
          for (HarmfulData item : result) {
            mHarmfulListAdapterDataModel.add(item);
          }
          view.refreshHarmfulList();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
    stateBtn = 1;
    getCameraImage(serial);
    view.showCameraFrame();
    view.changeBtn(stateBtn);
    isBtnsShow = false;
    view.hideHarmfulDetail();
    view.hideButton();
    view.showHarmfulList();
  }

  private void getMosquitoImage(String serial) {
    sensorDataAPI.getSensor(serial)
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
              view.refreshCameraImage(bmp);
            }
          }.execute();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
  }

  private void getCameraImage(String serial) {
    sensorDataAPI.getSensor(serial)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          new AsyncTask<Void, Void, Void>() {
            Bitmap bmp = null;

            @Override
            protected Void doInBackground(Void... params) {
              try {
                Log.i("url", result.getUrl());
                URL url = new URL(result.getUrl() + "/tmpfs/auto.jpg");
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
              view.refreshCameraImage(bmp);
            }
          }.execute();
        }, error -> {
          MyNetworkExcetionHandling.excute(error, view, view);
        });
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
      states.put("temp", false);
    } else {
      states.put("temp", true);
    }
    if (humidity > SensorValueGuide.GUIDE_HUMIDITY_MAX || humidity < SensorValueGuide.GUIDE_HUMIDITY_MIN) {
      states.put("humidity", false);
    } else {
      states.put("humidity", true);
    }
    if (light > SensorValueGuide.GUIDE_LIGHT_MAX || light < SensorValueGuide.GUIDE_LIGHT_MIN) {
      states.put("light", false);
    } else {
      states.put("light", true);
    }
    if (co2 > SensorValueGuide.GUIDE_CO2_MAX || co2 < SensorValueGuide.GUIDE_CO2_MIN) {
      states.put("co2", false);
    } else {
      states.put("co2", true);
    }
    if (ph > SensorValueGuide.GUIDE_PH_MAX || ph < SensorValueGuide.GUIDE_PH_MIN) {
      states.put("ph", false);
    } else {
      states.put("ph", true);
    }
    if (ec > SensorValueGuide.GUIDE_EC_MAX || ec < SensorValueGuide.GUIDE_EC_MIN) {
      states.put("ec", false);
    } else {
      states.put("ec", true);
    }
    return states;
  }


}
