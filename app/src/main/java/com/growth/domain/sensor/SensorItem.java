package com.growth.domain.sensor;

import dagger.Provides;

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
