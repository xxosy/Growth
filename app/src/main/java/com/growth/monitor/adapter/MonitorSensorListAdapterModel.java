package com.growth.monitor.adapter;

import com.growth.domain.sensor.SensorItem;

/**
 * Created by SSL-D on 2016-10-06.
 */

public interface MonitorSensorListAdapterModel {
  void add(SensorItem sensorItem);

  int getSize();

  SensorItem getItem(int position);

  void clear();
}
