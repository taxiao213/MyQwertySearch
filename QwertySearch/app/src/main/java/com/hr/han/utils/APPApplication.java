package com.hr.han.utils;

import android.app.Application;
import android.os.Handler;


/**
 *
 * Created by zhangjutao on 2017/3/28.
 */

public class APPApplication extends Application {

    /**获取主线程上下文*/
    public static APPApplication mContext;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程
    private static Thread mMainThread = null;
    // 获取到主线程的id
    private static int mMainThreadId;
    //是否打印log日志
    public static boolean IS_LOG_CONTENT = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mMainThreadHandler = new Handler();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        //initHandlerException();
    }


    public static APPApplication getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Thread getmMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }
}
