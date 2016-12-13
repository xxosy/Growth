package com.growth.monitor.dagger;

import com.growth.monitor.adapter.MonitorSensorListAdapter;
import com.growth.monitor.adapter.MonitorSensorListAdapterModel;
import com.growth.monitor.adapter.MonitorSensorListAdapterView;
import com.growth.monitor.presenter.MonitorPresenter;
import com.growth.monitor.presenter.MonitorPresenterImpl;
import com.growth.network.dagger.NetworkModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SSL-D on 2016-10-06.
 */
@Module(includes = NetworkModule.class)
public class MonitorModule {
  MonitorPresenter.View view;
  MonitorSensorListAdapter mMonitorSensorListAdapter;

  public MonitorModule(MonitorPresenter.View view, MonitorSensorListAdapter MonitorSensorListAdapter) {
    this.view = view;
    mMonitorSensorListAdapter = MonitorSensorListAdapter;
  }

  @Provides
  public MonitorPresenter providePresenter(MonitorPresenterImpl MonitorPresenter) {
    return MonitorPresenter;
  }

  @Provides
  public MonitorPresenter.View provideView() {
    return this.view;
  }

  @Provides
  public MonitorSensorListAdapterModel provideMonitorSensorListAdapterModel() {
    return mMonitorSensorListAdapter;
  }

  @Provides
  public MonitorSensorListAdapterView provideMonitorSensorListAdapterView() {
    return mMonitorSensorListAdapter;
  }
}
