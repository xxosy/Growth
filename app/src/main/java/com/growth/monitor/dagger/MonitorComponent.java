package com.growth.monitor.dagger;

import com.growth.monitor.view.MonitorFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-10-06.
 */

@Component(modules = MonitorModule.class)
public interface MonitorComponent {
  void inject(MonitorFragment monitorFragment);
}
