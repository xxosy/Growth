package com.growth.SensorDataDisplay.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.growth.SensorValueGuide;
import com.growth.domain.Value;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.graph.view.GraphFragment;
import com.growth.graph.view.ValueTpye;
import com.growth.home.PageChangeUtil;
import com.growth.network.SensorDataAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

public class SensorDataDisplayPresenterImpl implements SensorDataDisplayPresenter{
    SensorDataDisplayPresenter.View view;
    SensorDataAPI sensorDataAPI;

    HashMap<String,Boolean> states;
    String serial;
    int stateBtn = 0;
    @Inject
    SensorDataDisplayPresenterImpl(SensorDataDisplayPresenter.View view, SensorDataAPI sensorDataAPI){
        this.view = view;
        this.sensorDataAPI = sensorDataAPI;
        states = new HashMap<>();
    }

    @Override
    public void enterFragment(String serial) {
        view.startProgress();
        this.serial = serial;
        sensorDataAPI.getValue(serial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    states = getStates(result);
                    view.refreshState(states);
                    view.refreshStateView(result);
                    view.refreshData(result);
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                });
        new HttpUtil().execute();
    }
    @Override
    public void swipePage(String serial){
        this.serial = serial;
        sensorDataAPI.getValue(serial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    states = getStates(result);
                    view.refreshState(states);
                    view.refreshStateView(result);
                    view.refreshData(result);
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                });
        new HttpUtil().execute();
    }
    @Override
    public void btnChangeClick() {
        view.showButton();
    }

    @Override
    public void btnMosquitoClick() {
        getMosquitoImage(serial);
        stateBtn = 2;
        view.showCameraFrame();
        view.changeBtn(stateBtn);
        view.hideButton();
    }

    @Override
    public void btnViewClick() {
        stateBtn = 0;
        view.hideCameraFrame();
        view.changeBtn(stateBtn);
        view.hideButton();
    }

    @Override
    public void btnCameraClick() {
        stateBtn = 1;
        getCamgeraImage(serial);
        view.showCameraFrame();
        view.changeBtn(stateBtn);
        view.hideButton();
    }
    private void getMosquitoImage(String serial){
        sensorDataAPI.getSensor(serial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    new AsyncTask<Void,Void,Void>(){
                        Bitmap bmp = null;
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Log.i("url",result.getMosquito_url());
                                URL url = new URL(result.getMosquito_url()+"/tmpfs/auto.jpg");
                                URLConnection uc = url.openConnection();
                                String userpass = "admin" + ":" + "admin";
                                String basicAuth = "Basic " + new String(Base64.encode(userpass.getBytes(),Base64.DEFAULT));
                                uc.setRequestProperty ("Authorization", basicAuth);
                                InputStream in = uc.getInputStream();
                                bmp = BitmapFactory.decodeStream(in);

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e){
                                Log.i("error",e.toString());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            view.refreshCameraImage(bmp);
                        }
                    }.execute();
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                });
    }
    private void getCamgeraImage(String serial){
        sensorDataAPI.getSensor(serial)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    new AsyncTask<Void,Void,Void>(){
                        Bitmap bmp = null;
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Log.i("url",result.getUrl());
                                URL url = new URL(result.getUrl()+"/tmpfs/auto.jpg");
                                URLConnection uc = url.openConnection();
                                String userpass = "admin" + ":" + "admin";
                                String basicAuth = "Basic " + new String(Base64.encode(userpass.getBytes(),Base64.DEFAULT));
                                uc.setRequestProperty ("Authorization", basicAuth);
                                InputStream in = uc.getInputStream();
                                bmp = BitmapFactory.decodeStream(in);

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e){
                                Log.i("error",e.toString());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            view.refreshCameraImage(bmp);
                        }
                    }.execute();
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
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
    private void goGraph(int index){
        PageChangeUtil.newInstance().getPageChange().pageChange(new GraphFragment().newInstance(serial,index));
    }

    private HashMap<String,Boolean> getStates(Value result){
        HashMap<String,Boolean> states = new HashMap<>();
        float temp = Float.parseFloat(result.getTemperature());
        float humidity = Float.parseFloat(result.getHumidity());
        float light = Float.parseFloat(result.getLight());
        float ph = Float.parseFloat(result.getPh());
        float ec = Float.parseFloat(result.getEc());
        float co2 = Float.parseFloat(result.getCo2());
        if(temp> SensorValueGuide.GUIDE_TEMP_MAX || temp<SensorValueGuide.GUIDE_TEMP_MIN){
            states.put("temp",false);
        }else{
            states.put("temp",true);
        }
        if(humidity>SensorValueGuide.GUIDE_HUMIDITY_MAX || humidity<SensorValueGuide.GUIDE_HUMIDITY_MIN){
            states.put("humidity",false);
        }else{
            states.put("humidity",true);
        }
        if(light>SensorValueGuide.GUIDE_LIGHT_MAX || light<SensorValueGuide.GUIDE_LIGHT_MIN){
            states.put("light",false);
        }else{
            states.put("light",true);
        }
        if(co2>SensorValueGuide.GUIDE_CO2_MAX || co2<SensorValueGuide.GUIDE_CO2_MIN){
            states.put("co2",false);
        }else{
            states.put("co2",true);
        }
        if(ph>SensorValueGuide.GUIDE_PH_MAX || ph<SensorValueGuide.GUIDE_PH_MIN){
            states.put("ph",false);
        }else{
            states.put("ph",true);
        }
        if(ec>SensorValueGuide.GUIDE_EC_MAX || ec<SensorValueGuide.GUIDE_EC_MIN){
            states.put("ec",false);
        }else{
            states.put("ec",true);
        }
        return states;
    }
    public class HttpUtil extends AsyncTask<String, Void, Void> {
        String humid;
        double temp1;
        String weather;
        Bitmap mIcon = null;
        @Override
        public Void doInBackground(String... params) {
            try {
                String url = "http://api.openweathermap.org/data/2.5/weather?lat=35.82&lon=127.15&APPID=c3a6ae5ba98a12123a17b8f506e26fe6";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                Log.i("result", "ddasda");
                int retCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer response = new StringBuffer();
                response.append(br.readLine());

                br.close();
                String res = response.toString();
                JSONObject json = new JSONObject(res);
                JSONObject main = json.getJSONObject("main");
                String temp = main.getString("temp");
                humid = main.getString("humidity");
                JSONArray wea = json.getJSONArray("weather");
                weather = wea.getJSONObject(0).getString("main");
                temp1 = Double.valueOf(temp) - 272.15;
                temp1 = Double.parseDouble(String.format("%.1f", temp1));
                String icon = wea.getJSONObject(0).getString("icon");
                String urldisplay = "http://openweathermap.org/img/w/"+icon+".png";

                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                mIcon = Bitmap.createScaledBitmap(mIcon,mIcon.getWidth()*3,mIcon.getHeight()*3,true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.refreshWhether(weather, String.valueOf(temp1), humid,mIcon);
            view.refreshSwipe();
            view.stopProgress();
        }
    }
}
