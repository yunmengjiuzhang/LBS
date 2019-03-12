package com.dalimao.didi.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dalimao.didi.R;

import wangfeixixi.lbs.LocationInfo;
import wangfeixixi.lbs.OnLocationListener;
import wangfeixixi.lbs.justlocal.Localmanager;

public class TempActivity extends AppCompatActivity {

//    private IMapService iMapService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity);
        serviceIntent = new Intent(this, ServiceTemp.class);


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
