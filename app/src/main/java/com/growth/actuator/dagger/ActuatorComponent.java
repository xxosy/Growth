package com.growth.actuator.dagger;

import com.growth.actuator.view.ActuatorFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-09-20.
 */
@Component(modules = ActuatorModule.class)
public interface ActuatorComponent {
    void inject(ActuatorFragment actuatorFragment);
}
