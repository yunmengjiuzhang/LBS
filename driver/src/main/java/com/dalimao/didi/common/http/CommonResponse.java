package com.dalimao.didi.common.http;

/**
 * Created by liuguangli on 17/3/5.
 */

public class CommonResponse {

    public static final int STATE_OK = 200;
    //服务器错误
    public static final int STATE_ERROR =100001 ;
    // 用户未注册
    public static final int STATE_NO_REGISTER = 100002;
    //用户已经存
    public static final int STATE_USER_EXIST = 100003;
    //密码错误
    public static final int STATE_PW_ERROR = 100005;
    // token 过期
    public static final int STATE_TOKEN_EXPIRE = 100006;
    public static final int STATE_NOT_TOKEN_EXPIRE = 100007;
    //头部信息
    private int stateCode;
    //响应数据
    private String data;

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "stateCode=" + stateCode +
                ", data='" + data + '\'' +
                '}';
    }
}
