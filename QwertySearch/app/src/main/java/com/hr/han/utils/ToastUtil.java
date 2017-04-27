package com.hr.han.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.hr.han.base.BaseActivity;


/**
 * ToastUtil
 * Created by zhangjutao on 2015/7/13.
 */
public class ToastUtil {

    private static boolean mSnackBarShowing = false;


    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Throwable throwable) {
        showToast(throwable.toString(), Toast.LENGTH_LONG);
    }

    public static void showToast(@StringRes int resId) {
        showToast(UIUtil.getString(resId), Toast.LENGTH_SHORT);
    }

    private static void showToast(String msg, int duration) {
        try {
            Toast.makeText(UIUtil.getContext(), msg, duration).show();
        } catch (Exception e) {
            showSafeToast(msg, duration);
        }
    }

    private static void showSafeToast(final String str, final int duration) {
        if (UIUtil.isRunInMainThread()) {
            showToast(str);
        } else {
            UIUtil.post(new Runnable() {
                @Override
                public void run() {
                    showToast(str, duration);
                }
            });
        }
    }

    /**
     * 显示短土司
     *
     * @param msg 消息内容
     */
    public static void showSnackBar(BaseActivity activity, String msg, View v) {
        showSnackBar(activity, msg, Snackbar.LENGTH_SHORT,v);
    }


    /**
     * 子线程发送土司
     *
     * @param str 土司内容
     */
    public static void showSafeToast(final String str) {
        if (UIUtil.isRunInMainThread()) {
            showToast(str);
        } else {
            UIUtil.post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
        }
    }

    /**
     * 发送长土司
     *
     * @param msg 土司内容
     */
    public static void showLongToast(String msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    private static void showSnackBar(BaseActivity activity, String msg, int duration, View v) {
        if (activity == null||v==null) {
            showToast(msg, Toast.LENGTH_SHORT);
            return;
        }

        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            //隐藏输入法
            UIUtil.hideInputMethod(view);
        }

        final Snackbar snackBar = Snackbar.make(v, msg, duration);
        snackBar.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackBar.dismiss();
                mSnackBarShowing = false;
            }
        });
        snackBar.show();
        mSnackBarShowing = true;
    }

    public static boolean isSnackBarShowing() {
        return mSnackBarShowing;
    }
}
