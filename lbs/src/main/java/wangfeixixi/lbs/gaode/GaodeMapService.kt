package wangfeixixi.lbs.gaode

import android.R.attr.rotation
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.enums.AimLessMode
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.route.*
import wangfeixixi.lbs.*
import java.util.*

/**
 * 高德地图的实现
 */
class GaodeMapService(context: Context) : BaseMapService(context) {

    private val TAG = "GaodeMapService"

    //位置定位对象
    var mlocationClient: AMapLocationClient? = null
    // 地图视图对象
    private val mapView: MapView
    var myLocationStyle: MyLocationStyle? = null
    // 地图管理对象
    private val aMap: AMap
    // 地图位置变化回调对象
    private var mLocationChangeListener: LocationSource.OnLocationChangedListener? = null
    private var firstLocation = true
    // 管理地图标记集合
    private var mMarkersHashMap: MutableMap<String, Marker> = HashMap()
    // 异步路径规划驾车模式查询
    private val mRouteSearch: RouteSearch by lazy {
        RouteSearch(mCtx)
    }
    lateinit var locationOption: AMapLocationClientOption

    init {
        // 创建地图对象
        mapView = MapView(context)
        // 获取地图管理器
        aMap = mapView.map
        // 创建定位对象
        mlocationClient = AMapLocationClient(context)
        locationOption = AMapLocationClientOption()
        //设置为高精度定位模式
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位参数
//        mlocationClient?.setLocationOption(locationOption)
        //获取一次定位结果：
//该方法默认为false。
        locationOption.setOnceLocation(true);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        locationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mlocationClient?.setLocationOption(locationOption);

    }

    override fun setLocationRes(res: Int) {
        myLocationStyle = MyLocationStyle()
        myLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle!!.myLocationIcon(BitmapDescriptorFactory.fromResource(res))// 设置小蓝点的图标
        myLocationStyle!!.strokeColor(Color.BLACK)// 设置圆形的边框颜色
        myLocationStyle!!.radiusFillColor(Color.TRANSPARENT)// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle!!.strokeWidth(1.0f)// 设置圆形的边框粗细
    }

    fun setUpLocation() {
        //设置监听器
        mlocationClient?.setLocationListener { aMapLocation ->
            // 定位变化位置
            mLocationChangeListener?.let {
                it.onLocationChanged(aMapLocation)
                Log.d(TAG, "onLocationChanged")
                if (firstLocation) {
                    firstLocation = false
                    aMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
                }
                mCity = aMapLocation.city
                mLocationListener?.onLocationChange(LocationInfo(aMapLocation.poiName, aMapLocation.address, aMapLocation.latitude, aMapLocation.longitude))
            }


            if (mLocationChangeListener != null) {
                // 地图已经激活，通知蓝点实时更新
                mLocationChangeListener!!.onLocationChanged(aMapLocation)// 显示系统小蓝点
                Log.d(TAG, "onLocationChanged")
                if (firstLocation) {
                    firstLocation = false
                    aMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
                }

                mCity = aMapLocation.city
                mLocationListener?.onLocationChange(LocationInfo(aMapLocation.poiName, aMapLocation.address, aMapLocation.latitude, aMapLocation.longitude))
            }
        }

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient?.startLocation()
    }

    private fun setUpMap() {
        if (myLocationStyle != null) {
            aMap.myLocationStyle = myLocationStyle
        }
        // 设置地图激活（加载监听）
        aMap.setLocationSource(object : LocationSource {
            override fun activate(onLocationChangedListener: LocationSource.OnLocationChangedListener) {
                mLocationChangeListener = onLocationChangedListener
                Log.d(TAG, "activate")
            }

            override fun deactivate() {
                mlocationClient?.stopLocation()
                mlocationClient?.onDestroy()
                mlocationClient = null
            }
        })
        // 设置默认定位按钮是否显示，这里先不想业务使用方开放
        aMap.uiSettings.isMyLocationButtonEnabled = false
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false，这里先不想业务使用方开放
        aMap.isMyLocationEnabled = true
    }

    override fun getMap(): View {
        return mapView
    }

    //启动定位
    override fun startOnceLocation() {
        mlocationClient?.startLocation();
    }

    /**
     * CAMERA_DETECTED, 巡航时返回电子眼信息。
     * SPECIALROAD_DETECTED, 巡航时返回特殊道路设施信息。
     * CAMERA_AND_SPECIALROAD_DETECTED，巡航时返回电子眼和特殊道路设施信息。
     * updateAimlessModeStatistics
     */
    fun startAimlessMode(context: Context, listener: AMapNaviListener) {
        val navi = AMapNavi.getInstance(context);
        navi.startAimlessMode(AimLessMode.CAMERA_AND_SPECIALROAD_DETECTED);
        navi.setAMapNaviListener(listener)
    }

    override fun addMyLocationMarker(locationInfo: LocationInfo, bitmap: Bitmap) {
        addOrUpdateMarker(locationInfo, bitmap)
        KEY_MY_MARKERE = locationInfo.key
    }

