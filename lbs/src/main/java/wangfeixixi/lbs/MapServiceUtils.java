package wangfeixixi.lbs;

import android.content.Context;

import wangfeixixi.lbs.gaode.GaodeMapService;

public class MapServiceUtils {
    /**
     * @param type 0:高德，1：百度，2腾讯
     */
    public static IMapService switchMapService(int type, Context context) {
        return new GaodeMapService(context);
    }

}
