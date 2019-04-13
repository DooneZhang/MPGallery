package com.beiing.gifmaker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * “无限长”ListView
 */
public class MultiListView extends ListView {
    public MultiListView  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MultiListView  (Context context) {
        super(context);
    }
    public  MultiListView  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:  //禁止滑动
                return true ;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
