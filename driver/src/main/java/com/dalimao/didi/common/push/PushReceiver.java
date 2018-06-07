package com.dalimao.didi.common.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dalimao.didi.common.databus.RxBus;
import com.dalimao.didi.common.utils.LogUtil;
import com.google.gson.Gson;

import cn.bmob.push.PushConstants;

/**
 * 司机位置变化
 * Created by liuguangli on 17/3/16.
 */

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Gson gson = new Gson();
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            LogUtil.d("bmob", "客户端收到推送内容："+ intent.getStringExtra("msg"));

            String json = intent.getStringExtra("msg");

            MsgType msg = gson.fromJson(json, MsgType.class);
            if (msg.getType() == MsgType.TYPE_DRIVER_LOCATION) {
                // 司机位置变化消息
                DriverLocationMsg  driverLocationMsg =  gson.fromJson(json, DriverLocationMsg.class);
                RxBus.getInstance().send(driverLocationMsg.getData());
            } else if (msg.getType() == MsgType.TYPE_ORDER) {

               OrderMsg orderMsg = gson.fromJson(json, OrderMsg.class);
                RxBus.getInstance().send(orderMsg.getData());
            }
        }
    }
}
