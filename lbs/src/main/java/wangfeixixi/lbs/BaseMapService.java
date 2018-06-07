package wangfeixixi.lbs;

import android.content.Context;
import android.os.Bundle;

public abstract class BaseMapService implements IMapService {

    public String KEY_MY_MARKERE = "-1";

    public Context mCtx;
    protected String mCity;

    // 业务层使用通用的监听器
    public OnLocationListener mLocationListener;


    public BaseMapService(Context context) {
        mCtx = context;
    }


    @Override
    public void setLocationChangeListener(OnLocationListener listener) {
        this.mLocationListener = listener;
    }


    @Override
    public String getCity() {
        return mCity;
    }

    @Override
    public void onCreate(Bundle savedState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
