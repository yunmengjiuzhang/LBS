package com.dalimao.didi.common.push;

import com.dalimao.didi.order.model.bean.Order;

/**
 * Created by liuguangli on 17/3/25.
 */

public class OrderMsg extends MsgType {
    private Order.Data data;

    public Order.Data getData() {
        return data;
    }

    public void setData(Order.Data data) {
        this.data = data;
    }
}
