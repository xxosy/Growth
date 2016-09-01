package com.growth.domain.house;

/**
 * Created by SSL-D on 2016-08-08.
 */

public class HouseSensor {
    int id;
    String serial;
    int house_id;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getId() {
        return id;
    }

    public int getHouse_id() {
        return house_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHouse_id(int house_id) {
        this.house_id = house_id;
    }
}
