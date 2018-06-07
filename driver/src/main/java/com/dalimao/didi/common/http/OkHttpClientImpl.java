package com.dalimao.didi.common.http;

import android.util.Log;

import com.dalimao.didi.common.utils.LogUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liuguangli on 17/3/4.
 */

public class OkHttpClientImpl implements IHttpClient {
    private static OkHttpClientImpl instance;
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            long t1 = System.nanoTime();
            Request request = chain.request();

            Log.d("OkHttpClientImpl" , String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();

            Log.d("OkHttpClientImpl" ,String.format("Received response for %s in %.1fms%n%s",
                    request.url(), (t2 - t1) / 1e6d, response.headers()));

            return response;

        }
    };
    OkHttpClient mOkHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    private OkHttpClientImpl (){};
    public static synchronized OkHttpClientImpl  getInstance() {
        if (instance == null) {
            instance = new OkHttpClientImpl();
        }
        return instance;
    }
    @Override
    public CommonResponse get(CommonRequest request, CommonHandler handler) {

        // 设置请求头部
        Map<String, String> header = request.getHeader();
        Request.Builder builder = new Request.Builder();
        for (String key : header.keySet()) {

            builder.header(key, header.get(key));

        }
        builder.url(request.getLineParam())
                .get();
        Request oKRequest = builder.build();
        return  execute(oKRequest);
    }

    @Override
    public CommonResponse post(CommonRequest request, CommonHandler handler) {
        //定义数据格式为 json  格式
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        // 设置请求体
        RequestBody body = RequestBody.create(mediaType, request.getJsonBody());
        // 设置请求头部
        Map<String, String> header = request.getHeader();
        Request.Builder builder = new Request.Builder();
        for (String key : header.keySet()) {

            builder.header(key, header.get(key));

        }
        builder.url(request.getUrl())
                .post(body);
        Request oKRequest = builder.build();
        return  execute(oKRequest);

    }
    private CommonResponse execute(Request request)  {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            commonResponse.setStateCode(response.code());
            String body = response.body().string();
            commonResponse.setData(body);
            Log.d("OkHttpClientImpl" ,String.format("Received response body: %s ",
                    body));
        } catch (IOException e) {
            e.printStackTrace();
            commonResponse.setStateCode(CommonResponse.STATE_ERROR);
            commonResponse.setData(e.getMessage());
        }
        return commonResponse;

    }
}
