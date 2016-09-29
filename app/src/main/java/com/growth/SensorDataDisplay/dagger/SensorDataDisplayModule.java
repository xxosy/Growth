package com.growth.SensorDataDisplay.dagger;

import com.growth.SensorDataDisplay.adapter.HarmfulListAdapter;
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapterDataModel;
import com.growth.SensorDataDisplay.adapter.HarmfulListAdapterDataView;
import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenter;
import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-08-25.
 */
@Module(includes = NetworkModule.class)
public class SensorDataDisplayModule {
    SensorDataDisplayPresenter.View view;
    HarmfulListAdapter harmfulListAdapter;
    public SensorDataDisplayModule(SensorDataDisplayPresenter.View view, HarmfulListAdapter harmfulListAdapter){
        this.view = view;
        this.harmfulListAdapter = harmfulListAdapter;
    }

    @Provides
    public HarmfulListAdapterDataModel provideHarmfulListAdapterDataModel(){
        return harmfulListAdapter;
    }
    @Provides
    public HarmfulListAdapterDataView provideHarmfulListAdapterDataView(){
        return harmfulListAdapter;
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
