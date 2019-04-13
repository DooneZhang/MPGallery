package net.bither.util;
/**
 * Created by ZhangDong on 2017/10/9.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static cn.mepstudio.mpgallery.compressor.CompressActivity.SIZE;
import static cn.mepstudio.mpgallery.compressor.CompressActivity.QUALITY;


/**
 * JNI图片压缩工具类
 */
 public class NativeUtil {

    public final static int SIZE_1KB = 1024;
    public final static int SIZE_1MB = SIZE_1KB * 1024;
    public static int w,h;
    public static int QUALITY_DEFAULT = SIZE;
    public static void compressBitmap(Bitmap bitmap, String filePath) {
        compressBitmap(bitmap, filePath, SIZE_1MB, QUALITY_DEFAULT);
    }

    public static void compressBitmap(Bitmap bitmap, String filePath, int maxByte, int quality) {

        int resultW;
        w = bitmap.getWidth();
        int resultH;
        h = bitmap.getHeight();

        float ratio = getRatioSize(w, h, quality);

        if(quality==0)
        {
            resultW = w;
            resultH = h;
        }else {
            resultW = Math.round(w / ratio);
            resultH = Math.round(h / ratio);
        }
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(resultW, resultH, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, resultW, resultH);
        canvas.drawBitmap(bitmap, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = QUALITY;//100不压缩品质
        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length > maxByte) {
            // 重置baos即清空baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
      // JNI保存图片到SD卡 这个关键
        NativeUtil.saveBitmap(result, options, filePath, true);
      // 释放Bitmap
        if (!result.isRecycled()) {
            result.recycle();
            result = null;
        }
    }
    /**
     * 计算缩放比例
     */
    private static float getRatioSize(int w, int h, int qualityH) {
        float ratio;
        //如过qualityH！=0即用户选择原改变尺寸压缩，进行缩放
            if (w > h) {
                ratio = h * 100.00f / qualityH / 100f;
            } else {
                ratio = w * 100.00f / qualityH / 100f;
            }
            if (ratio <= 0) ratio = 1;
            return ratio;
    }

    /**
     *
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 调用native方法
     */
    private static void saveBitmap(Bitmap bitmap, int quality, String fileName, boolean optimize) {
        compressBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), quality, fileName.getBytes(), optimize);
    }

    /**
     * 调用底层 bitherlibjni.c中的方法
     */
    private static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes, boolean optimize);

    /**
     * 加载lib下两个so文件
     */
    static {
        System.loadLibrary("jpegbither");
        System.loadLibrary("bitherjni");
    }

}