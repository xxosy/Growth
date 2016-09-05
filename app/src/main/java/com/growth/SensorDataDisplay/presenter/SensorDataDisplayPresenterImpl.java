package com.growth.SensorDataDisplay.presenter;

import android.util.Log;

import com.growth.SensorValueGuide;
import com.growth.domain.Value;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.network.SensorDataAPI;

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
}
