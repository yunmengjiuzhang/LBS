package com.dalimao.didi.common.repository;

import android.text.TextUtils;

import com.dalimao.didi.DidiApplication;
import com.dalimao.didi.account.model.bean.AccountInfo;
import com.dalimao.didi.common.cache.SharedPreferencesDao;
import com.dalimao.didi.common.http.CommonResponse;
import com.dalimao.didi.common.utils.DevUtil;
import com.dalimao.didi.common.utils.SafeUtil;
import com.google.gson.Gson;

/**
 * 本地数据仓库
 * Created by liuguangli on 17/3/25.
 */

public class Repository {
    public AccountInfo getLocalAccountFromPF(){
        SharedPreferencesDao dao = new SharedPreferencesDao(DidiApplication.getInstance(), SharedPreferencesDao.FILE_USER);
        String encrytString = dao.get(SharedPreferencesDao.KEY_USER);
        AccountInfo info;
        if (encrytString != null) {
            //解密
            String json = SafeUtil.decrypt(encrytString);
            info = new Gson().fromJson(json, AccountInfo.class);

            if (System.currentTimeMillis()  > info.getData().getExpired()) {

                // token 过期
                info.setCode(CommonResponse.STATE_TOKEN_EXPIRE);
            } else {
                info.setCode(CommonResponse.STATE_NOT_TOKEN_EXPIRE);
            }


        } else {
            info = new AccountInfo();
            info.setCode(CommonResponse.STATE_NO_REGISTER);
        }

        return info;
    }

    public String getAccountUID() {
        AccountInfo info = getLocalAccountFromPF();
        String uid = null;
        if (info != null && info.getData() != null) {
            uid = info.getData().getUid();
        }
        if (TextUtils.isEmpty(uid)) {
            uid = DevUtil.getRandomUUID();
        }
        return uid;
    }
}
