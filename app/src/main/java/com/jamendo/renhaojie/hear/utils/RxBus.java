package com.jamendo.renhaojie.hear.utils;

import java.util.HashMap;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ren Haojie on 2017/2/13.
 */

public class RxBus {
    private static volatile RxBus mInstance;
    private SerializedSubject<Object, Object> mSubject;
    private HashMap<String, CompositeSubscription> mSubsccription;

    private RxBus() {
        mSubject = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    public void post(Object o) {
        mSubject.onNext(o);
    }

    public <T> Observable<T> toObservable(final Class<T> type) {
        return mSubject.ofType(type);
    }

    public <T> Subscription doSubscribe(Class<T> type, Action1<T> next, Action1<Throwable> error) {
        return toObservable(type)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next, error);
    }

    public void addSubscription(Object o, Subscription subscription) {
        if (mSubsccription == null) {
            mSubsccription = new HashMap<>();
        }

        String key = o.getClass().getName();
        if (mSubsccription.get(key) != null) {
            mSubsccription.get(key).add(subscription);
        } else {
            CompositeSubscription compositeSubscription = new CompositeSubscription();
            compositeSubscription.add(subscription);
            mSubsccription.put(key, compositeSubscription);
        }
    }

    public void unSubscribe(Object o) {
        if (mSubsccription == null)
            return;

        String key = o.getClass().getName();
        if (!mSubsccription.containsKey(key))
            return;

        if (mSubsccription.get(key) != null)
            mSubsccription.get(key).unsubscribe();

        mSubsccription.remove(key);
    }
}
