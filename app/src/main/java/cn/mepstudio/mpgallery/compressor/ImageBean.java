package cn.mepstudio.mpgallery.compressor;

/**
 * Created by ZhangDong on 2017/10/7.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */

class ImageBean {
    private String originArg;
    private String thumbArg;
    private String image;

    ImageBean(String originArg, String thumbArg, String image) {
        this.originArg = originArg;
        this.thumbArg = thumbArg;
        this.image = image;
    }

    String getOriginArg() {
        return originArg;
    }

    public void setOriginArg(String originArg) {
        this.originArg = originArg;
    }

    String getThumbArg() {
        return thumbArg;
    }

    public void setThumbArg(String thumbArg) {
        this.thumbArg = thumbArg;
    }

    String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
