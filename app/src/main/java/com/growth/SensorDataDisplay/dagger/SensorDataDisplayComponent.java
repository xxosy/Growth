package com.growth.SensorDataDisplay.dagger;

import com.growth.SensorDataDisplay.presenter.SensorDataDisplayPresenter;
import com.growth.SensorDataDisplay.view.SensorDataDisplayFragment;

import javax.inject.Inject;

import dagger.Component;

/**
 * Created by SSL-D on 2016-08-25.
 */
@Component(modules = SensorDataDisplayModule.class)
public interface SensorDataDisplayComponent {
  void inject(SensorDataDisplayFragment sensorDataDisplayFragment);
}
