package com.hr.han.utils.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hr.han.utils.UIUtil;


/**
 *
 * Created by zhangjutao on 16/9/22.
 */
public class ImgLoader {

    public static void loadImageFr0mUrl(String url, ImageView iv) {
        Glide.with(UIUtil.getContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv);
    }

    public static void loadImageFromUrlWithHolder(String url, int res, ImageView iv) {
        Glide.with(UIUtil.getContext())
                .load(url)
                .asBitmap()
                .placeholder(res)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(iv);
    }

    public static void loadImageFromUrlNotCache(String url, ImageView iv) {
        Glide.with(UIUtil.getContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv);
    }

    public static void loadImageFromRes(int res, ImageView iv) {
        Glide.with(UIUtil.getContext())
                .load(res)
                .crossFade()
                .into(iv);

    }

    public static void loadImageFromUrlWithError(String url, int res, final ImageView iv) {
        Glide.with(UIUtil.getContext())
                .load(url)
                .error(res)
                .placeholder(res)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        iv.setImageDrawable(resource);
                    }
                });
    }

    public static void loadImageRound(Context context, String url, int holder, ImageView iv, final int dp) {
        Glide.with(UIUtil.getContext())
                .load(url)
                .asBitmap()
                .placeholder(holder)
                .transform(new BitmapTransformation(context) {
                    @Override
                    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                        return BitmapUtils.getRoundedCornerBitmap(toTransform, dp);
                    }

                    @Override
                    public String getId() {
                        return getClass().getName() + Math.round(dp);
                    }
                }).into(iv);
    }
}
