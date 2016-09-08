package com.growth.domain.graph;

import android.util.Log;

/**
 * Created by SSL-D on 2016-08-02.
 */

public class GraphItem {
    private int id;
    private String temperature;
    private String humidity;
    private String ph;
    private String ec;
    private String co2;
    private String light;
    private String temperature2;
    private String value;
    private String soil_moisture;
    private int sensor_id;
    private  String update_time;

    public String getUpdate_time() {
        return update_time;
    }

    public int getId() {
        return id;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }


    public String getCo2() {
        return co2;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getLight() {
        return light;
    }

    public String getEc() {
        return ec;
    }

    public String getPh() {
        return ph;
    }

    public String getTemperature2() {
        return temperature2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
        this.value = co2;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
        this.value = humidity;
    }

    public void setEc(String ec) {
        this.ec = ec;
        this.value = ec;
    }

    public void setPh(String ph) {
        this.ph = ph;
        this.value = ph;
    }

    public void setTemperature2(String temperature2) {
        this.temperature2 = temperature2;
        this.value = temperature2;
    }

    public void setLight(String light) {
        this.light = light;
        Log.i("dasdadasda","sadasddasdsa");
        this.value = light;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
        Log.i("dasda","sadasd");
        this.value = temperature;
        Log.i("dasda",this.value);
    }

    public String getSoil_moisture() {
        return soil_moisture;
    }

    public void setSoil_moisture(String soil_moisture) {
        this.soil_moisture = soil_moisture;
    }

    public String getValue() {
        return value;
    }
}
