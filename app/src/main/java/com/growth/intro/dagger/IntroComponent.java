package com.growth.intro.dagger;

import com.growth.intro.view.IntroActivity;

import dagger.Component;

/**
 * Created by SSL-D on 2016-08-31.
 */
@Component(modules = IntroModule.class)
public interface IntroComponent {
    void inject(IntroActivity introActivity);
}
