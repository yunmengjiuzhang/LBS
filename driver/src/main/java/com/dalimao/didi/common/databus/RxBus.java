package com.dalimao.didi.common.databus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuguangli on 17/3/5.
 */

public class RxBus implements DataBus {
    private static volatile RxBus instance;
    private Set<Object> subscribers;
    private Subscription sendSubscription;
    private Subscription chainSubScription;

    private RxBus() {
        subscribers = new CopyOnWriteArraySet<>();
    }

    public void chainProcess(Func1 func) {
       chainSubScription = Observable.just("")
                .subscribeOn(Schedulers.computation())
                .map(func)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object data) {
                        for (Object s : subscribers) {
                            //s.onNext(o);
                            //扫描注解

                            callMethodByAnnotation(s, data);

                        }
                    }
                });
    }


    @Override
    public void send(final Object data) {
        //被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<Subscriber>() {
            @Override
            public void call(Subscriber subscriber1) {
                // 发生事件
                System.out.println("call in tread:" + Thread.currentThread().getName());
                subscriber1.onNext(data);
                if (subscriber1.isUnsubscribed()) {
                    subscriber1.unsubscribe();
                }
              chainSubScription.unsubscribe();
            }
        });

         sendSubscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        synchronized (RxBus.class) {
                            for (Object s : subscribers) {
                                //扫描注解
                                callMethodByAnnotation(s, data);

                            }
                            if (!isUnsubscribed()) {
                                unsubscribe();
                            }
                            sendSubscription.unsubscribe();
                        }

                    }
                });
    }

    @Override
    public synchronized void register(Object subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public synchronized void unRegister(Object subscriber) {
        subscribers.remove(subscriber);
    }

    public static synchronized RxBus getInstance() {

        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }

            }
        }
        return instance;
    }

    private void callMethodByAnnotation(Object target, Object data) {

        if (data == null) {
            return;
        }
        Method[] methodArray = target.getClass().getDeclaredMethods();
        for (int i = 0; i < methodArray.length; i++) {
            try {
                if (methodArray[i].isAnnotationPresent(RegisterBus.class)) {
                    Class paramType = methodArray[i].getParameterTypes()[0];
                    if (data.getClass().getName().equals(paramType.getName())) {

                        methodArray[i].invoke(target, new Object[]{data});

                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
