package com.growth.domain;

/**
 * Created by SSL-D on 2016-07-29.
 */

public class Value {
    int id;
    String temperature;
    String humidity;
    String co2;
    String light;
    String ec;
    String ph;
    String update_time;
    String update_date;
    int sensor_id;

    public int getId() {
        return id;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public String getCo2() {
        return co2;
    }

    public String getEc() {
        return ec;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getLight() {
        return light;
    }

    public String getPh() {
        return ph;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

}
