package com.growth.actuator.dagger;

import com.growth.actuator.presenter.ActuatorPresenter;
import com.growth.actuator.presenter.ActuatorPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-09-20.
 */
@Module(includes = NetworkModule.class)
public class ActuatorModule {
  ActuatorPresenter.View view;

  public ActuatorModule(ActuatorPresenter.View view) {
    this.view = view;
  }

  @Provides
  ActuatorPresenter providePresenter(ActuatorPresenterImpl actuatorPresenter) {
    return actuatorPresenter;
  }

  @Provides
  ActuatorPresenter.View provideView() {
    return view;
  }
}
