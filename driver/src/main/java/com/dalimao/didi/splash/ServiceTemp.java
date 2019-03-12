package com.dalimao.didi.splash;

import com.dalimao.didi.R;

import wangfeixixi.lbs.justlocal.LocationForegoundService;

public class ServiceTemp extends LocationForegoundService {
    @Override
    public Class<?> getcls() {
        return TempActivity.class;
    }

    @Override
    public int getIconId() {
        return R.mipmap.ic_launcher;
    }
}
