package com.hr.han.utils.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangjutao on 16/8/2.
 */
public class BitmapUtils {

    /**
     * 图像背景圆角处理
     * bitmap要处理的图片 roundPx 图片弯角的圆度一般是5到10之间
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        // 创建与原图大小一样的bitmap文件，Config.ARGB_8888根据情况可以改用其它的
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 实例画布，绘制的bitmap将保存至output中
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;//写自己需要的颜色值
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        bitmap = null;
        return output;
    }

    /**
     * bitmap缩放
     * width要缩放的宽度 height要缩放的高度
     */
    public static Bitmap getBitmapDeflation(Bitmap bitmap, int width, int height, boolean recycle) {

        if (null == bitmap) {

            return null;

        }
        float scaleWidth = 0f;
        float scaleHeight = 0f;
        // 获取bitmap宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 计算缩放比,图片的宽高小于指定的宽高则不缩放
        if (width < bitmapWidth) {
            scaleWidth = ((float) width) / bitmapWidth;
        } else {
            scaleWidth = 1.00f;
        }
        if (height < bitmapHeight) {
            scaleHeight = ((float) height) / bitmapHeight;
        } else {
            scaleHeight = 1.00f;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        if (recycle && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = null;

        return newBitmap;
    }

    /**
     * 方法概述：进入图片的大小与质量压缩，用于区分大小图片
     */
    public static Bitmap getCompressedImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        newOpts.inPurgeable = true;
        newOpts.inJustDecodeBounds = true;
        FileInputStream is = null;
        try {
            is = new FileInputStream(srcPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 130f;// 这里设置高度为800f
        float ww = 130f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (bitmap != null) {
            return compressImageSize(bitmap, 8);// 压缩好比例大小后再进行质量压缩
        }
        return null;
    }

    /**
     * 方法概述：图片质量压缩
     */
    protected static Bitmap compressImageSize(Bitmap image, int size) {
        if (image == null)
            return image;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里10表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1000 > size && options / 3 > 0) { // 循环判断如果压缩后图片是否大于10kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= options / 3;// 每次都减少30%
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 方法概述：保存图片
     */
    public static String saveBitmapWithName(String path, String bitName, Bitmap mBitmap) {
        File f = new File(path + bitName + ".png");
        String url = path + bitName + ".png";
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("文件创建出错");
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            System.out.println(" 创建文件流失败");
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 方法概述：根据传入参数保存图片
     */
    public static boolean saveImageTo(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 切成圆形
     * @param bitmap
     * @return
     */
    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
