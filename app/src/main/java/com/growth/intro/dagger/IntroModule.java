package com.growth.intro.dagger;

import com.growth.domain.database.DBManager;
import com.growth.intro.presenter.IntroPresenter;
import com.growth.intro.presenter.IntroPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-08-31.
 */
@Module(includes = NetworkModule.class)
public class IntroModule {
    IntroPresenter.View view;

    public IntroModule(IntroPresenter.View view){
        this.view = view;
    }

    @Provides
    IntroPresenter providePresenter(IntroPresenterImpl introPresenter){
        return introPresenter;
    }
    @Provides
    IntroPresenter.View provideView(){
        return view;
    }
}
