package com.dalimao.didi.common.push;

import com.dalimao.didi.main.model.bean.Driver;

/**
 * Created by liuguangli on 17/3/18.
 */

public class DriverLocationMsg extends MsgType {

    Driver data;

    public Driver getData() {
        return data;
    }

    public void setData(Driver data) {
        this.data = data;
    }
}
