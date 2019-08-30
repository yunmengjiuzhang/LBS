package wangfeixixi.lbs.gaode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wangfeixixi.lbs.BaseMapService;
import wangfeixixi.lbs.LbsSensorListner;
import wangfeixixi.lbs.LocationInfo;
import wangfeixixi.lbs.LocationMarker;
import wangfeixixi.lbs.OnInfoWindowMarkerListener;
import wangfeixixi.lbs.OnRouteListener;
import wangfeixixi.lbs.OnSearchedListener;
import wangfeixixi.lbs.RouteInfo;
import wangfeixixi.lbs.SensorEventHelper;

public class GaodeService extends BaseMapService {

    private String TAG = "GaodeMapService";

    //位置定位对象
    private AMapLocationClient mlocationClient;
    // 地图视图对象
    private MapView mapView;
    private MyLocationStyle myLocationStyle;
    // 地图管理对象
    private AMap aMap;
    // 地图位置变化回调对象
    private LocationSource.OnLocationChangedListener mLocationChangeListener;
    private boolean firstLocation = true;
    // 管理地图标记集合
    private HashMap<String, SmoothMoveMarker> mMarkersHashMap = new HashMap<String, SmoothMoveMarker>();
    // 异步路径规划驾车模式查询
    private RouteSearch mRouteSearch = new RouteSearch(mCtx);
    private AMapLocationClientOption locationOption;

    public GaodeService(Context context) {
        super(context);
        // 创建地图对象
        mapView = new MapView(mCtx);
        // 获取地图管理器
        aMap = mapView.getMap();
    }


