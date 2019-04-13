package com.beiing.gifmaker.bean;

/**
 * Created by chenliu on 2016/7/1.<br/>
 * 描述：
 * </br>
 */
public class GifImageFrame {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_ICON = 1;

    private String path;
    private int type;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
