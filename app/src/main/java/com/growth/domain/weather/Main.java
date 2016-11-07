package com.growth.domain.weather;

/**
 * Created by SSL-D on 2016-10-24.
 */

public class Main {
  float temp;
  float pressure;
  float humidity;
  float temp_min;
  float temp_max;
  float sea_level;
  float grnd_level;

  public float getGrnd_level() {
    return grnd_level;
  }

  public float getHumidity() {
    return humidity;
  }

  public float getPressure() {
    return pressure;
  }

  public float getSea_level() {
    return sea_level;
  }

  public float getTemp() {
    return temp;
  }

  public float getTemp_max() {
    return temp_max;
  }

  public float getTemp_min() {
    return temp_min;
  }

  public void setGrnd_level(float grnd_level) {
    this.grnd_level = grnd_level;
  }

  public void setHumidity(float humidity) {
    this.humidity = humidity;
  }

  public void setPressure(float pressure) {
    this.pressure = pressure;
  }

  public void setSea_level(float sea_level) {
    this.sea_level = sea_level;
  }

  public void setTemp(float temp) {
    this.temp = temp;
  }

  public void setTemp_max(float temp_max) {
    this.temp_max = temp_max;
  }

  public void setTemp_min(float temp_min) {
    this.temp_min = temp_min;
  }
}
