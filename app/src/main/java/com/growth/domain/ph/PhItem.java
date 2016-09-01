package com.growth.domain.ph;

/**
 * Created by SSL-D on 2016-07-21.
 */

public class PhItem {
    private int id;
    private String ph;
    private int sensor_id;

    public int getId() {
        return id;
    }

    public int getSensor_id() {
        return sensor_id;
    }

    public String getPh() {
        return ph;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSensor_id(int sensor_id) {
        this.sensor_id = sensor_id;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }
}
