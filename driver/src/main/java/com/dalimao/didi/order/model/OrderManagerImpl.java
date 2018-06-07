package com.dalimao.didi.order.model;

import com.dalimao.didi.DidiApplication;
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

import static android.R.attr.numberPickerStyle;
import static android.R.attr.rotation;

/**
 * Created by liuguangli on 17/3/25.
 */

public class OrderManagerImpl implements IOrderManager {
    private static final String TAG = "OrderManagerImpl";
    private IHttpClient httpClient;

    public OrderManagerImpl(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }


    @Override
    public void cancelOrder(final String id) {

        RxBus.getInstance().chainProcess(new Func1() {


            @Override
            public Object call(Object o) {
                CommonRequest commonRequest = new CommonRequest(HttpConfig.getCurrentDomain() + API.CANCEL_ORDER);
                commonRequest.setBody("id", id );
                CommonResponse response = httpClient.post(commonRequest, new CommonHandler());
                OrderOptMsg msg = new OrderOptMsg();
                if (response.getStateCode() == CommonResponse.STATE_OK) {
                    msg.setCode(OrderOptMsg.STATE_CANCEL_SUC);
                } else {
                    msg.setCode(OrderOptMsg.STATE_CANCEL_FAIL);
                }

                return msg;
            }
        });
    }

    @Override
    public void getProcessingOrder() {
        RxBus.getInstance().chainProcess(new Func1() {


            @Override
            public Object call(Object o) {
                CommonRequest commonRequest = new CommonRequest(HttpConfig.getCurrentDomain() + API.GET_PROCESSING_ORDERS);
                commonRequest.setBody("uid", new Repository().getAccountUID());
                CommonResponse response = httpClient.get(commonRequest, new CommonHandler());
                if (response.getStateCode() == CommonResponse.STATE_OK) {
                    Order order = new Gson().fromJson(response.getData(), Order.class);
                    return order.getData();
                }
                return null;
            }
        });
    }

    @Override
    public void paySuc(final Order.Data order) {
        RxBus.getInstance().chainProcess(new Func1() {


            @Override
            public Object call(Object o) {
                CommonRequest commonRequest = new CommonRequest(HttpConfig.getCurrentDomain() + API.CANCEL_ORDER);
                commonRequest.setBody("id", order.getOrderId() );
                commonRequest.setBody("state", new Integer(Order.HAS_PAYED ).toString());
                CommonResponse response = httpClient.post(commonRequest, new CommonHandler());
                OrderOptMsg msg = new OrderOptMsg();
                if (response.getStateCode() == CommonResponse.STATE_OK) {
                    msg.setCode(OrderOptMsg.PAY_SUC);
                } else {
                    msg.setCode(OrderOptMsg.PAY_FAIL);
                }

                return msg;
            }
        });
    }
}
