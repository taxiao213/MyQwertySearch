package com.hr.han.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 集中管理activity的销毁工作
 * Created by zhangjutao on 16/8/30.
 */
public class ActivityManager {
    private static final String TAG = "ActivityManager";
    private List<Activity> mActivitys = new LinkedList<>();

    private ActivityManager() {

    }

    public void add(Activity activity) {
        mActivitys.add(activity);
    }

    public void remove(Activity activity) {
        mActivitys.remove(activity);
    }

    // 系统关闭时调用，关闭所有activity，置空变量
    public void removeAllActivity() {
        removeAllActivity(null);
//        mActivitys = null;
//        ManagerInstance.instance = null;
    }

    // 关闭除了except之外所有的activity
    public void removeAllActivity(final Activity except) {
        if (mActivitys == null || mActivitys.size() == 1)
            return;

        Observable
                .from(mActivitys)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .filter(new Func1<Activity, Boolean>() {
                    @Override
                    public Boolean call(Activity activity) {
                        return activity != except;
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.exit(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        mActivitys.remove(activity);
                        activity.finish();
                    }
                });
    }

    public static ActivityManager getInstance() {
        return ManagerInstance.instance;
    }

    private static class ManagerInstance {
        static ActivityManager instance = new ActivityManager();
    }
}
