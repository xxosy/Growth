package com.growth.domain.weather;

/**
 * Created by SSL-D on 2016-10-24.
 */

public class Sys {
  float message;
  String country;
  int sunrise;
  int sunset;

  public float getMessage() {
    return message;
  }

  public int getSunrise() {
    return sunrise;
  }

  public int getSunset() {
    return sunset;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setMessage(float message) {
    this.message = message;
  }

  public void setSunrise(int sunrise) {
    this.sunrise = sunrise;
  }

  public void setSunset(int sunset) {
    this.sunset = sunset;
  }
}
