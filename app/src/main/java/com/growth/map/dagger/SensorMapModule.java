package com.growth.map.dagger;

import com.growth.map.presenter.SensorMapPresenter;
import com.growth.map.presenter.SensorMapPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-08-23.
 */
@Module(includes = NetworkModule.class)
public class SensorMapModule {
    SensorMapPresenter.View view;

    public SensorMapModule(SensorMapPresenter.View view){this.view = view;}

    @Provides
    public SensorMapPresenter provideSensorMapPresenter(SensorMapPresenterImpl sensorMapPresenter){
        return sensorMapPresenter;
    }

    @Provides
    public SensorMapPresenter.View provideView(){
        return view;
    }
}
