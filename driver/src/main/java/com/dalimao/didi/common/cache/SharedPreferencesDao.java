package com.dalimao.didi.common.cache;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * ShraredPreference 数据访问对象
 * Created by liuguangli on 17/3/6.
 */

public class SharedPreferencesDao {
    public static final String FILE_USER = "FILE_USER";
    public static final String KEY_USER = "KEY_USER";
    private SharedPreferences sharedPreferences;
    /**
     *  初始化
     */
    public SharedPreferencesDao(Application application, String fileName) {
        sharedPreferences = application.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
    /**
     * 保存 k-v
     */
    public void save(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }
    /**
     *  读取 k-v
     */
    public String get(String key) {

        return sharedPreferences.getString(key, null);
    }

    /**
     * 保存对象
     */
    public void save(String key, Object object) {
        String value = new Gson().toJson(object);
        save(key, value);
    }

    /**
     *  读取对象
     */

    public Object get(String key, Class cls) {

        String value = get(key);
        Object o = new Gson().fromJson(value, cls);
        return o;
    }
}
