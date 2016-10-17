package com.growth.domain.co2;

/**
 * Created by SSL-D on 2016-08-02.
 */

public class Co2Item {
  private int id;
  private String co2;
  private int sensor_id;
  private String update_time;

  public int getId() {
    return id;
  }

  public int getSensor_id() {
    return sensor_id;
  }

  public String getCo2() {
    return co2;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSensor_id(int sensor_id) {
    this.sensor_id = sensor_id;
  }

  public void setCo2(String co2) {
    this.co2 = co2;
  }

  public String getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(String update_time) {
    this.update_time = update_time;
  }
}
