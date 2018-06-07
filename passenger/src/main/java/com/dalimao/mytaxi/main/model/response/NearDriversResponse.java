package com.dalimao.mytaxi.main.model.response;

import com.dalimao.mytaxi.common.http.biz.BaseBizResponse;

import java.util.List;

import wangfeixixi.lbs.LocationInfo;

/**
 * Created by liuguangli on 17/5/31.
 */

public class NearDriversResponse extends BaseBizResponse {
    List<LocationInfo> data;

    public List<LocationInfo> getData() {
        return data;
    }

    public void setData(List<LocationInfo> data) {
        this.data = data;
    }
}
