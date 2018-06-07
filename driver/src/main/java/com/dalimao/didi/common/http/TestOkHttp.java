package com.dalimao.didi.common.http;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liuguangli on 17/2/20.
 */

public class TestOkHttp {
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            long t1 = System.nanoTime();
            Request request = chain.request();

            System.out.println(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();

            System.out.println(String.format("Received response for %s in %.1fms%n%s",
                    request.url(), (t2 - t1) / 1e6d, response.headers()));

            return response;

        }
    };
    public static void post() {
        OkHttpClient yOkHttpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "{'name': 'Jim'}");
        Request request = new Request.Builder()
                .url("http://liuguangli.mock.uctest.local:8024/weex/goodsList")
                .post(body)
                .build();
        try {
            Response response = yOkHttpClient.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void get() {

        Request request = new Request.Builder()
                .url("http://liuguangli.mock.uctest.local:8024/weex/goodsList")
                .build();
        OkHttpClient yOkHttpClient = new OkHttpClient();
        try {
            Response response = yOkHttpClient.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testCache(boolean forceNet) {
        int cacheSize = 10 * 1024 * 1024;

        Cache cache = new Cache(new File("bzh.tmp"), cacheSize);

        OkHttpClient yOkHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build();

        Request request = new Request.Builder()
                .cacheControl(forceNet ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
                .url("http://publicobject.com/helloworld.txt").build();

        Response response1 = null;
        try {
            response1 = yOkHttpClient.newCall(request).execute();

            if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

            String response1Body = response1.body().string();
            System.out.println("Response 1 response:          " + response1);
            System.out.println("Response 1 cache response:    " + response1.cacheResponse());
            System.out.println("Response 1 network response:  " + response1.networkResponse());

            Response response2 = yOkHttpClient.newCall(request).execute();
            if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

            String response2Body = response2.body().string();
            System.out.println("Response 2 response:           " + response2);
            System.out.println("Response 2 cache response:     " + response2.cacheResponse());
            System.out.println("Response 2 network response:   " + response2.networkResponse());

            System.out.println("Response 2 equals Response 1 ? " + response1Body.equals(response2Body));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testOkImpl(){
        IHttpClient httpClient = OkHttpClientImpl.getInstance();
        CommonRequest commonRequest = new CommonRequest("http://cloud.bmob.cn/f34e28da5816433d/getMsgCode");
        commonRequest.setBody("phone", "15919496914");
        httpClient.post(commonRequest, new CommonHandler());
    }

}
