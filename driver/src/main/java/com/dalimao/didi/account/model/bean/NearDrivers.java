package com.dalimao.didi.account.model.bean;

import com.dalimao.didi.common.BaseBean;
import com.dalimao.didi.main.model.bean.Driver;

import java.util.List;

/**
 * Created by liuguangli on 17/3/15.
 */

public class NearDrivers extends BaseBean{
    private List<Driver> data;

    public List<Driver> getData() {
        return data;
    }

    public void setData(List<Driver> data) {
        this.data = data;
    }
}
