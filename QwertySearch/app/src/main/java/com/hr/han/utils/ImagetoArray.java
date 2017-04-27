package com.hr.han.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 图片转换成二进制数组再转化成String
 * @author king
 *
 */
public class ImagetoArray {
	public static String imagetoArray(Bitmap bitmap){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
		return imageString;

	}
	/**
	 *
	 * @param bitmap
	 * @return
	 */
	public static String imagetoArray(String iamge_path){
		//Bitmap bitmap = BitmapFactory.decodeFile(iamge_path);
		Bitmap bitmap = PictureUtil.getSmallBitmap(iamge_path);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String imageString = "";
		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
			byte[] byteArray = byteArrayOutputStream.toByteArray();
			imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
			bitmap.recycle();
			bitmap = null;
		}
		return imageString;
	}
	/**
	 * 用户头像
	 * @param bitmap
	 * @return
	 */
	public static String headimagetoArray(Bitmap bitmap){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream.toByteArray();
		String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
		return imageString;
	}
}
