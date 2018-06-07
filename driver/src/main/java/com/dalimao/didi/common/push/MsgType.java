package com.dalimao.didi.common.push;

/**
 *
 * 消息类型
 * Created by liuguangli on 17/3/18.
 */

public class MsgType {
    /**
     * 司机位置变化信息
     */
    public static final  int TYPE_DRIVER_LOCATION = 1;
    /**
     * 订单状态变化
     */
    public static final int TYPE_ORDER = 2;
    protected int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
