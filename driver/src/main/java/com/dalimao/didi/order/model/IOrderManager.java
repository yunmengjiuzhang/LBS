package com.dalimao.didi.order.model;

import com.dalimao.didi.order.model.bean.Order;

/**
 * 订单相关模块
 * Created by liuguangli on 17/3/25.
 */

public interface IOrderManager {
    /**
     *  取消订单
     */
    void cancelOrder(String id);

    /**
     *  获取进行中的订单
     */

    void getProcessingOrder();

    void paySuc(Order.Data mCurrentOrder);
}
