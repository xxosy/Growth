package com.growth.standard.dagger;

import com.growth.standard.view.StandardInformationFragment;

import dagger.Component;

/**
 * Created by SSL-D on 2016-11-21.
 */
@Component(modules = StandardInformationModule.class)
public interface StandardInformationComponent {
  void inject(StandardInformationFragment standardInformationFragment);
}
