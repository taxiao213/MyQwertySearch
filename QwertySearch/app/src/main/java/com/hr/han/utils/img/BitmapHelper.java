package com.hr.han.utils.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.hr.han.utils.FileUtil;
import com.hr.han.utils.IOUtil;
import com.hr.han.utils.LogUtil;
import com.hr.han.utils.UIUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * BitmapHelper
 * Created by zhangjutao on 2015/11/4.
 */
public class BitmapHelper {
    private static final String TAG = "BitmapHelper";
    public static List<String> images = new ArrayList<>();
    public static List<String> tempImgs = new ArrayList<>();

    // 对文件中的bitmap进行压缩，之后再保存到该文件
    public static void revisionImageSize(File... files) {
        if (files == null)
            return;

        Bitmap bitmap = null;

        for (File f : files) {
            bitmap = BitmapHelper.revisionImageSize(f);
            saveBitmap2file(bitmap, UIUtil.getContext().getCacheDir() + File.separator + f.getName());
        }

        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    //图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
    public static Bitmap revisionImageSize(File file) {
        if (file == null)
            return null;
        long start = System.currentTimeMillis();
        BufferedInputStream in;
        Bitmap bitmap = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            IOUtil.close(in);
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                    in = new BufferedInputStream(new FileInputStream(file));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    IOUtil.close(in);
                    break;
                }
                i += 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis() - start;
        LogUtil.d(TAG, "耗时: " + time + "毫秒");
        return bitmap;
    }

    // 图片转为文件
    public static boolean saveBitmap2file(Bitmap bmp, String filePath) {
        if (bmp == null)
            return false;
        File uploadCache = new File(filePath);
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    // bitmap转换file
    public static File saveBitmap2file(Bitmap bmp) {
        if (bmp == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
        File file = new File(FileUtil.getCacheDir(), "uploadFile");
        IOUtil.writeTo(stream, file);
        IOUtil.close(stream);
        return file;
    }

    // 根据maxWidth, maxHeight计算最合适的inSampleSize
    public static int getSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        // raw height and width of image
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;

        // calculate best sample size
        int inSampleSize = 0;
        if (rawHeight > maxHeight || rawWidth > maxWidth) {
            float ratioWidth = (float) rawWidth / maxWidth;
            float ratioHeight = (float) rawHeight / maxHeight;
            inSampleSize = (int) Math.min(ratioHeight, ratioWidth);
        }
        inSampleSize = Math.max(1, inSampleSize);

        return inSampleSize;
    }

    // uri转换bitmap
    public static Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(UIUtil.getContext().
                    getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static int getImageSize() {
        return tempImgs.size();
    }

    // uri转换file
    public static File decodeUriAsFile(Uri uri) {
        Bitmap bitmap = decodeUriAsBitmap(uri);
        return saveBitmap2file(bitmap);
    }

    public static void clearImagesAndTempImgs() {
        if (images != null) {
            images.clear();
        }
        if (tempImgs != null) {
            tempImgs.clear();
        }
    }

    public static void clearTempAndAddAllImages() {
        tempImgs.clear();
        tempImgs.addAll(BitmapHelper.images);
    }
}
