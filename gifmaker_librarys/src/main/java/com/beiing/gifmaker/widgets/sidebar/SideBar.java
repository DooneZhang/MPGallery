package com.beiing.gifmaker.widgets.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author Beiing
 */
public class SideBar extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    /**
     * 显示的字符集--默认
     */
    private String[] characters = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"};

    /**
     * 选中标识
     */
    private int choose = -1;

    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 选中提示框
     */
    private TextView mTextDialog;

    /**
     * 字体大小
     */
    private int textSize = (int) (14 * getContext().getResources().getDisplayMetrics().density);

    /**
     * 默认字体颜色
     */
    private int defaultTextColor = Color.BLACK;

    /**
     * 选中字体颜色
     */
    private int selectedTextColor = Color.BLUE;

    /**
     * 触摸时背景颜色
     */
    private int touchedBgColor = Color.TRANSPARENT;
    ;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context) {
        this(context, null, 0);
    }

    /**
     * 设置显示字符集
     *
     * @param chs
     */
    public SideBar setCharacters(String[] chs) {
        if (chs != null && chs.length != 0)
            this.characters = chs;
        return this;

    }

    /**
     * 设置提示框
     */
    public SideBar setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
        return this;
    }

    /**
     * 设置选中时显示的字体颜色
     *
     * @param color
     */
    public SideBar setSelectedTextColor(int color) {
        if (color != 0)
            this.selectedTextColor = color;
        return this;
    }

    /**
     * 设置默认显示的字体颜色
     *
     * @param color
     */
    public SideBar setDefaultTextColor(int color) {
        if (color != 0)
            this.defaultTextColor = color;
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public SideBar setTextSize(int textSize) {
        if (textSize != 0)
            this.textSize = textSize;
        return this;
    }

    /**
     * 设置触摸时背景色
     *
     * @param color
     */
    public SideBar setTouchedBgColor(int color) {
        if (color != 0)
            this.touchedBgColor = color;
        return this;
    }

    /**
     * 初始化
     */
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / characters.length;// 获取每一个字母的高度

        for (int i = 0; i < characters.length; i++) {
            if (!isInEditMode()) {
                paint.setColor(defaultTextColor);
            }

            paint.setTextSize(textSize);
            // 选中的状态
            if (i == choose) {
                paint.setColor(selectedTextColor);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(characters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(characters[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
            paint.setAntiAlias(true);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * characters.length);// 点击y坐标所占总高度的比例*characters数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);// 透明
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundColor(touchedBgColor);// 选中时设置背景色
                if (oldChoose != c) {
                    if (c >= 0 && c < characters.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(characters[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(characters[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}