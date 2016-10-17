package com.growth.map.dagger;

import com.growth.map.view.SensorMapFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-08-24.
 */
@Component(modules = SensorMapModule.class)
public interface SensorMapComponent {
  void inject(SensorMapFragment sensorMapFragment);
}
