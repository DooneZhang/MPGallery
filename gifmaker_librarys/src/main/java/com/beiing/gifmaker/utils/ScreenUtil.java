package com.beiing.gifmaker.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by chenliu on 2016/5/9.<br/>
 * 描述：
 * </br>
 */
public class ScreenUtil {

    /**

     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int dp2px(Context context, float dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }

    /**
     *
     * @param activity
     * @param alpha
     */
    public static void setWindowAlpha(Activity activity, float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }








}
