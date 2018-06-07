package com.dalimao.didi.main.model.bean;

/**
 *
 * 位置
 * Created by liuguangli on 17/3/15.
 */

public class Driver {
    private double latitude;
    private double longitude;
    private float rotation;
    private String key;
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRotation() {
        return rotation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
