package com.dalimao.didi.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.dalimao.didi.main.ui.MainActivity;

/**
 * 设备相关的工具类
 * Created by liuguangli on 17/3/13.
 */

public class DevUtil {
    public static String getRandomUUID() {

        return java.util.UUID.randomUUID().toString();
    }
    public static void closeInputMethod(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
