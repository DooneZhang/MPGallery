package top.zibin.luban;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static cn.mepstudio.mpgallery.compressor.CompressActivity.user_defined_files;


/**
 * Created by ZhangDong on 2017/10/14.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */
public class Luban implements Handler.Callback {
  private static final String TAG = "Luban";
 // private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

  private static final int MSG_COMPRESS_SUCCESS = 0;
  private static final int MSG_COMPRESS_START = 1;
  private static final int MSG_COMPRESS_ERROR = 2;


  private String mTargetDir;
  //路径列表
  private List<String> mPaths;
  //最小压缩尺寸
  private int mLeastCompressSize;

  private OnCompressListener mCompressListener;

  private Handler mHandler;

  private Luban(Builder builder) {
    this.mPaths = builder.mPaths;
    this.mTargetDir = builder.mTargetDir;
    this.mCompressListener = builder.mCompressListener;
    this.mLeastCompressSize = builder.mLeastCompressSize;

    mHandler = new Handler(Looper.getMainLooper(), this);
  }

  public static Builder with(Context context) {
    return new Builder(context);
  }

  /**
   * 返回一个缓存的音频名称mfile的私有缓存目录。
   *
   * @param context
   *     A context.
   */
  private File getImageFile(Context context, String suffix) {
    //判断 mTargetDir 是否为空
    if (TextUtils.isEmpty(mTargetDir)) {
      //赋值路径的字符串，获取照片储存路径
      mTargetDir = getImageDir(context).getAbsolutePath();
    }
    //照片路径+名字的字符串
    String cacheBuilder = mTargetDir + "/" + "已压缩_"+ System.currentTimeMillis()
            +(TextUtils.isEmpty(suffix) ? ".jpg" : suffix);

    return new File(cacheBuilder);
  }

  /**
   * 返回应用程序的私有缓存目录中具有默认名称的目录
   *用于存储检索的音频。
   *
   * @param context
   *     A context.
   *
   * @see #getImageDir(Context, String)
   */
  @Nullable
  private File getImageDir(Context context) {
    return getImageDir(context,user_defined_files);
  }

  /**
   * 返回应用程序的私有缓存目录中给定名称的目录
   *用于存储检索的媒体和缩略图。
   *
   * @param context
   *     A context.
   * @param cacheName
   *    cacheName的子目录的名称。
   *
   * @see #getImageDir(Context)
   */
  @Nullable

  private File getImageDir(Context context, String cacheName) {
    //定义图片文件
    File ImageDir = context.getExternalCacheDir();
    if (ImageDir != null) {
      ///压缩后的照片起名(path_string,name)
      File result = new File(ImageDir, cacheName);

      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // 文件无法创建目录，或结果存在，但不能目录
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "默认磁盘缓存目录为空");
    }
    return null;
  }

  /**
   * 启动异步压缩线程
   */
  @UiThread private void launch(final Context context) {
    if (mPaths == null || mPaths.size() == 0 && mCompressListener != null) {
      mCompressListener.onError(new NullPointerException("图片不能为空"));
    }

    Iterator<String> iterator = mPaths.iterator();
    while (iterator.hasNext()) {

      final String path = iterator.next();
      if (Checker.isImage(path)) {
        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
          @Override public void run() {
            try {
              mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_START));

              File result = Checker.isNeedCompress(mLeastCompressSize, path) ?
                  new Engine(path, getImageFile(context, Checker.checkSuffix(path))).compress() :
                  new File(path);
              mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_SUCCESS, result));
            } catch (IOException e) {
              mHandler.sendMessage(mHandler.obtainMessage(MSG_COMPRESS_ERROR, e));
            }
          }
        });
      } else {
        mCompressListener.onError(new IllegalArgumentException("can not read the path : " + path));
      }
      iterator.remove();
    }
  }

  /**
   * 开始压缩并返回文件
   */
  @WorkerThread private File get(String path, Context context) throws IOException {
    return new Engine(path, getImageFile(context, Checker.checkSuffix(path))).compress();
  }

  @WorkerThread private List<File> get(Context context) throws IOException {
    List<File> results = new ArrayList<>();
    Iterator<String> iterator = mPaths.iterator();

    while (iterator.hasNext()) {
      String path = iterator.next();
      if (Checker.isImage(path)) {
        results.add(new Engine(path, getImageFile(context, Checker.checkSuffix(path))).compress());
      }
      iterator.remove();
    }
    return results;
  }

  @Override public boolean handleMessage(Message msg) {
    if (mCompressListener == null) return false;

    switch (msg.what) {
      //开始
      case MSG_COMPRESS_START:
        mCompressListener.onStart();
        break;
      //成功
      case MSG_COMPRESS_SUCCESS:
        mCompressListener.onSuccess((File) msg.obj);
        break;
      //错误
      case MSG_COMPRESS_ERROR:
        mCompressListener.onError((Throwable) msg.obj);
        break;
    }
    return false;
  }

  public static class Builder {
    private Context context;
    private String mTargetDir;
    private List<String> mPaths;
    private int mLeastCompressSize = 100;
    private OnCompressListener mCompressListener;

    Builder(Context context) {
      this.context = context;
      this.mPaths = new ArrayList<>();
    }

    private Luban build() {
      return new Luban(this);
    }

    public Builder load(File file) {
      this.mPaths.add(file.getAbsolutePath());
      return this;
    }

    public Builder load(String string) {
      this.mPaths.add(string);
      return this;
    }

    public Builder load(List<String> list) {
      this.mPaths.addAll(list);
      return this;
    }

    public Builder putGear(int gear) {
      return this;
    }

    public Builder setCompressListener(OnCompressListener listener) {
      this.mCompressListener = listener;
      return this;
    }

    public Builder setTargetDir(String targetDir) {
      this.mTargetDir = targetDir;
      return this;
    }

    /**
     * 当原始图像文件大小小于一个值时，不要压缩。
     *
     * @param size 值为 100K
     */
    public Builder ignoreBy(int size) {
      this.mLeastCompressSize = size;
      return this;
    }

    /**
     * 开始用异步方式压缩图像
     */
    public void launch() {
      build().launch(context);
    }

    public File get(String path)
            throws IOException {
      return build().get(path, context);
    }

    /**
     * 开始用同步开始压缩图像
     *
     * @return 图片列表
     */
    public List<File> get() throws IOException {
      return build().get(context);
    }
  }
}