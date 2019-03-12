package wangfeixixi.lbs;


import com.amap.api.maps.model.animation.Animation;

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
    public int localtionType;
    public float accuracy;
    public String provider;
    public float speed;
    public float bearing;
    public int satellites;
    public String country;
    public String province;
    public String city;
    public String cityCode;
    public String district;
    public String adCode;
    public String poiName;
    public long time;
    public int errorCode;
    public String errorInfo;
    public String locationDetail;
    public boolean wifiEnable;
    public int gpsStatus;
    public int gpsSatelites;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nkey:" + key);
        sb.append("\nname:" + name);
        sb.append("\naddress:" + address);
        sb.append("\nlatitude:" + latitude);
        sb.append("\nlongitude:" + longitude);
        sb.append("\nrotation:" + rotation);
        sb.append("\nlocaltionType:" + localtionType);
        sb.append("\naccuracy:" + accuracy);
        sb.append("\nprovider:" + provider);
        sb.append("\nspeed:" + speed);
        sb.append("\nbearing:" + bearing);
        sb.append("\nsatellites:" + satellites);
        sb.append("\ncountry:" + country);
        sb.append("\nprovince:" + province);
        sb.append("\ncity:" + city);
        sb.append("\ncityCode:" + cityCode);
        sb.append("\ndistrict:" + district);
        sb.append("\nadCode:" + adCode);
        sb.append("\npoiName:" + poiName);
        sb.append("\ntime:" + time);
        sb.append("\nerrorCode:" + errorCode);
        sb.append("\nerrorInfo:" + errorInfo);
        sb.append("\nlocationDetail:" + locationDetail);
        return sb.toString();
    }

//                      sb.append("定位成功" + "\n");
//                    sb.append("定位类型: " + location.getLocationType() + "\n");
//                    sb.append("经    度    : " + location.getLongitude() + "\n");
//                    sb.append("纬    度    : " + location.getLatitude() + "\n");
//                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//                    sb.append("提供者    : " + location.getProvider() + "\n");
//
//                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                    sb.append("角    度    : " + location.getBearing() + "\n");
//                    // 获取当前提供定位服务的卫星个数
//                    sb.append("星    数    : " + location.getSatellites() + "\n");
//                    sb.append("国    家    : " + location.getCountry() + "\n");
//                    sb.append("省            : " + location.getProvince() + "\n");
//                    sb.append("市            : " + location.getCity() + "\n");
//                    sb.append("城市编码 : " + location.getCityCode() + "\n");
//                    sb.append("区            : " + location.getDistrict() + "\n");
//                    sb.append("区域 码   : " + location.getAdCode() + "\n");
//                    sb.append("地    址    : " + location.getAddress() + "\n");
//                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
//                    //定位完成的时间
//                    sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
//                } else {
//                    //定位失败
//                    sb.append("定位失败" + "\n");
//                    sb.append("错误码:" + location.getErrorCode() + "\n");
//                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
//                }


    public float rotation = 0;

    public Animation animation;

    public LocationInfo() {

    }

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
        this.key = key;
        this.rotation = rotation;
    }

    public LocationInfo(String key, double latitude, double longitude, Animation animation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
        this.animation = animation;
    }
}
