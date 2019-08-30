//package wangfeixixi.lbs.gaode;
//
//import android.util.Log;
//
//import com.amap.api.navi.AMapNaviListener;
//import com.amap.api.navi.model.AMapCongestionLink;
//import com.amap.api.navi.model.AMapLaneInfo;
//import com.amap.api.navi.model.AMapNaviCameraInfo;
//import com.amap.api.navi.model.AMapNaviCross;
//import com.amap.api.navi.model.AMapNaviInfo;
//import com.amap.api.navi.model.AMapNaviLocation;
//import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
//import com.amap.api.navi.model.AMapServiceAreaInfo;
//import com.amap.api.navi.model.AimLessModeCongestionInfo;
//import com.amap.api.navi.model.AimLessModeStat;
//import com.amap.api.navi.model.NaviInfo;
//import com.amap.api.navi.model.NaviLatLng;
//import com.autonavi.tbt.TrafficFacilityInfo;
//
//public class NaviListener implements AMapNaviListener {
//
//    public String TAG = "navilistener";
//
//    @Override
//    public void onInitNaviFailure() {
//
//    }
//
//    @Override
//    public void onInitNaviSuccess() {
//
//    }
//
//    @Override
//    public void onStartNavi(int i) {
//
//    }
//
//    @Override
//    public void onTrafficStatusUpdate() {
//
//    }
//
//    @Override
//    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
//
//    }
//
//    @Override
//    public void onGetNavigationText(int i, String s) {
//
//    }
//
//    @Override
//    public void onEndEmulatorNavi() {
//
//    }
//
//    @Override
//    public void onArriveDestination() {
//
//    }
//
//    @Override
//    public void onCalculateRouteSuccess() {
//
//    }
//
//    @Override
//    public void onCalculateRouteFailure(int i) {
//
//    }
//
//    @Override
//    public void onReCalculateRouteForYaw() {
//
//    }
//
//    @Override
//    public void onReCalculateRouteForTrafficJam() {
//
//    }
//
//    @Override
//    public void onArrivedWayPoint(int i) {
//
//    }
//
//    @Override
//    public void onGpsOpenStatus(boolean b) {
//
//    }
//
//    @Override
//    public void onNaviInfoUpdate(NaviInfo naviInfo) {
//
//    }
//
//    @Override
//    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
//
//    }
//
//    @Override
//    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
//
//    }
//
//    @Override
//    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
//
//    }
//
//    @Override
//    public void showCross(AMapNaviCross aMapNaviCross) {
//
//    }
//
//    @Override
//    public void hideCross() {
//
//    }
//
//    @Override
//    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
//
//    }
//
//    @Override
//    public void hideLaneInfo() {
//
//    }
//
//    @Override
//    public void onCalculateMultipleRoutesSuccess(int[] ints) {
//
//    }
//
//    @Override
//    public void notifyParallelRoad(int i) {
//
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
//
//    }
//
//    /**
//     * 获取特殊道路设施数据
//     * <p>
//     * 在巡航过程中，出现特殊道路设施（如：测速摄像头、测速雷达；违章摄像头；铁路道口；应急车道等等）时，
//     * 回进到 OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] infos)，通过 AMapNaviTrafficFacilityInfo  对象可获取道路交通设施信息。
//     *
//     * @param aMapNaviTrafficFacilityInfos
//     */
//    @Override
//    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
//        for (AMapNaviTrafficFacilityInfo info : aMapNaviTrafficFacilityInfos) {
////            Toast.makeText(this, , Toast.LENGTH_LONG).show();
//            Log.d(TAG, "(trafficFacilityInfo.coor_X+trafficFacilityInfo.coor_Y+trafficFacilityInfo.distance+trafficFacilityInfo.limitSpeed):"
//                    + (info.getCoorX() + info.getCoorY() + info.getDistance() + info.getLimitSpeed()));
//        }
//    }
//
//    @Override
//    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
//
//    }
//
//    /**
//     * 获取巡航统计数据
//     * <p>
//     * 连续5个点速度大于15km/h后触发 updateAimlessModeStatistics 回调，通过 AimLessModeStat 对象可获取巡航的连续行驶距离和连
//     *
//     * @param aimLessModeStat
//     */
//    @Override
//    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
////        Toast.makeText(this, "看log", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "distance=" + aimLessModeStat.getAimlessModeDistance());
//        Log.d(TAG, "time=" + aimLessModeStat.getAimlessModeTime());
//    }
//
//    /**
//     * 获取巡航拥堵数据
//     * <p>
//     * 在巡航过程中，出现拥堵长度大于500米且拥堵时间大于5分钟时，
//     * 会进到 updateAimlessModeCongestionInfo 回调中，
//     * 通过 AimLessModeCongestionInfo 对象，可获取到道路拥堵信息（如：导致拥堵的事件类型、拥堵的状态等）。
//     *
//     * @param aimLessModeCongestionInfo
//     */
//    @Override
//    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
//        Log.d(TAG, "roadName=" + aimLessModeCongestionInfo.getRoadName());
//        Log.d(TAG, "CongestionStatus=" + aimLessModeCongestionInfo.getCongestionStatus());
//        Log.d(TAG, "eventLonLat=" + aimLessModeCongestionInfo.getEventLon() + "," + aimLessModeCongestionInfo.getEventLat());
//        Log.d(TAG, "length=" + aimLessModeCongestionInfo.getLength());
//        Log.d(TAG, "time=" + aimLessModeCongestionInfo.getTime());
//        for (AMapCongestionLink link : aimLessModeCongestionInfo.getAmapCongestionLinks()) {
//            Log.d(TAG, "status=" + link.getCongestionStatus());
//            for (NaviLatLng latlng : link.getCoords()) {
//                Log.d(TAG, latlng.toString());
//            }
//        }
//    }
//
//    @Override
//    public void onPlayRing(int i) {
//
//    }
//}
