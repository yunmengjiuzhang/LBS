package com.dalimao.didi.order.model.bean;

import com.dalimao.didi.common.BaseBean;

/**
 * Created by liuguangli on 17/3/25.
 */

public class OrderOptMsg extends BaseBean {
    public static final int STATE_CANCEL_SUC = 1;
    public static final int STATE_CANCEL_FAIL = -1;
    public static final int PAY_SUC = 2;
    public static final int PAY_FAIL = -2;
    public static final int STATE_CREATE_FAIL = -3;
    public static final int STATE_CREATE_SUC = 3;
}
