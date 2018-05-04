package com.max.app.kotlincrm.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class PickImageUtil {

	private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	private static ImageView iv_image;
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

	private File tempFile;
	private static PickImageUtil mPickImageUtil;
	static Activity fromActivity;
	public static synchronized PickImageUtil getInstance(Activity activity){
		fromActivity = activity;
		if(mPickImageUtil == null){
			mPickImageUtil = new PickImageUtil();
		}
		return mPickImageUtil;
	}
	
	public void showPickImageDialog(ImageView imageview){
		iv_image = imageview;
		new AlertDialog.Builder(fromActivity)
		.setTitle("请选择获取图片方式")
		.setNegativeButton("拍  照", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				camera();
			}
		})
		.setPositiveButton("图  库", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gallery();
			}
		})
		.show();
	}

	/*
	 * 从相册获取
	 */
	public void gallery() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		fromActivity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/*
	 * 从相机获取
	 */
	public void camera() {
		camera(null);
	}
	public void camera(IGetSelectBitmap callback) {
		setCallback(callback);
		// 激活相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					PHOTO_FILE_NAME);
			// 从文件中创建uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		fromActivity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
	}

	/*
	 * 剪切图片
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 350);
		intent.putExtra("outputY", 350);

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		fromActivity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/*
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	//要用Activity的	onActivityResult 调用这个方法。
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_CAREMA) {
			// 从相机返回的数据
			if (hasSdcard()) {
				if (resultCode == Activity.RESULT_OK) {
					if (tempFile != null) {
						doFinish(tempFile);
					}
				} else {
					if (tempFile != null && tempFile.exists()) {
						tempFile.delete();
					}
				}
			} else {
				Toast.makeText(fromActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public interface IGetSelectBitmap {
        void getSelImageBitmap(Bitmap bitmap, int id);
        void getSelImageFile(File file);
    }
    IGetSelectBitmap callback;
    public void doFinish(Bitmap bitmap, int id) {
		if(callback != null){
			callback.getSelImageBitmap(bitmap, id);
		}
	}
    public void doFinish(File file) {
		if(callback != null){
			callback.getSelImageFile(file);
		}
	}
    public void setCallback(IGetSelectBitmap callback) {
        this.callback = callback;
    }


}
