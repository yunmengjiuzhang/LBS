package com.dalimao.mytaxi.main.view

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView

import com.dalimao.mytaxi.MyTaxiApplication
import com.dalimao.mytaxi.R
import com.dalimao.mytaxi.account.model.AccountManagerImpl
import com.dalimao.mytaxi.account.model.IAccountManager
import com.dalimao.mytaxi.account.view.PhoneInputDialog
import com.dalimao.mytaxi.common.databus.RxBus
import com.dalimao.mytaxi.common.http.IHttpClient
import com.dalimao.mytaxi.common.http.api.API
import com.dalimao.mytaxi.common.http.impl.OkHttpClientImpl
import com.dalimao.mytaxi.common.storage.SharedPreferencesDao
import com.dalimao.mytaxi.common.util.DevUtil
import com.dalimao.mytaxi.common.util.LogUtil
import com.dalimao.mytaxi.common.util.ToastUtil
import com.dalimao.mytaxi.main.model.IMainManager
import com.dalimao.mytaxi.main.model.MainMangerImpl
import com.dalimao.mytaxi.main.model.bean.Order
import com.dalimao.mytaxi.main.presenter.IMainPresenter
import com.dalimao.mytaxi.main.presenter.MainPresenterImpl

import java.util.ArrayList

import cn.bmob.push.BmobPush
import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobInstallation
import wangfeixixi.lbs.IMapService
import wangfeixixi.lbs.LocationInfo
import wangfeixixi.lbs.OnLocationListener
import wangfeixixi.lbs.OnRouteListener
import wangfeixixi.lbs.OnSearchedListener
import wangfeixixi.lbs.RouteInfo
import wangfeixixi.lbs.gaode.GaodeMapService


/**
 * －－－ 登录逻辑－－－
 * 1 检查本地纪录(登录态检查)
 * 2 若用户没登录则登录
 * 3 登录之前先校验手机号码
 * 4 token 有效使用 token 自动登录
 * －－－－ 地图初始化－－－
 * 1 地图接入
 * 2 定位自己的位置，显示蓝点
 * 3 使用 Marker 标记当前位置和方向
 * 4 地图封装
 * ------获取附近司机---
 */
class MainActivity : AppCompatActivity(), IMainView {
    //    private static final String LOCATION_END = "10000end";
    private var mPresenter: IMainPresenter? = null
    private var mLbsLayer: IMapService? = null
    private var mDriverBit: Bitmap? = null
    private var mPushKey: String? = null

    //  起点与终点
    private var mStartEdit: AutoCompleteTextView? = null
    private var mEndEdit: AutoCompleteTextView? = null
    private var mEndAdapter: PoiAdapter? = null
    // 标题栏显示当前城市
    private var mCity: TextView? = null
    // 记录起点和终点
    private var mStartLocation: LocationInfo? = null
    private var mEndLocation: LocationInfo? = null
    private var mStartBit: Bitmap? = null
    private var mEndBit: Bitmap? = null
    //  当前是否登录
    private var mIsLogin: Boolean = false
    //  操作状态相关元素
    private var mOptArea: View? = null
    private var mLoadingArea: View? = null
    private var mTips: TextView? = null
    private var mLoadingText: TextView? = null
    private var mBtnCall: Button? = null
    private var mBtnCancel: Button? = null
    private var mBtnPay: Button? = null
    private var mCost: Float = 0.toFloat()
    private var mLocationBit: Bitmap? = null
    private var mIsLocate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val httpClient = OkHttpClientImpl()
        val dao = SharedPreferencesDao(MyTaxiApplication.getInstance(),
                SharedPreferencesDao.FILE_ACCOUNT)
        val manager = AccountManagerImpl(httpClient, dao)
        val mainManager = MainMangerImpl(httpClient)
        mPresenter = MainPresenterImpl(this, manager, mainManager)
        RxBus.getInstance().register(mPresenter)
        mPresenter!!.loginByToken()

        // 地图服务
        mLbsLayer = GaodeMapService(this)
        mLbsLayer!!.onCreate(savedInstanceState)
        mLbsLayer!!.setLocationChangeListener { locationInfo ->
            // 记录起点
            mStartLocation = locationInfo
            //  设置标题
            mCity!!.text = mLbsLayer!!.city
            // 设置起点
            mStartEdit!!.setText(locationInfo.name)
            // 获取附近司机
            getNearDrivers(locationInfo.latitude, locationInfo.longitude)
            // 上报当前位置
            updateLocationToServer(locationInfo)
            // 首次定位，添加当前位置的标记
            addLocationMarker()
            mIsLocate = true
            //  获取进行中的订单
            getProcessingOrder()
        }

