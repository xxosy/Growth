package com.growth.home.presenter;

import com.growth.network.SensorDataAPI;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by SSL-D on 2016-07-20.
 */

public class HomePresenterImpl implements HomePresenter {
    private View view;
    private SensorDataAPI sensorDataAPI;
    private Subscription initSubscription;
    private PublishSubject subject;
    private int currentValue;
    private int currentSensor;

    @Inject
    public HomePresenterImpl(View view, SensorDataAPI sensorDataAPI){
        this.view = view;
        this.sensorDataAPI = sensorDataAPI;
        subject = PublishSubject.create();
    }

    @Override
    public void initHome() {
//        sensorDataAPI.getSensorList()
//                .subscribeOn(Schedulers.io())
//                .flatMap(result-> Observable.from(result))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(result -> {
//
//                });
    }

}
