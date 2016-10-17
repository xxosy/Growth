package com.growth.home.dagger;

import com.growth.home.view.HomeActivity;

import dagger.Component;

/**
 * Created by SSL-D on 2016-07-20.
 */
@Component(modules = HomeModule.class)
public interface HomeComponent {
  void inject(HomeActivity homeActivity);
}
