package com.growth.domain.house;


import com.growth.domain.sensor.SensorItem;

import java.util.ArrayList;

/**
 * Created by SSL-D on 2016-08-04.
 */

public class HouseItem {
    String name;
    int id;
    ArrayList<SensorItem> sensorItems;

    public HouseItem(){
        sensorItems = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SensorItem> getSensorItems() {
        return sensorItems;
    }
    public void addSensorItem(SensorItem sensorItem) {
        this.sensorItems.add(sensorItem);
    }
    public void setSensorItems(ArrayList<SensorItem> sensorItems) {
        this.sensorItems = sensorItems;
    }
}
