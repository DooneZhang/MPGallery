package cn.mepstudio.mpgallery.album.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by Rick Ge on 2017/1/12.
 */

public class PreviewViewPager extends ViewPager {
    private boolean mScrolling;
    private float touchDownY;
    private int mTouchSlop;

    public PreviewViewPager(Context context) {
        this(context, null);
    }

    public PreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    // 由于ViewPager Adapter的Item都有添加点击事件，为了避免上下滑动和点击事件的冲突做如下处理。
    // 上下滑动时拦截事件，只有真正的点击时才执行Item的点击事件。
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isBeingDragged = super.onInterceptTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = ev.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownY - ev.getY()) >= mTouchSlop) {
                    mScrolling = true;
                }
                else{
                    mScrolling = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }

        return isBeingDragged ? isBeingDragged : mScrolling;
    }

}
