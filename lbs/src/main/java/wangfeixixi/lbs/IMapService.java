package wangfeixixi.lbs;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.model.LatLng;

/**
 * 地图的抽象接口
 * Created by liuguangli on 17/3/18.
 */

public interface IMapService {
    /**
     * 获取一个地图视图
     */
    View getMap();

    /**
     * 设置定位图标
     */
    void setLocationRes(int res);

    void addInfoWindowMarker(LocationInfo locationInfo, Bitmap bitmap);


    void setMarkerInfoWindowClickListener(OnInfoWindowMarkerListener listener);


    void updateMarkers(LocationMarker[] markers);

    /**
     * 移除标记
     */
    void removeMarker(String key);

    /**
     * 移除所有标记
     */

    void clearAllMarker();

    /**
     * 位置变化监听
     */
    void setLocationChangeListener(OnLocationListener listener);

    /**
     * 获取当前城市
     */
    String getCity();

    /**
     * 联动搜索附近的位置
     */
    void poiSearch(String key, OnSearchedListener listener);

    /**
     * 移动相机到点
     */

    void moveCamera(LocationInfo locationInfo, int scale);

    /**
     * 移动相机到范围
     */
    void moveCamera(LocationInfo locationInfo1, LocationInfo locationInfo2);

    void startOnceLocation();

    void setMySensor();

    void setPointToCenter(int x, int y);

//    void rotateEnable(boolean enable);

    void changeBearing(float bearing);

    void changeLatLng(LocationInfo locationInfo);

    void changeTilt(float tilt);

    /**
     * 驾车线路规划
     */

    void driverRoute(LocationInfo start, LocationInfo end, int color, OnRouteListener listener);

    float calculateLineDistance(LocationInfo start, LocationInfo end);

    /**
     * 生命周期函数定义
     */

    void onCreate(Bundle savedState);

    void onSaveInstanceState(Bundle outState);

    void onResume();

    void onPause();

    void onDestroy();


}
