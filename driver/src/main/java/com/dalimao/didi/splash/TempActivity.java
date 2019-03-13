package com.dalimao.didi.splash;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dalimao.didi.R;
import com.dalimao.didi.common.utils.ToastUtil;

import wangfeixixi.lbs.Gps;
import wangfeixixi.lbs.GpsUtil;
import wangfeixixi.lbs.IMapService;
import wangfeixixi.lbs.LocationInfo;
import wangfeixixi.lbs.MapServiceUtils;
import wangfeixixi.lbs.OnInfoWindowMarkerListener;
import wangfeixixi.lbs.OnLocationListener;
import wangfeixixi.lbs.justlocal.Localmanager;

public class TempActivity extends AppCompatActivity {

    //    private IMapService iMapService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity);
        FrameLayout viewById = findViewById(R.id.rl);

        IMapService iMapService = MapServiceUtils.switchMapService(0, this);
//        IMapService iMapService = MapServiceUtils.switchMapServic2(0, this);

        iMapService.onCreate(savedInstanceState);

        viewById.addView(iMapService.getMap());

        serviceIntent = new Intent(this, ServiceTemp.class);

        Gps gps = GpsUtil.gps84_To_Gcj02(30.329193793402776, 121.31807400173611);

        LocationInfo locationInfo = new LocationInfo();
        locationInfo.longitude = gps.longitude;
        locationInfo.latitude = gps.latitude;
        locationInfo.name = "创新中心";
        locationInfo.address = "5G&V2X停车场";
        iMapService.addOrUpdateMarker(locationInfo, BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        iMapService.addMyLocationMarker(locationInfo, BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        iMapService.moveCamera(locationInfo, 18);
        iMapService.addInfoWindowMarker(locationInfo, BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        iMapService.setMarkerInfoWindowClickListener(new OnInfoWindowMarkerListener() {
            @Override
            public void onClick(String title, String msg) {
                ToastUtil.show(TempActivity.this, msg);
            }
        });


        final TextView tv_log = findViewById(R.id.tv_log);
//        MapServiceUtils mapServiceUtils = new MapServiceUtils();
//        iMapService = mapServiceUtils.switchMapService(0, this);
//
//        iMapService.onCreate(savedInstanceState);
//        iMapService.justLocationListener(new OnLocationListener() {
//            @Override
//            public void onLocationChange(LocationInfo locationInfo) {
//                tv_log.setText("local\n" + locationInfo.toString());
//            }
//        });
        findViewById(R.id.btn_temp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Localmanager.getInstance().startLocation();
            }
        });

        Localmanager.getInstance().initLocation(this, new OnLocationListener() {
            @Override
            public void onLocationChange(LocationInfo locationInfo) {
                tv_log.setText(locationInfo.toString());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //如果已经开始定位了，显示通知栏
        if (Localmanager.getInstance().isSartLocation) {
            if (null != serviceIntent) {
                startService(serviceIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果要一直显示可以不执行
        if (null != serviceIntent) {
            stopService(serviceIntent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Localmanager.getInstance().destroyLocation();
        //如果要一直显示可以不执行
        if (null != serviceIntent) {
            stopService(serviceIntent);
        }

    }
}
