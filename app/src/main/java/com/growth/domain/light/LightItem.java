package com.growth.domain.light;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class LightItem {
  private int id;
  private String light;
  private int sensor_id;
  private String update_time;

  public int getId() {
    return id;
  }

  public int getSensor_id() {
    return sensor_id;
  }

  public String getUpdate_time() {
    return update_time;
  }

  public String getLight() {
    return light;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSensor_id(int sensor_id) {
    this.sensor_id = sensor_id;
  }

  public void setUpdate_time(String update_time) {
    this.update_time = update_time;
  }

  public void setLight(String light) {
    this.light = light;
  }
}
