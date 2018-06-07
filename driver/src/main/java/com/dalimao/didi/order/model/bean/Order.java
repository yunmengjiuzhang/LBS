package com.dalimao.didi.order.model.bean;

import com.dalimao.didi.common.BaseBean;

/**
 * Created by liuguangli on 17/3/25.
 */

public class Order extends BaseBean {
    //  待接单状态
    public static final int CREATE = 0;
    // 已接单
    public static final int ACCEPT = 1;
    // 待上车状态
    public static final int ONBOARD = 2;
    // 行程中
    public static final int STROKE = 3;
    // 已结束
    public static final int FINISH = 4;
    // 已支付
    public static final int HAS_PAYED = 5;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {


        private double driverLatitude;
        private double driverLongitude;
        private float  driverRotation;
        private double startLatitude;
        private double startLongitude;
        private double endLatitude;
        private double endLongitude;
        private String startAddr;
        private String endAddr;
        private float cost;



        private String driverName;
        private String carNo;
        private String driverPhone;
        private int state;
        private String orderId;

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public void setDriverUid(String driverUid) {
            this.driverUid = driverUid;
        }

        private String driverUid;

        public double getDriverLatitude() {
            return driverLatitude;
        }

        public void setDriverLatitude(double driverLatitude) {
            this.driverLatitude = driverLatitude;
        }

        public double getDriverLongitude() {
            return driverLongitude;
        }

        public void setDriverLongitude(double driverLongitude) {
            this.driverLongitude = driverLongitude;
        }

        public float getDriverRotation() {
            return driverRotation;
        }

        public void setDriverRotation(float driverRotation) {
            this.driverRotation = driverRotation;
        }

        public float getCost() {
            return cost;
        }

        public void setCost(float cost) {
            this.cost = cost;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getDriverPhone() {
            return driverPhone;
        }

        public void setDriverPhone(String driverPhone) {
            this.driverPhone = driverPhone;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getDriverUid() {
            return driverUid;
        }

        public String getOrderId() {
            return orderId;
        }

        public double getStartLatitude() {
            return startLatitude;
        }

        public void setStartLatitude(double startLatitude) {
            this.startLatitude = startLatitude;
        }

        public double getStartLongitude() {
            return startLongitude;
        }

        public void setStartLongitude(double startLongitude) {
            this.startLongitude = startLongitude;
        }

        public String getStartAddr() {
            return startAddr;
        }

        public void setStartAddr(String startAddr) {
            this.startAddr = startAddr;
        }

        public String getEndAddr() {
            return endAddr;
        }

        public void setEndAddr(String endAddr) {
            this.endAddr = endAddr;
        }

        public double getEndLatitude() {
            return endLatitude;
        }

        public void setEndLatitude(double endLatitude) {
            this.endLatitude = endLatitude;
        }

        public double getEndLongitude() {
            return endLongitude;
        }

        public void setEndLongitude(double endLongitude) {
            this.endLongitude = endLongitude;
        }

        public boolean processing() {
            return state != HAS_PAYED;
        }


    }
}
