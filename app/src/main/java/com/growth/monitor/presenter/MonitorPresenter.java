package com.growth.monitor.presenter;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface MonitorPresenter {
  void OnCreatedView();

  void OnRecyclerMonitorItemClick(int position);

  interface View {
    void refreshRecyclerMonitorList();

    void startActivityPlantGallery(String serial);
  }
}
