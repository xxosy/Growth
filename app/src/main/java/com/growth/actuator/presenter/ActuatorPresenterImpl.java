package com.growth.actuator.presenter;

import android.util.Log;

import com.growth.domain.actuator.ActuatorState;
import com.growth.exception.MyNetworkExcetionHandling;
import com.growth.graph.presenter.GraphPresenter;
import com.growth.network.SensorDataAPI;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-09-20.
 */

public class ActuatorPresenterImpl implements ActuatorPresenter {
    ActuatorPresenter.View view;
    SensorDataAPI sensorDataAPI;
    ActuatorState mActuatorState;
    int[] iActuatorState;
    int currentSwitch;
    @Inject
    ActuatorPresenterImpl(ActuatorPresenter.View view, SensorDataAPI sensorDataAPI){
        this.view = view;
        this.sensorDataAPI = sensorDataAPI;
    }
    public void enter(){
        Log.i("TAG","asdasd");
        getActuatorState();
    }
    private void getActuatorState(){
        sensorDataAPI.getActuatorState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    mActuatorState = result;
                    parseState(mActuatorState.getState());
                    view.refreshActuatorState(iActuatorState);
                    Log.i("TAG",result.getState());
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                    Log.i("error",error.toString());
                });
    }
    @Override
    public void btnActuatorClick(int index) {
        if (iActuatorState[index]==0)
            iActuatorState[index] = 1;
        else if(iActuatorState[index]==1)
            iActuatorState[index] =0;
        mActuatorState.setState(parseStringState());
        Log.i("parseStringState()",parseStringState());
        sensorDataAPI.putActuatorState(mActuatorState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    getActuatorState();

                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                    Log.i("error",error.toString());
                });
    }
    @Override
    public void btnActuatorDetailClick(int index){
        currentSwitch = index;
        view.showActuatorDetail(index, iActuatorState[index]);
    }

    @Override
    public void btnActuatorDetailSwitchClick() {
        if (iActuatorState[currentSwitch]==0)
            iActuatorState[currentSwitch] = 1;
        else if(iActuatorState[currentSwitch]==1)
            iActuatorState[currentSwitch] =0;
        mActuatorState.setState(parseStringState());
        Log.i("parseStringState()",parseStringState());
        sensorDataAPI.putActuatorState(mActuatorState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    getActuatorState();
                    view.refreshActuatorDetailState(iActuatorState[currentSwitch]);
                },error->{
                    MyNetworkExcetionHandling.excute(error,view,view);
                    Log.i("error",error.toString());
                });
    }

    private void parseState(String state){
        iActuatorState = new int[state.length()];
        for(int i = 0;i<state.length();i++){
            iActuatorState[i]=Integer.valueOf(state.charAt(i))-48;
        }
    }
    private String parseStringState(){
        String stringState="";
        for(int i = 0;i<iActuatorState.length;i++){
            stringState+=String.valueOf(iActuatorState[i]);
        }
        return stringState;
    }
}
