package com.hr.han.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureUtil {
	/**
	 * // 图片路径
	 */
	public static String mCurrentPhotoPath;// 图片路径
	public static String mCurrentPhotoPath1;// 图片路径
	public static String mCurrentCarPhotoPath;// 汽车位置图片路径
	public static String mCurrentCarPicPhotoPath;// 汽车图片路径

	/**
	 * 把bitmap转换成String  再压缩
	 *  baos 压缩后的
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);

	}

	/**
	 * 计算图片的缩放值
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 *
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;   //图片压缩之前  true  内存不加载图片，但是可以获取图片的数据
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 240, 320);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;  //经过图片压缩后，在内存中加载图片

		return BitmapFactory.decodeFile(filePath, options);


	}

	/**
	 * 根据路径删除图片
	 *
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 获取保存图片的目录
	 *
	 * @return
	 */
	public static File getAlbumDir() {
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)参数不能为空，返回的路径有可能不存在，必寻File.mkdirs
		File externalStoragePublicDirectory = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if (!externalStoragePublicDirectory.exists()) {
			externalStoragePublicDirectory.mkdirs();
		}
		File dir = new File(externalStoragePublicDirectory, getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 *
	 * @return
	 */
	public static String getAlbumName() {
		String fileNAME = "/honry/picture";
		return "honry";
	}

	/**
	 * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
	 * 照片的命名规则为：honry_20130125_173729.jpg
	 *
	 * @return
	 * @throws IOException
	 */
	@SuppressLint("SimpleDateFormat")
	public static File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "honry_" + timeStamp + ".jpg";
		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	public static File createImageFile1() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "honry1_" + timeStamp + ".jpg";
		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath1 = image.getAbsolutePath();
		return image;
	}
	/*
     *  手机拍照的照片保存到手机，
     */
	@SuppressLint("SimpleDateFormat")
	public static File createCarImageFile() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "car_" + timeStamp + ".jpg";
		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentCarPhotoPath = image.getAbsolutePath();
		return image;
	}
	@SuppressLint("SimpleDateFormat")
	public static File createCarPicImageFile() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "carpic_" + timeStamp + ".jpg";
		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentCarPicPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * 设置头像的圆角
	 *
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

}
