package com.growth.domain.humidity;

import com.growth.utils.AESDecryptor;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class HumidityItem {
  private int id;
  private String humidity;
  private int sensor_id;
  private String update_time;
  public void decrypt(){
    try {
      humidity = AESDecryptor.decrypt("pais",humidity);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public int getId() {
    return id;
  }

  public int getSensor_id() {
    return sensor_id;
  }

  public String getHumidity() {
    return humidity;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setSensor_id(int sensor_id) {
    this.sensor_id = sensor_id;
  }

  public void setHumidity(String humidity) {
    this.humidity = humidity;
  }

  public String getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(String update_time) {
    this.update_time = update_time;
  }
}
