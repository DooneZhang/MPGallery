package com.beiing.gifmaker.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiing.gifmaker.R;


/**
 * Created by chenliu on 2016/5/11.<br/>
 * 描述：控制输入字数限制的输入框
 * </br>
 */
public class NumControlledEditText extends RelativeLayout{

    private EditText editText;
    private TextView textView;

    private int maxNum;//最大可输入字数
    private String editHint;

    public NumControlledEditText(Context context) {
        this(context, null);
    }

    public NumControlledEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumControlledEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(context);

        initAttrs(context, attrs);
        initData();
        initEvent();
    }

    private void initViews(Context context) {
        // 第二个参数表示：布局资源中跟标签内声明的布局参数参考父控件类型
        // 第三个参数：为true时代表将第一个参数中声明的子控件归属到第二个参数对象中，false不归属
        LayoutInflater.from(context).inflate(R.layout.layout_num_controled_edittext, this, true);
        editText = (EditText) findViewById(R.id.et_note);
        textView = (TextView) findViewById(R.id.tv_tip);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumControlledEditText);
        maxNum = a.getInt(R.styleable.NumControlledEditText_maxNum, 50);
        editHint = a.getString(R.styleable.NumControlledEditText_editHint);
        a.recycle();
    }

    private void initData() {
        setHint(editHint);
        setCount(0);
    }

    private void initEvent() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > maxNum){
                    editText.setText(s.subSequence(0, maxNum ));
                    setCount(maxNum);
                    editText.setError("达到输入上限，不能再输入了！");
                    editText.setSelection(editText.getText().toString().length());
                } else {
                    setCount(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void setHint(String hint){
        editHint = hint;
        editText.setHint(hint);
    }

    public void setTip(String tip){
        textView.setText(tip);
    }

    public void setCount(int count){
        textView.setText(count + "/" + maxNum);
    }

    public void setMaxNum(int maxNum){
        this.maxNum = maxNum;
    }

    public void setText(String content){
        if(!TextUtils.isEmpty(content))
            if(!content.equals(editHint)){
                editText.setText(content);
                editText.setSelection(content.length());
            }
    }

    public String getText(){
        return editText.getText().toString();
    }


    public void setEnabled(boolean enabled){
        editText.setEnabled(enabled);
    }
}
