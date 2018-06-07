package com.dalimao.didi.common.http;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装 HTTP 通用的请求信息
 * Created by liuguangli on 17/3/4.
 *
 */

public class CommonRequest {
    private String url;
    private Map<String, String> header;
    private Map<String, Object> body;

    public CommonRequest(String url) {
        this.url = url;
        header = new HashMap();
        body = new HashMap<>();
        //通用头部
        header.put("X-Bmob-Application-Id", HttpConfig.getCurrentAppID());
        header.put("X-Bmob-REST-API-Key", HttpConfig.getCurrentAppKey());

    }

    public void setHeader(String key, String value) {
        header.put(key, value);
    }

    public void setBody(String key, String value) {
        body.put(key, value);
    }

    public String getJsonBody() {

       return new Gson().toJson(this.body, HashMap.class);

    }
    public String getLineParam(){
        for (String key : body.keySet()) {


            url = url.replace("${" + key + "}", body.get(key).toString());

        }
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getUrl() {
        return url;
    }
}
