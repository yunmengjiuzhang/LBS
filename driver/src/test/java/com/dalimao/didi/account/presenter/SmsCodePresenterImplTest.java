package com.dalimao.didi.account.presenter;

import com.dalimao.didi.common.http.OkHttpClientImpl;
import com.dalimao.didi.account.model.AccountManagerImpl;
import com.dalimao.didi.account.ui.ISmsCodeView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by liuguangli on 17/3/5.
 */
public class SmsCodePresenterImplTest {
    private SmsCodePresenterImpl presenter;
    @Before
    public void setUp() throws Exception {

        presenter = new SmsCodePresenterImpl(new ISmsCodeView() {
            @Override
            public void showLoading() {

            }

            @Override
            public void showError() {

            }

            @Override
            public void showSendState(boolean suc) {

            }

            @Override
            public void showVerifyState(boolean suc) {

            }


        },
          new AccountManagerImpl(OkHttpClientImpl.getInstance())
        );

        presenter.subscribe();
    }

    @After
    public void tearDown() throws Exception {

        presenter.unSubscribe();
    }

    @Test
    public void getSmsCode() throws Exception {

        presenter.getSmsCode("15919496914");
    }

    @Test
    public void showLoading() throws Exception {

    }

    @Test
    public void showError() throws Exception {

    }

    @Test
    public void showVerifySuc() throws Exception {

    }

    @Test
    public void subscribe() throws Exception {

    }

    @Test
    public void unSubscribe() throws Exception {

    }

}