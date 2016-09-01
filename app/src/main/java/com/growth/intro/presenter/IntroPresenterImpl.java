package com.growth.intro.presenter;

import android.util.Log;

import com.growth.domain.database.DBManager;
import com.growth.domain.sensor.SensorItem;
import com.growth.domain.user.User;
import com.growth.network.SensorDataAPI;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SSL-D on 2016-08-31.
 */

public class IntroPresenterImpl implements IntroPresenter {
    IntroPresenter.View view;
    SensorDataAPI sensorDataAPI;
    DBManager mDBManager;
    String userCode;
    boolean checkUserCode;
    @Inject
    public IntroPresenterImpl(IntroPresenter.View view, SensorDataAPI sensorDataAPI){
        this.view = view;
        this.sensorDataAPI = sensorDataAPI;
        checkUserCode = false;
    }
    @Override
    public void setDBManager(DBManager mDBManager) {
        this.mDBManager = mDBManager;
    }

    @Override
    public void setUserCode() {
        if(mDBManager.getCount()>0){
        }else{
            checkUserCode();

            mDBManager.createUserCode(userCode);
        }
        User.getInstance().setUserCode(mDBManager.getUserCode());
    }
    private String createUserCode(){
        String tempUserCode = "";
        for (int i = 0; i < 8; i++) {
            int rndVal = (int) (Math.random() * 62);
            if (rndVal < 10) {
                tempUserCode += rndVal;
            } else if (rndVal > 35) {
                tempUserCode += (char) (rndVal + 61);
            } else {
                tempUserCode += (char) (rndVal + 55);
            }
        }
        userCode = tempUserCode;
        return tempUserCode;
    }
    private void checkUserCode(){
        sensorDataAPI.insertUserCode(createUserCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result1 -> {
                });
    }
}
