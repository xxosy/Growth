package com.growth.standard.dagger;

import com.growth.network.dagger.NetworkModule;
import com.growth.standard.adapter.StandardInformationAdapterModel;
import com.growth.standard.adapter.StandardInformationAdapterView;
import com.growth.standard.adapter.StandardInformationRecyclerAdapter;
import com.growth.standard.presenter.StandardInformationPresenter;
import com.growth.standard.presenter.StandardInformationPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-11-21.
 */
@Module(includes = NetworkModule.class)
public class StandardInformationModule {
  StandardInformationPresenter.View view;
  StandardInformationRecyclerAdapter adapter;

  public StandardInformationModule(StandardInformationPresenter.View view, StandardInformationRecyclerAdapter adapter){
    this.view = view;
    this.adapter = adapter;
  }

  @Provides
  public StandardInformationPresenter.View provideView(){
    return view;
  }

  @Provides
  public StandardInformationPresenter providePresenter(StandardInformationPresenterImpl standardInformationPresenter){
    return standardInformationPresenter;
  }

  @Provides
  public StandardInformationAdapterModel provideStandardInformationAdapterModel(){
    return adapter;
  }

  @Provides
  public StandardInformationAdapterView provideStandardInformationAdapterView(){
    return adapter;
  }
}
