package com.growth.domain;

/**
 * Created by SSL-D on 2016-08-24.
 */

public class UpdateSensorData {
    String lat;
    String lng;
    String title;

    public UpdateSensorData(String lat, String lng, String title){
        this.title = title;
        this.lat =lat;
        this.lng = lng;
    }
}
