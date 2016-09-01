package com.growth.domain.temperature;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class TemperatureItem {
    private int id;
    private String temperature;
    private int sensor_id;
    private  String update_time;

    public int getId() {
        return id;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
