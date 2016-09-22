package com.growth.domain.actuator;

/**
 * Created by SSL-D on 2016-09-20.
 */

public class ActuatorState {
    int id;
    String state;
    String serial;

    public int getId() {
        return id;
    }

    public String getSerial() {
        return serial;
    }

    public String getState() {
        return state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setState(String state) {
        this.state = state;
    }
}
