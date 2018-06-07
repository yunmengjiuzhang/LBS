package com.dalimao.didi.account.model.bean;

import android.provider.ContactsContract;

import com.dalimao.didi.common.BaseBean;

/**
 * Created by liuguangli on 17/3/2.
 */

public class AccountInfo extends BaseBean {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String account;
        private String token;
        private String uid;
        public long getExpired() {
            return expired;
        }

        public void setExpired(long expired) {
            this.expired = expired;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        private long expired;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getTokent() {
            return token;
        }

        public void setTokent(String token) {
            this.token = token;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

}
