package com.beiing.gifmaker.supports;

/**
 * Created by chenliu on 2016/5/13.<br/>
 * 描述：适配器里用到的点击接口
 * </br>
 */
public interface OnClickListener<T> {
    void onClick(int position, int id, T t);
}