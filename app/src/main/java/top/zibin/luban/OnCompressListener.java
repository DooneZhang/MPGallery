package top.zibin.luban;

import java.io.File;
/**
 * Created by ZhangDong on 2017/10/9.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */
public interface OnCompressListener {

  /**
   * 在启动压缩时启动，在自己的代码中覆盖到句柄。
   */
  void onStart();

  /**
   * 当压缩成功返回时触发，在自己的代码中覆盖到句柄。
   */
  void onSuccess(File file);

  /**
   * 在压缩失败时触发，在自己的代码中覆盖到句柄。
   */
  void onError(Throwable e);
}
