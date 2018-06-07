package com.dalimao.didi.common.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by liuguangli on 17/2/20.
 */
public class TestOkHttpTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void post() throws Exception {

        TestOkHttp.post();
    }

    @Test
    public void get() throws Exception {

        TestOkHttp.get();
    }
    @Test
    public void getOKHttpImpl() throws Exception {

        TestOkHttp.testOkImpl();
    }
    @Test
    public void testCache() {
        TestOkHttp.testCache(false);
    }

}