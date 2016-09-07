package com.growth.SensorDataDisplay.presenter;

import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    boolean stateCamera = false;
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
                    view.refreshData(result);
                    view.stopProgress();
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                });
        new HttpUtil().execute();
    }

    @Override
    public void btnChangeCameraViewClick() {
//        view.refreshCameraImage();
        if(stateCamera){
            stateCamera = false;
            view.hideCameraFrame();
        }else{
            stateCamera = true;
            view.showCameraFrame();
        }
        view.changeBtnChageCameraView(stateCamera);
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

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            view.refreshWhether(weather, String.valueOf(temp1), humid);
        }
    }
}