    override fun addOrUpdateMarker(locationInfo: LocationInfo, bitmap: Bitmap) {
        val latLng = LatLng(locationInfo.latitude, locationInfo.longitude)
        // 如果已经存在则更新角度、位置   // 如果不存在则创建
        val storedMarker = mMarkersHashMap[locationInfo.key]
        if (storedMarker != null) {
            storedMarker.position = latLng
            storedMarker.rotateAngle = locationInfo.rotation
        } else {
            val options = MarkerOptions()
            options.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            options.anchor(0.5f, 0.5f)
            options.position(latLng)
            val marker = aMap.addMarker(options)
            marker.rotateAngle = rotation.toFloat()
            mMarkersHashMap.put(locationInfo.key, marker)
        }
    }

    override fun removeMarker(key: String) {
        mMarkersHashMap[key]?.remove()
    }

    override fun poiSearch(key: String, listener: OnSearchedListener) {
        if (!TextUtils.isEmpty(key)) {
            val inputquery = InputtipsQuery(key, "")
            val inputTips = Inputtips(mCtx, inputquery)
            inputTips.setInputtipsListener { tipList, rCode ->
                when (rCode) {
                    AMapException.CODE_AMAP_SUCCESS -> {
                        val locationInfos = ArrayList<LocationInfo>()
                        for (tip in tipList)
                            locationInfos.add(LocationInfo(tip.name, tip.address, tip.point.latitude, tip.point.longitude))
                        listener.onSearched(locationInfos)
                    }
                    else -> listener.onError(rCode)
                }
            }
            inputTips.requestInputtipsAsyn()
        }
    }

    override fun moveCamera(locationInfo: LocationInfo, scale: Int) {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationInfo.latitude, locationInfo.longitude), scale.toFloat()))
    }

    override fun moveCamera(first: LocationInfo, end: LocationInfo) {
        val latLngFirst = LatLng(first.latitude, first.longitude)
        val latLngEnd = LatLng(end.latitude, end.longitude)
        val latLngBounds = LatLngBounds(latLngFirst, latLngEnd)
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20))
    }

    // 驾车路径规划
    override fun driverRoute(start: LocationInfo, end: LocationInfo, color: Int, onRouteListener: OnRouteListener?) {
        val fromAndTo = RouteSearch.FromAndTo(LatLonPoint(start.latitude, start.longitude), LatLonPoint(end.latitude, end.longitude))
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        val query = RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "")
        mRouteSearch.calculateDriveRouteAsyn(query)
        mRouteSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onBusRouteSearched(busRouteResult: BusRouteResult, i: Int) {

            }

            override fun onDriveRouteSearched(driveRouteResult: DriveRouteResult, i: Int) {
                // 获取第一条路径
                val driveRoute = driveRouteResult.paths[0]
                val routeOptions = PolylineOptions()
                // 路径起点
                val startPoint = driveRouteResult.startPos
                // 路径中间步骤
                val routeSteps = driveRoute.steps
                // 路径终点
                val endPoint = driveRouteResult.targetPos
                // 绘制路径
                routeOptions.add(LatLng(startPoint.latitude, startPoint.longitude))
                for (step in routeSteps)
                    for (latLonPoint in step.polyline)
                        routeOptions.add(LatLng(latLonPoint.latitude, latLonPoint.longitude))
                routeOptions.color(color)
                routeOptions.add(LatLng(endPoint.latitude, endPoint.longitude))
                aMap.addPolyline(routeOptions)
                onRouteListener?.let {
                    val info = RouteInfo()
                    info.taxiCost = driveRouteResult.taxiCost
                    info.duration = 10 + (driveRoute.duration / 1000 * 60).toInt()
                    info.distance = 0.5f + driveRoute.distance / 1000
                    it.onComplete(info)
                }
            }

            override fun onWalkRouteSearched(walkRouteResult: WalkRouteResult, i: Int) {

            }

            override fun onRideRouteSearched(rideRouteResult: RideRouteResult, i: Int) {

            }
        })
    }

    override fun calculateLineDistance(start: LocationInfo, end: LocationInfo): Float {
        return AMapUtils.calculateLineDistance(LatLng(start.latitude, start.longitude), LatLng(end.latitude, end.longitude))
    }


    override fun clearAllMarker() {
        aMap.clear()
        mMarkersHashMap.clear()
    }

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        mapView.onCreate(savedState)
        setUpMap()
    }

    override fun setMySensor() {
        val mSensor = SensorEventHelper(mCtx)
        mSensor.registerSensorListener(object : LbsSensorListner(mCtx) {

            override fun onSensorLisner(v: Float) {
                mMarkersHashMap[KEY_MY_MARKERE]?.rotateAngle = v
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        setUpLocation()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        mlocationClient?.stopLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        mlocationClient?.onDestroy()
    }
}
