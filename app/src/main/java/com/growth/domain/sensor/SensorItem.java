package com.growth.domain.sensor;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class SensorItem {
  private int id;
  private String name;
  private String serial;
  private String title;
  private String lat;
  private String lng;
  private String usercode;
  private String url;
  private String mosquito_url;
  private String actuator_serial;

  public String getActuator_serial() {
    return actuator_serial;
  }

  public String getMosquito_url() {
    return mosquito_url;
  }

  public void setMosquito_url(String mosquito_url) {
    this.mosquito_url = mosquito_url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsercode() {
    return usercode;
  }

  public void setUsercode(String usercode) {
    this.usercode = usercode;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSerial() {
    return serial;
  }

  public void setActuator_serial(String actuator_serial) {
    this.actuator_serial = actuator_serial;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }

  public String getLat() {
    return lat;
  }

  public String getLng() {
    return lng;
  }

  public String getTitle() {
    return title;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public void setLng(String lng) {
    this.lng = lng;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
