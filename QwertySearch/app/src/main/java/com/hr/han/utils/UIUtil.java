package com.hr.han.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.hr.han.base.BaseActivity;

import java.lang.reflect.Method;

import rx.functions.Action1;


/**
 * UI工具类,对一些界面数据的获取与操作
 * Created by zhangjutao on 16/7/4.
 */
public class UIUtil {

    //屏幕宽度
    public static int mScreenWidth;
    //屏幕高度
    public static int mScreenHeight;



    public static Context getContext() {
        return APPApplication.getApplication();
    }


    //判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        //android.os.Process.myTid()表示调用线程的id
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }


    /**
     * 获取文字
     */
    public static String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgus) {
        return getResources().getString(resId, formatArgus);
    }


    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return ActivityCompat.getDrawable(getContext(), resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public static int getScreenHeight() {
        if (mScreenHeight == 0) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            mScreenHeight = wm.getDefaultDisplay().getHeight();
        }
        return mScreenHeight;
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public static int getScreenWidth() {
        if (mScreenWidth == 0) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            mScreenWidth = wm.getDefaultDisplay().getWidth();
        }
        return mScreenWidth;
    }


    /**
     * 设置指定textView的字体大小，单位sp
     *
     * @param textView textView
     * @param textSize textSize
     */
    public static void setTextSize(TextView textView, int textSize) {
        if (textView != null)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }


    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }


    /**
     * 获取主线程的handler
     */
    public static Handler getHandler() {
        return APPApplication.getMainThreadHandler();
    }

    public static int getMainThreadId() {
        return APPApplication.getMainThreadId();
    }




    /**
     * 显示确定Dialog
     */
    public static void showAlertDialog(final Context context, final String content, final Action1<Integer> action1) {
        runInMainThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context).setTitle("提示").setMessage(content)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }


    /**
     * 显示ProgressDialog
     */
    public static ProgressDialog showProgressDialog(Context context, String tips) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        // 10秒后如果没有被关闭，则自动关闭
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    ToastUtil.showToast("程序异常");
                    dialog.dismiss();
                }
            }
        }, 10000);
        return dialog;
    }


    public static boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w("UIUItile", e);
        }


        return hasNavigationBar;
    }

    /**
     * 沉浸式工具类，判断是底部有虚拟键进行透明设置
     * @param context
     */
    public static void transportStatus(Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (!UIUtil.checkDeviceHasNavigationBar(context)){
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 沉浸式工具类
     * @param context
     * @param view
     */

    public static void transportStatusWithView(Activity context, View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (view!=null){
                view.setVisibility(View.VISIBLE);
            }
            if (!UIUtil.checkDeviceHasNavigationBar(context)){
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    public static void hideInputMethod(final View view) {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(BaseActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}


