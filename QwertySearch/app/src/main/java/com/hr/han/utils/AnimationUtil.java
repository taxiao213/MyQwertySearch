package com.hr.han.utils;

import android.animation.Animator;
import android.os.Build;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

/**
 * 动画工具类
 * Created by zhangjutao on 2016/8/13.
 */
public class AnimationUtil {

    /**
     * 设置透明度
     *
     * @param view     view
     * @param duration 动画时长
     */
    public static void setAlpha(View view, float fromValue, float toValue, long duration) {
        setAlpha(view, fromValue, toValue, duration, null);
    }

    public static void setAlpha(View view, float fromValue, float toValue, long duration,
                                Animator.AnimatorListener l) {
        if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setAlpha(fromValue);
            view.animate().alpha(toValue).setDuration(duration).setListener(l).start();
        } else {
            if (l != null) {
                l.onAnimationEnd(null);
            }
        }
    }

    /**
     * 设置旋转动画
     */
    public static void setRotation(View view, float fromRotation, float toRotation, long duration) {
        if (view != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setRotation(fromRotation);
            view.animate().
                    rotation(toRotation)
                    .setDuration(duration)
                    .start();
        }
    }

    /**
     * 平移动画
     *
     * @param view     view
     * @param duration 动画时长
     */
    public static ViewPropertyAnimator setTranslation(View view, float startX, float startY, float toX, float toY,
                                                      long duration, long startDelay) {
        if (view == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setTranslationX(startX);
            view.setTranslationY(startY);
            return view.animate()
                    .translationX(toX)
                    .translationY(toY)
                    .setInterpolator(new FastOutLinearInInterpolator())
                    .setDuration(duration)
                    .setStartDelay(startDelay);
        }

        return null;
    }


    public static void setTranslationX(View view, int startX, int toX, long duration, long startDelay) {
        setTranslation(view, startX, 0, toX, 0, duration, startDelay);
    }

    public static void setTranslationY(View view, int startY, int toY, long duration, long startDelay) {
        setTranslation(view, 0, startY, 0, toY, duration, startDelay);
    }


    public static LayoutAnimationController getAnimationController() {
        int duration = 200;
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addAnimation(animation);

//        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }
}
