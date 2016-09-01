package com.growth.SensorDataDisplay.dagger;

import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenter;
import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenterImpl;
import com.growth.SensorDataDisplay.view.SensorDataDisplayFragment;
import com.growth.map.presenter.SensorMapPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-08-25.
 */
@Module(includes = NetworkModule.class)
public class SensorDataDisplayModule {
    SensorDataDisplayPresenter.View view;

    public SensorDataDisplayModule(SensorDataDisplayPresenter.View view){
        this.view = view;
    }

    @Provides
    public SensorDataDisplayPresenter provideSensorDataDisplayPresenter(SensorDataDisplayPresenterImpl sensorDataDisplayPresenter){
        return sensorDataDisplayPresenter;
    }
    @Provides
    public SensorDataDisplayPresenter.View provideView(){
        return view;
    }
}
