package com.beiing.gifmaker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.bumptech.glide.gifencoder.AnimatedGifEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by chenliu on 2016/7/1.<br/>
 * 描述：
 * </br>
 */
public class GifMakeUtil {

    public static String createGif(String filename, List<String> paths, int fps, int width, int height) throws IOException {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
            localAnimatedGifEncoder.start(baos);//start
            localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
            localAnimatedGifEncoder.setDelay(fps);
            if (paths.size() > 0) {
                for (int i = 0; i < paths.size(); i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
                    Bitmap resizeBm = ImageUtil.resizeImage(bitmap, width, height);
                    localAnimatedGifEncoder.addFrame(resizeBm);
                }
            }
            localAnimatedGifEncoder.finish();//finish

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/魔方相册/故事相册");
            if (!file.exists()) file.mkdir();
            String path = Environment.getExternalStorageDirectory().getPath() + "/魔方相册/故事相册/" + filename + ".gif";
            FileOutputStream fos = new FileOutputStream(path);
            baos.writeTo(fos);
            baos.flush();
            fos.flush();
            baos.close();
            fos.close();

        return path;
    }

}
