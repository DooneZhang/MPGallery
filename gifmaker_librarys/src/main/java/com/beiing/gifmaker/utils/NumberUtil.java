package com.beiing.gifmaker.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by chenliu on 2016/5/19.<br/>
 * 描述：
 * </br>
 */
public final class NumberUtil {
    private NumberUtil() {

    }

    /**
     * 货币格式化。
     * 给两个小数点，并且隔三个数加个逗号
     *
     * @param money
     * @return
     */
    public static String formatMoney(double money) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(money);
    }


    /**
     * 分转换为元
     *
     * @param fen
     * @return
     */
    public static String fenToYuan(int fen) {
        return String.valueOf(fen / 100f);
    }

    /**
     * 分转换为元
     *
     * @param fen
     * @return
     */
    public static String fenToYuanWithPre(int fen) {
        return String.valueOf("￥" + fen / 100f);
    }

    /**
     *
     */
    public static String yuanWithPre(int yuan) {
        return String.valueOf("￥" + yuan);
    }


    /**
     * 距离处理
     * @param m
     * @return 距离字符串
     */
    public static String getDistanceStr(int m){
        String disStr = "";
        if(m < 1000){
            disStr = "<" + m + "m";
        } else {
            m = m / 1000;
            disStr = "<" + m + "km";
        }
        return disStr;
    }

}









