package com.growth.home.dagger;

import com.growth.home.presenter.HomePresenter;
import com.growth.home.presenter.HomePresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-07-20.
 */
@Module(includes = NetworkModule.class)
public class HomeModule {
  private HomePresenter.View view;

  public HomeModule(HomePresenter.View view) {
    this.view = view;
  }

  @Provides
  HomePresenter provideHomePresenter(HomePresenterImpl homePresenter) {
    return homePresenter;
  }

  @Provides
  HomePresenter.View provideView() {
    return view;
  }
}
