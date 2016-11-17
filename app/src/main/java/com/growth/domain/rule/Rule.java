package com.growth.domain.rule;

/**
 * Created by SSL-D on 2016-11-01.
 */

public class Rule {
  int id;
  String sensor_serial;
  String activation;
  String factor;
  String value;
  String condition;
  String actuator_serial;
  String port;
  String action;

  public int getId() {
    return id;
  }

  public String getActivation() {
    return activation;
  }

  public String getActuator_serial() {
    return actuator_serial;
  }

  public String getCondition() {
    return condition;
  }

  public String getFactor() {
    return factor;
  }

  public String getPort() {
    return port;
  }

  public String getSensor_serial() {
    return sensor_serial;
  }

  public String getValue() {
    return value;
  }

  public String getAction() {
    return action;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setActivation(String activation) {
    this.activation = activation;
  }

  public void setActuator_serial(String actuator_serial) {
    this.actuator_serial = actuator_serial;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public void setFactor(String factor) {
    this.factor = factor;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public void setSensor_serial(String sensor_serial) {
    this.sensor_serial = sensor_serial;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
