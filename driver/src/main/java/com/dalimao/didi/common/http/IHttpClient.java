package com.dalimao.didi.common.http;

import java.util.Map;

/**
 * Http 执行类的抽象
 * Created by liuguangli on 17/3/4.
 */

public interface IHttpClient {
    CommonResponse get(CommonRequest request, CommonHandler handler);
    CommonResponse post(CommonRequest request, CommonHandler handler);

}
