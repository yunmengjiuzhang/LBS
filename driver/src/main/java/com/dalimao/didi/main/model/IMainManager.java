package com.dalimao.didi.main.model;

/**
 * 主页面业务逻辑层
 * Created by liuguangli on 17/3/15.
 *
 */

public interface IMainManager {
    /**
     *  获取附近司机
     */
    void getNearDrivers(double latitude, double longitude);

    /**
     *  更新我的位置
     */

    void updateMyLocation(String key, double latitude, double longitude, float rotation);

    /**
     *  呼叫附近司机
     */
    void callNearDrivers(String key, double startLatitude, double startLongitude, double endLatitude, double endLongitude, String startAddr, String endAddr, float cost);
}
