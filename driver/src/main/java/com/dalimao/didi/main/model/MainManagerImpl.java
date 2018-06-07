package com.dalimao.didi.main.model;

import android.text.TextUtils;

import com.dalimao.didi.DidiApplication;
import com.dalimao.didi.account.model.bean.AccountInfo;
import com.dalimao.didi.account.model.bean.NearDrivers;
import com.dalimao.didi.common.databus.RxBus;
import com.dalimao.didi.common.http.API;
import com.dalimao.didi.common.http.CommonHandler;
import com.dalimao.didi.common.http.CommonRequest;
import com.dalimao.didi.common.http.CommonResponse;
import com.dalimao.didi.common.http.HttpConfig;
import com.dalimao.didi.common.http.IHttpClient;
import com.dalimao.didi.common.repository.Repository;
import com.dalimao.didi.common.utils.DevUtil;
import com.dalimao.didi.common.utils.LogUtil;
import com.dalimao.didi.order.model.bean.Order;
import com.dalimao.didi.order.model.bean.OrderOptMsg;
import com.google.gson.Gson;

import rx.functions.Func1;

/**
 * Created by liuguangli on 17/3/15.
 */

public class MainManagerImpl implements IMainManager {
    private static final String TAG = "MainManagerImpl";
    private IHttpClient httpClient;

    public MainManagerImpl(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void getNearDrivers(final double latitude, final double longitude) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                CommonRequest commonRequest = new CommonRequest(HttpConfig.getCurrentDomain() + API.GET_NEAR_DRIVERS);
                commonRequest.setBody("latitude", new Double(latitude).toString() );
                commonRequest.setBody("longitude", new Double(longitude).toString());
                CommonResponse response = httpClient.get(commonRequest, new CommonHandler());
                NearDrivers nearDrivers;
                if (response.getStateCode() == CommonResponse.STATE_OK) {
                    nearDrivers = new Gson().fromJson(response.getData(), NearDrivers.class);
                } else {
                    nearDrivers = new NearDrivers();
                    nearDrivers.setCode(response.getStateCode());
                }

                return nearDrivers;
            }
        });
    }

    @Override
    public void updateMyLocation(final String key, final double latitude, final double longitude, final float rotation) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                CommonRequest commonRequest = new CommonRequest(HttpConfig.getCurrentDomain() + API.UPDATE_MY_LOCATION);
                commonRequest.setBody("latitude", new Double(latitude).toString() );
                commonRequest.setBody("longitude", new Double(longitude).toString());
                commonRequest.setBody("rotation", new Float(rotation).toString());
                commonRequest.setBody("key", key);
                CommonResponse response = httpClient.post(commonRequest, new CommonHandler());
              
                if (response.getStateCode() == CommonResponse.STATE_OK) {
                    LogUtil.d(TAG, "update location suc");
                }
                return null;
            }
        });
    }

    @Override
    public void callNearDrivers(final String key, final double startLatitude, final double startLongitude, final double endLatitude, final double endLongitude, final String startAddr, final String endAddr , final float cost) {
        RxBus.getInstance().chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                Repository repository =  new Repository();
                final AccountInfo.Data info = repository.getLocalAccountFromPF().getData();
                if (info == null || info.getUid() == null) {
                    // 用户未注册
                    AccountInfo accountInfo = new AccountInfo();
                    accountInfo.setCode(CommonResponse.STATE_NO_REGISTER);
                    return accountInfo;
                } else {
                    if (TextUtils.isEmpty(info.getAccount()) ) {
                        // 用户过期
                        AccountInfo accountInfo = new AccountInfo();
                        accountInfo.setCode(CommonResponse.STATE_NOT_TOKEN_EXPIRE);
                        return accountInfo;
                    }
                    CommonRequest commonRequest = new CommonRequest(HttpConfig.getCurrentDomain() + API.CALL_DRIVERS);
                    commonRequest.setBody("startLatitude", new Double(startLatitude).toString() );
                    commonRequest.setBody("startLongitude", new Double(startLongitude).toString());
                    commonRequest.setBody("endLatitude", new Double(endLatitude).toString() );
                    commonRequest.setBody("endLongitude", new Double(endLongitude).toString());
                    commonRequest.setBody("cost", new Float(cost).toString());
                    commonRequest.setBody("key", key);
                    commonRequest.setBody("phone", info.getAccount());
                    commonRequest.setBody("startAddr", startAddr);
                    commonRequest.setBody("endAddr", endAddr);
                    commonRequest.setBody("uid", repository.getAccountUID() );
                    CommonResponse response = httpClient.post(commonRequest, new CommonHandler());
                    OrderOptMsg msg = new OrderOptMsg();
                    if (response.getStateCode() == CommonResponse.STATE_OK) {
                        LogUtil.d(TAG, "call send suc");
                        msg.setCode(OrderOptMsg.STATE_CREATE_SUC);
                    }  else {
                        msg.setCode(OrderOptMsg.STATE_CREATE_FAIL);
                    }
                   return msg;
                }


            }
        });
    }
}
