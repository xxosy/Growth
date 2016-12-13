package com.growth.monitor.adapter;

import com.growth.views.OnRecyclerItemClickListener;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface MonitorSensorListAdapterView {
  void refresh();

  void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener);
}