        // 添加地图到容器
        val mapViewContainer = findViewById(R.id.map_container) as ViewGroup
        mapViewContainer.addView(mLbsLayer!!.map)

        // 推送服务
        // 初始化BmobSDK
        Bmob.initialize(this, API.Config.getAppId())
        // 使用推送服务时的初始化操作
        val installation = BmobInstallation.getCurrentInstallation(this)
        installation.save()
        mPushKey = installation.installationId
        // 启动推送服务
        BmobPush.startWork(this)


        //  初始化其他视图元素
        initViews()

        mIsLogin = mPresenter!!.isLogin
    }

    private fun addLocationMarker() {
        if (mLocationBit == null || mLocationBit!!.isRecycled) {
            mLocationBit = BitmapFactory.decodeResource(resources,
                    R.drawable.navi_map_gps_locked)
        }
        mLbsLayer!!.addOrUpdateMarker(mStartLocation, mLocationBit)
    }

    private fun initViews() {
        mStartEdit = findViewById(R.id.start) as AutoCompleteTextView
        mEndEdit = findViewById(R.id.end) as AutoCompleteTextView
        mCity = findViewById(R.id.city) as TextView
        mOptArea = findViewById(R.id.optArea)
        mLoadingArea = findViewById(R.id.loading_area)
        mLoadingText = findViewById(R.id.loading_text) as TextView
        mBtnCall = findViewById(R.id.btn_call_driver) as Button
        mBtnCancel = findViewById(R.id.btn_cancel) as Button
        mBtnPay = findViewById(R.id.btn_pay) as Button
        mTips = findViewById(R.id.tips_info) as TextView

        val listener = View.OnClickListener { v ->
            when (v.id) {
                R.id.btn_call_driver ->
                    // 呼叫司机
                    callDriver()
                R.id.btn_cancel ->
                    //  取消
                    cancel()
                R.id.btn_pay ->
                    // 支付
                    pay()
            }
        }
        mBtnCall!!.setOnClickListener(listener)
        mBtnCancel!!.setOnClickListener(listener)
        mBtnPay!!.setOnClickListener(listener)
        mEndEdit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                //  关键搜索推荐地点
                mLbsLayer!!.poiSearch(s.toString(), object : OnSearchedListener {
                    override fun onSearched(results: List<LocationInfo>) {
                        // 更新列表
                        updatePoiList(results)
                    }

                    override fun onError(rCode: Int) {

                    }
                })
            }
        })
    }

    private fun pay() {
        mLoadingArea!!.visibility = View.VISIBLE
        mTips!!.visibility = View.GONE
        mLoadingText!!.setText(R.string.paying)
        mPresenter!!.pay()
    }

    /**
     * 取消
     */
    private fun cancel() {
        if (!mBtnCall!!.isEnabled) {
            // 说明已经点了呼叫
            showCanceling()
            mPresenter!!.cancel()
        } else {
            // 知识显示了路径信息，还没点击呼叫，恢复 UI 即可
            restoreUI()
        }
    }

    /**
     * 显示取消中
     */
    private fun showCanceling() {
        mTips!!.visibility = View.GONE
        mLoadingArea!!.visibility = View.VISIBLE
        mLoadingText!!.text = getString(R.string.canceling)
        mBtnCancel!!.isEnabled = false
    }

    /**
     * 恢复 UI
     */

    private fun restoreUI() {
        // 清楚地图上所有标记：路径信息、起点、终点
        mLbsLayer!!.clearAllMarker()
        // 添加定位标记
        addLocationMarker()
        // 恢复地图视野
        mLbsLayer!!.moveCamera(mStartLocation, 17)
        //  获取附近司机
        getNearDrivers(mStartLocation!!.latitude, mStartLocation!!.longitude)
        // 隐藏操作栏
        hideOptArea()

    }

    private fun hideOptArea() {
        mOptArea!!.visibility = View.GONE

    }

    /**
     * 呼叫司机
     */
    private fun callDriver() {
        mIsLogin = mPresenter!!.isLogin
        if (mIsLogin) {
            // 已登录，直接呼叫
            showCalling()
            //   请求呼叫
            mPresenter!!.callDriver(mPushKey, mCost, mStartLocation, mEndLocation)
        } else {
            // 未登录，先登录
            ToastUtil.show(this, getString(R.string.pls_login))
            showPhoneInputDialog()
        }
    }

    private fun showCalling() {
        mTips!!.visibility = View.GONE
        mLoadingArea!!.visibility = View.VISIBLE
        mLoadingText!!.text = getString(R.string.calling_driver)
        mBtnCancel!!.isEnabled = true
        mBtnCall!!.isEnabled = false
    }

    /**
     * 更新 POI 列表
     *
     * @param results
     */
    private fun updatePoiList(results: List<LocationInfo>) {

        val listString = ArrayList<String>()
        for (i in results.indices) {
            listString.add(results[i].name)
        }
        if (mEndAdapter == null) {
            mEndAdapter = PoiAdapter(applicationContext, listString)
            mEndEdit!!.setAdapter<PoiAdapter>(mEndAdapter)

        } else {

            mEndAdapter!!.setData(listString)
        }
        mEndEdit!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            ToastUtil.show(this@MainActivity, results[position].name)
            DevUtil.closeInputMethod(this@MainActivity)
            //  记录终点
            mEndLocation = results[position]
            //                mEndLocation.setKey(LOCATION_END);
            // 绘制路径
            showRoute(mStartLocation, mEndLocation, OnRouteListener { result ->
                LogUtil.d(TAG, "driverRoute: " + result)


                mLbsLayer!!.moveCamera(mStartLocation, mEndLocation)
                // 显示操作区
                showOptArea()
                mCost = result.getTaxiCost()
                var infoString = getString(R.string.route_info)
                infoString = String.format(infoString,
                        result.getDistance().toInt(),
                        mCost,
                        result.getDuration())
                mTips!!.visibility = View.VISIBLE
                mTips!!.text = infoString
            })
        }
        mEndAdapter!!.notifyDataSetChanged()
    }

    /**
     * 绘制起点终点路径
     */

    private fun showRoute(mStartLocation: LocationInfo?,
                          mEndLocation: LocationInfo?,
                          listener: OnRouteListener) {

        mLbsLayer!!.clearAllMarker()
        addStartMarker()
        addEndMarker()
        mLbsLayer!!.driverRoute(mStartLocation,
                mEndLocation,
                Color.GREEN,
                listener
        )
    }


    /**
     * 显示操作区
     */
    private fun showOptArea() {
        mOptArea!!.visibility = View.VISIBLE
        mLoadingArea!!.visibility = View.GONE
        mTips!!.visibility = View.VISIBLE
        mBtnCall!!.isEnabled = true
        mBtnCancel!!.isEnabled = true
        mBtnCancel!!.visibility = View.VISIBLE
        mBtnCall!!.visibility = View.VISIBLE
        mBtnPay!!.visibility = View.GONE
    }

    private fun addStartMarker() {
        if (mStartBit == null || mStartBit!!.isRecycled) {
            mStartBit = BitmapFactory.decodeResource(resources,
                    R.drawable.start)
        }
        mLbsLayer!!.addOrUpdateMarker(mStartLocation, mStartBit)
    }

    private fun addEndMarker() {
        if (mEndBit == null || mEndBit!!.isRecycled) {
            mEndBit = BitmapFactory.decodeResource(resources,
                    R.drawable.end)
        }
        mLbsLayer!!.addOrUpdateMarker(mEndLocation, mEndBit)
    }


    /**
     * 上报当前位置
     *
     * @param locationInfo
     */
    private fun updateLocationToServer(locationInfo: LocationInfo) {
        //        locationInfo.setKey(mPushKey);
        mPresenter!!.updateLocationToServer(locationInfo)
    }


    /**
     * 获取附近司机
     *
     * @param latitude
     * @param longitude
     */
    private fun getNearDrivers(latitude: Double, longitude: Double) {

        mPresenter!!.fetchNearDrivers(latitude, longitude)
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mLbsLayer!!.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mLbsLayer!!.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mLbsLayer!!.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        RxBus.getInstance().unRegister(mPresenter)
        mLbsLayer!!.onDestroy()
    }

    /**
     * 自动登录成功
     */

    override fun showLoginSuc() {
        ToastUtil.show(this, getString(R.string.login_suc))
        mIsLogin = true
        if (mStartLocation != null) {
            updateLocationToServer(mStartLocation!!)
        }
        // 获取正在进行中的订单
        getProcessingOrder()
    }

    /**
     * 获取正在进行中的订单
     */
    private fun getProcessingOrder() {
        /**
         * 满足： 已经登录、已经定位两个条件，执行 getProcessingOrder
         */
        if (mIsLogin && mIsLocate) {
            mPresenter!!.getProcessingOrder()
        }

    }

    /**
     * 显示附近司机
     *
     * @param data
     */

    override fun showNears(data: List<LocationInfo>) {

        for (locationInfo in data) {
            showLocationChange(locationInfo)
        }
    }

    /**
     * 显示司机的标记
     *
     * @param locationInfo
     */
    override fun showLocationChange(locationInfo: LocationInfo) {
        if (mDriverBit == null || mDriverBit!!.isRecycled) {
            mDriverBit = BitmapFactory.decodeResource(resources, R.drawable.car)
        }
        mLbsLayer!!.addOrUpdateMarker(locationInfo, mDriverBit)
    }


    /**
     * 呼叫司机成功发出
     */
    override fun showCallDriverSuc(order: Order) {
        mLoadingArea!!.visibility = View.GONE
        mTips!!.visibility = View.VISIBLE
        mTips!!.text = getString(R.string.show_call_suc)
        // 显示操作区
        showOptArea()
        mBtnCall!!.isEnabled = false
        // 显示路径信息
        if (order.endLongitude != 0.0 || order.driverLatitude != 0.0) {
            mEndLocation = LocationInfo(order.endLatitude, order.endLongitude)
            //            mEndLocation.setKey(LOCATION_END);
            // 绘制路径
            showRoute(mStartLocation, mEndLocation, OnRouteListener { result ->
                LogUtil.d(TAG, "driverRoute: " + result)


                mLbsLayer!!.moveCamera(mStartLocation, mEndLocation)
                mCost = result.getTaxiCost()
                var infoString = getString(R.string.route_info_calling)
                infoString = String.format(infoString,
                        result.getDistance().toInt(),
                        mCost,
                        result.getDuration())
                mTips!!.visibility = View.VISIBLE
                mTips!!.text = infoString
            })
        }
        LogUtil.d(TAG, "showCallDriverSuc: " + order)
    }

    override fun showCallDriverFail() {
        mLoadingArea!!.visibility = View.GONE
        mTips!!.visibility = View.VISIBLE
        mTips!!.text = getString(R.string.show_call_fail)
        mBtnCall!!.isEnabled = true

    }

    /**
     * 取消订单成功
     */
    override fun showCancelSuc() {
        ToastUtil.show(this, getString(R.string.order_cancel_suc))
        restoreUI()
    }

    /**
     * 取消订单失败
     */
    override fun showCancelFail() {
        ToastUtil.show(this, getString(R.string.order_cancel_error))
        mBtnCancel!!.isEnabled = true
    }

    /**
     * 司机接单
     *
     * @param order
     */
    override fun showDriverAcceptOrder(order: Order) {
        // 提示信息
        ToastUtil.show(this, getString(R.string.driver_accept_order))

        // 清除地图标记
        mLbsLayer!!.clearAllMarker()
        /**
         * 添加司机标记
         */

        val driverLocation = LocationInfo(order.driverLatitude,
                order.driverLongitude)
        //        driverLocation.setKey(order.getKey());
        showLocationChange(driverLocation)
        // 显示我的位置
        addLocationMarker()
        /**
         * 显示司机到乘客的路径
         */
        mLbsLayer!!.driverRoute(driverLocation,
                mStartLocation,
                Color.BLUE
        ) { result ->
            // 地图聚焦到司机和我的位置
            mLbsLayer!!.moveCamera(mStartLocation, driverLocation)
            // 显示司机、路径信息
            val stringBuilder = StringBuilder()
            stringBuilder.append("司机：")
                    .append(order.driverName)
                    .append(", 车牌：")
                    .append(order.carNo)
                    .append("，预计")
                    .append(result.getDuration())
                    .append("分钟到达")


            mTips!!.text = stringBuilder.toString()
            // 显示操作区
            showOptArea()
            // 呼叫不可点击
            mBtnCall!!.isEnabled = false
        }

    }

    /**
     * 提示司机到达
     *
     * @param mCurrentOrder
     */
    override fun showDriverArriveStart(mCurrentOrder: Order) {

        showOptArea()
        val arriveTemp = getString(R.string.driver_arrive)
        mTips!!.text = String.format(arriveTemp,
                mCurrentOrder.driverName,
                mCurrentOrder.carNo)
        mBtnCall!!.isEnabled = false
        mBtnCancel!!.isEnabled = true
        // 清除地图标记
        mLbsLayer!!.clearAllMarker()
        /**
         * 添加司机标记
         */

        val driverLocation = LocationInfo(mCurrentOrder.driverLatitude,
                mCurrentOrder.driverLongitude)
        //        driverLocation.setKey(mCurrentOrder.getKey());
        showLocationChange(driverLocation)
        // 显示我的位置
        addLocationMarker()
    }

    /**
     * 司机到上车地点的路径绘制
     *
     * @param locationInfo
     */

    override fun updateDriver2StartRoute(locationInfo: LocationInfo, order: Order) {

        mLbsLayer!!.clearAllMarker()
        addLocationMarker()
        showLocationChange(locationInfo)
        mLbsLayer!!.driverRoute(locationInfo, mStartLocation, Color.BLUE) { result ->
            val tipsTemp = getString(R.string.accept_info)
            mTips!!.text = String.format(tipsTemp,
                    order.driverName,
                    order.carNo,
                    result.getDistance(),
                    result.getDuration())
        }
        // 聚焦
        mLbsLayer!!.moveCamera(locationInfo, mStartLocation)

    }

    /**
     * 显示开始行程
     *
     * @param order
     */
    override fun showStartDrive(order: Order) {


        val locationInfo = LocationInfo(order.driverLatitude, order.driverLongitude)
        //        locationInfo.setKey(order.getKey());
        // 路径规划绘制
        updateDriver2EndRoute(locationInfo, order)
        // 隐藏按钮
        mBtnCancel!!.visibility = View.GONE
        mBtnCall!!.visibility = View.GONE
    }

    /**
     * 显示到达终点
     *
     * @param order
     */
    override fun showArriveEnd(order: Order) {
        val tipsTemp = getString(R.string.pay_info)
        val tips = String.format(tipsTemp,
                order.cost,
                order.driverName,
                order.carNo)
        // 显示操作区
        showOptArea()
        mBtnCancel!!.visibility = View.GONE
        mBtnCall!!.visibility = View.GONE
        mTips!!.text = tips
        mBtnPay!!.visibility = View.VISIBLE
    }

    /**
     * 司机到终点的路径绘制或更新
     *
     * @param locationInfo
     */

    override fun updateDriver2EndRoute(locationInfo: LocationInfo, order: Order) {
        // 终点位置从 order 中获取
        if (order.endLongitude != 0.0 || order.endLatitude != 0.0) {
            mEndLocation = LocationInfo(order.endLatitude, order.endLongitude)
            //            mEndLocation.setKey(LOCATION_END);
        }
        mLbsLayer!!.clearAllMarker()
        addEndMarker()
        showLocationChange(locationInfo)
        addLocationMarker()
        mLbsLayer!!.driverRoute(locationInfo, mEndLocation, Color.GREEN) { result ->
            val tipsTemp = getString(R.string.driving_info)
            mTips!!.text = String.format(tipsTemp,
                    order.driverName,
                    order.carNo,
                    result.getDistance(),
                    result.getDuration())
            // 显示操作区
            showOptArea()
            mBtnCancel!!.isEnabled = false
            mBtnCall!!.isEnabled = false
        }
        // 聚焦
        mLbsLayer!!.moveCamera(locationInfo, mEndLocation)
    }

    /**
     * 显示支付成功
     *
     * @param mCurrentOrder
     */
    override fun showPaySuc(mCurrentOrder: Order) {
        restoreUI()
        ToastUtil.show(this, getString(R.string.pay_suc))
    }

    /**
     * 显示支付失败
     */
    override fun showPayFail() {
        restoreUI()
        ToastUtil.show(this, getString(R.string.pay_fail))
    }


    /**
     * 显示 loading
     */
    override fun showLoading() {
        // TODO: 17/5/14   显示加载框
    }

    /**
     * 错误处理
     *
     * @param code
     * @param msg
     */

    override fun showError(code: Int, msg: String) {
        when (code) {
            IAccountManager.TOKEN_INVALID -> {
                // 登录过期
                ToastUtil.show(this, getString(R.string.token_invalid))
                showPhoneInputDialog()
                mIsLogin = false
            }
            IAccountManager.SERVER_FAIL ->
                // 服务器错误
                showPhoneInputDialog()
        }
    }

    /**
     * 显示手机输入框
     */
    private fun showPhoneInputDialog() {
        val dialog = PhoneInputDialog(this)
        dialog.show()
        dialog.setOnDismissListener { RxBus.getInstance().register(mPresenter) }
        RxBus.getInstance().unRegister(mPresenter)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private val TAG = "MainActivity"
    }


}