    public void initLocation(Context context) {
        // 创建定位对象
        mlocationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        //设置为高精度定位模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        locationOption.setInterval(1000);
        //设置定位参数
//        mlocationClient?.setLocationOption(locationOption)
        //获取一次定位结果：
//该方法默认为false。
//        locationOption.setOnceLocation(true);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        locationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        if (mlocationClient != null) {
            mlocationClient.setLocationOption(locationOption);
            mlocationClient.startLocation();
        }
    }

    @Override
    public View getMap() {
        return mapView;
    }

    @Override
    public void setLocationRes(int res) {
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(res));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
    }

    @Override
    public void updateMarkers(LocationMarker... markers) {
        for (LocationMarker marker : markers)
            updateMakerOneTimeSmooth(marker);

//        计算无用marker
//        ArrayList<String> keys_be_destry = new ArrayList<>();
//
//        for (Map.Entry<String, SmoothMoveMarker> entry : mMarkersHashMap.entrySet()) {
//            String key = entry.getKey();
//            for (LocationMarker marker : markers)
//                if (!key.equals(marker.key)) {
//                    keys_be_destry.add(key);
//                }
//        }
//
////        移除没有信息的marker
//        for (String key : keys_be_destry)
//            removeMarker(key);
    }

    private void updateMakerOneTimeSmooth(LocationMarker marker) {
        List<LatLng> points = new ArrayList<>();
        LatLng endLatLng = new LatLng(marker.latitude, marker.longitude);

        SmoothMoveMarker smoothMarker = mMarkersHashMap.get(marker.key);
        if (smoothMarker != null) {
            LatLng startLatLng = smoothMarker.getPosition();
            points.add(startLatLng);
            points.add(endLatLng);
        } else {
            points.add(endLatLng);
            points.add(endLatLng);
            smoothMarker = new SmoothMoveMarker(aMap);
            // 设置滑动的图标
            smoothMarker.setDescriptor(BitmapDescriptorFactory.fromBitmap(marker.icon));
            mMarkersHashMap.put(marker.key, smoothMarker);
        }
        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(points);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(marker.animTime);
        // 开始滑动
        smoothMarker.startSmoothMove();


    }

    @Override
    public void removeMarker(String key) {
        if (mMarkersHashMap.get(key) != null)
            mMarkersHashMap.get(key).destroy();
    }

    @Override
    public void addInfoWindowMarker(LocationInfo locationInfo, Bitmap bitmap) {
        LatLng latLng = new LatLng(locationInfo.latitude, locationInfo.longitude);
//        // 如果已经存在则更新角度、位置   // 如果不存在则创建
//        Marker marker = mMarkersHashMap.get(locationInfo.key);
//        if (marker != null) {
//            marker.setPosition(latLng);
//            marker.setTitle(locationInfo.name);
//            marker.setSnippet(locationInfo.address);
//            marker.setRotateAngle(locationInfo.rotation);
//        } else {
//
//            mMarkersHashMap.put(locationInfo.key, marker);
//            if (locationInfo.animation != null) {
//                marker.setAnimation(locationInfo.animation);
//                marker.startAnimation();
//            }
//        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 0.5f);
        options.position(latLng);
        options.title(locationInfo.name);
        Marker marker = aMap.addMarker(options);
        marker.setTitle(locationInfo.name);
        marker.setSnippet(locationInfo.address);
        marker.setRotateAngle(0);
        marker.showInfoWindow();
    }

    @Override
    public void setMarkerInfoWindowClickListener(final OnInfoWindowMarkerListener listener) {
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                listener.onClick(marker.getTitle(), marker.getSnippet());
            }
        });
    }


    @Override
    public void clearAllMarker() {
        aMap.clear();
        mMarkersHashMap.clear();
    }

    @Override
    public void poiSearch(String key, final OnSearchedListener listener) {
        if (!TextUtils.isEmpty(key)) {
            InputtipsQuery inputquery = new InputtipsQuery(key, "");
            final Inputtips inputTips = new Inputtips(mCtx, inputquery);
            inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
                @Override
                public void onGetInputtips(List<Tip> tipList, int rCode) {
                    switch (rCode) {
                        case AMapException.CODE_AMAP_SUCCESS:
                            ArrayList<LocationInfo> locationInfos = new ArrayList<LocationInfo>();
                            for (Tip tip : tipList)
                                locationInfos.add(new LocationInfo(tip.getName(), tip.getAddress(), tip.getPoint().getLatitude(), tip.getPoint().getLongitude()));
                            listener.onSearched(locationInfos);
                            break;
                        default:
                            listener.onError(rCode);
                            break;
                    }
                }
            });
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void moveCamera(LocationInfo locationInfo, int scale) {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationInfo.latitude, locationInfo.longitude), scale));
    }

    @Override
    public void moveCamera(LocationInfo first, LocationInfo end) {
        LatLng latLngFirst = new LatLng(first.latitude, first.longitude);
        LatLng latLngEnd = new LatLng(end.latitude, end.longitude);
        LatLngBounds latLngBounds = new LatLngBounds(latLngFirst, latLngEnd);
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20));
    }

    @Override
    public void startOnceLocation() {
        if (mlocationClient != null)
            mlocationClient.startLocation();
    }

    @Override
    public void setMySensor() {
        SensorEventHelper mSensor = new SensorEventHelper(mCtx);
        mSensor.registerSensorListener(new LbsSensorListner(mCtx) {
            @Override
            public void onSensorLisner(float v) {
                SmoothMoveMarker marker = mMarkersHashMap.get(KEY_MY_MARKERE);
                if (marker != null)
                    marker.setRotate(v);
                mMarkersHashMap.put(KEY_MY_MARKERE, marker);
            }
        });
    }

    @Override
    public void setPointToCenter(int x, int y) {
        aMap.setPointToCenter(x, y);
    }

    @Override
    public void changeBearing(float bearing) {
        aMap.animateCamera(CameraUpdateFactory.changeBearing(bearing));
    }

    @Override
    public void changeLatLng(LocationInfo locationInfo) {
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationInfo.latitude, locationInfo.longitude)));
    }

    @Override
    public void changeTilt(float tilt) {
        aMap.animateCamera(CameraUpdateFactory.changeTilt(tilt));
    }

    @Override
    public void driverRoute(LocationInfo start, LocationInfo end, final int color, final OnRouteListener
            onRouteListener) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(start.latitude, start.longitude), new LatLonPoint(end.latitude, end.longitude));
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int color) {
                // 获取第一条路径
                DrivePath driveRoute = driveRouteResult.getPaths().get(0);
                PolylineOptions routeOptions = new PolylineOptions();
                // 路径起点
                LatLonPoint startPoint = driveRouteResult.getStartPos();
                // 路径中间步骤
                List<DriveStep> routeSteps = driveRoute.getSteps();
                // 路径终点
                LatLonPoint endPoint = driveRouteResult.getTargetPos();
                // 绘制路径
                routeOptions.add(new LatLng(startPoint.getLatitude(), startPoint.getLongitude()));
                for (DriveStep step : routeSteps)
                    for (LatLonPoint latLonPoint : step.getPolyline())
                        routeOptions.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                routeOptions.color(color);
                routeOptions.add(new LatLng(endPoint.getLatitude(), endPoint.getLongitude()));
                aMap.addPolyline(routeOptions);
                if (onRouteListener != null) {
                    RouteInfo info = new RouteInfo();
                    info.taxiCost = driveRouteResult.getTaxiCost();
                    info.duration = (int) (10 + (driveRoute.getDuration() / 1000 * 60));
                    info.distance = 0.5f + driveRoute.getDistance() / 1000;
                    onRouteListener.onComplete(info);
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
    }

    private void setUpLocation() {
        //设置监听器
        if (mlocationClient != null)
            mlocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (mLocationChangeListener != null) {
                        mLocationChangeListener.onLocationChanged(aMapLocation);
                        Log.d(TAG, "onLocationChanged");
                        if (firstLocation) {
                            firstLocation = false;
                            aMap.animateCamera(CameraUpdateFactory.zoomTo(18f));
                        }
                        mCity = aMapLocation.getCity();
                        if (mLocationListener != null)
                            mLocationListener.onLocationChange(new LocationInfo(aMapLocation.getPoiName(), aMapLocation.getAddress(), aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    }
                    if (mLocationChangeListener != null) {
                        // 地图已经激活，通知蓝点实时更新
                        mLocationChangeListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                        Log.d(TAG, "onLocationChanged");
                        if (firstLocation) {
                            firstLocation = false;
                            aMap.animateCamera(CameraUpdateFactory.zoomTo(18f));
                        }
                        mCity = aMapLocation.getCity();
                        if (mLocationListener != null)
                            mLocationListener.onLocationChange(new LocationInfo(aMapLocation.getPoiName(), aMapLocation.getAddress(), aMapLocation.getLatitude(), aMapLocation.getLongitude()));
                    }


                }
            });
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        if (mlocationClient != null)
            mlocationClient.startLocation();
    }

    @Override
    public float calculateLineDistance(LocationInfo start, LocationInfo end) {
        return AMapUtils.calculateLineDistance(new LatLng(start.latitude, start.longitude), new LatLng(end.latitude, end.longitude));
    }

    private void setUpMap() {
        if (myLocationStyle != null) {
            aMap.setMyLocationStyle(myLocationStyle);
        }
        // 设置地图激活（加载监听）
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mLocationChangeListener = onLocationChangedListener;
                Log.d(TAG, "activate");
            }

            @Override
            public void deactivate() {
                if (mlocationClient != null) {
                    mlocationClient.stopLocation();
                    mlocationClient.onDestroy();
                    mlocationClient = null;
                }
            }
        });
        // 设置默认定位按钮是否显示，这里先不想业务使用方开放
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
        aMap.setMyLocationEnabled(false);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false，这里先不想业务使用方开放
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        mapView.onCreate(savedState);
        setUpMap();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        setUpLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        if (mlocationClient != null)
            mlocationClient.stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mlocationClient != null)
            mlocationClient.onDestroy();
    }
}
