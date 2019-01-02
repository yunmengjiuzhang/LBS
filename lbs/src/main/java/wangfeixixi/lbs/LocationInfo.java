package wangfeixixi.lbs;


import java.io.Serializable;

/**
 * 位置点
 */
public class LocationInfo implements Serializable {


    public String key;
    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public float rotation = 0;

    public LocationInfo(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        key = String.valueOf(latitude) + String.valueOf(longitude);
    }

    public LocationInfo(String key, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
    }

    public LocationInfo(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        key = String.valueOf(latitude) + String.valueOf(longitude);
    }

    public LocationInfo(String name, String address, double latitude, double longitude, float rotation) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        key = String.valueOf(latitude) + String.valueOf(longitude);
        this.rotation = rotation;
    }

    public LocationInfo(String key, double latitude, double longitude, float rotation) {
        this.latitude = latitude;
        this.longitude = longitude;
        key = key;
        this.rotation = rotation;
    }
}
