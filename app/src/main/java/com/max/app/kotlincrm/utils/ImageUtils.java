package com.max.app.kotlincrm.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 */
public class ImageUtils {

    public static Bitmap getSmallBitMap(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 存储Bitmap到SD卡上
     * @param path
     * @param fileName
     * @param bitmap
     * @return
     */
    public static String saveImage2SD(String path, String fileName, Bitmap bitmap){
        FileOutputStream fOut = null;
        String result_path = "";
        String sdpath = "";
        if("".equals(path) || path == null )
            sdpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        else
            sdpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + path + "/";
        File destDir = new File(sdpath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        result_path = sdpath + fileName;
        File f = new File(result_path);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
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
        return result_path;
    }
}
