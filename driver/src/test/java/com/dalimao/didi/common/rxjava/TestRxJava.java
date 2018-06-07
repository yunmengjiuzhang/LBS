package com.dalimao.didi.common.rxjava;

/**
 * Created by liuguangli on 17/3/5.
 */
import com.dalimao.didi.common.databus.RxBus;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TestRxJava {
    @Test
    public void testSubscribe() {

        //观察者／订阅者
        final Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted in tread:" + Thread.currentThread().getName());

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError in tread:" + Thread.currentThread().getName());
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext in tread:" + Thread.currentThread().getName());
                System.out.println(s);
            }
        };

        //被观察者

        Observable observable = Observable.create(new Observable.OnSubscribe<Subscriber>() {
            @Override
            public void call(Subscriber subscriber1) {
                // 发生事件
                System.out.println("call in tread:" + Thread.currentThread().getName());
                subscriber1.onStart();
                subscriber1.onNext("hello world");
                subscriber1.onCompleted();
            }
        });

        //订阅
        observable.subscribe(subscriber);

    }

    @Test
    public void testScheduler(){

        //观察者／订阅者
        final Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted in tread:" + Thread.currentThread().getName());

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError in tread:" + Thread.currentThread().getName());
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext in tread:" + Thread.currentThread().getName());
                System.out.println(s);
            }
        };

        //被观察者

        Observable observable = Observable.create(new Observable.OnSubscribe<Subscriber>() {
            @Override
            public void call(Subscriber subscriber1) {
                // 发生事件
                System.out.println("call in tread:" + Thread.currentThread().getName());
                subscriber1.onStart();
                subscriber1.onNext("hello world");
                subscriber1.onCompleted();

            }
        });


        //订阅
        observable.subscribeOn(Schedulers.immediate())
                  .observeOn(Schedulers.io())
                  .subscribe(subscriber);


    }

    @Test
    public void testRxBus() {
        final RxBus rxBus = RxBus.getInstance();
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public synchronized void onNext(Object o) {

                System.out.println("onNext in tread:" + Thread.currentThread().getName());
                System.out.println(o);
                rxBus.unRegister(this);
            }
        };
        rxBus.register(subscriber);
        //rxBus.send("hello");

        rxBus.chainProcess(new Func1() {
            @Override
            public Object call(Object o) {
                return "{hello}";
            }
        });
    }

}
