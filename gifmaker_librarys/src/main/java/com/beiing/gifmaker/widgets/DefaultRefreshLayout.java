package com.beiing.gifmaker.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.beiing.gifmaker.R;


/**
 * Created by wei on 2016/3/30.
 */
public class DefaultRefreshLayout extends SwipeRefreshLayout {

    public DefaultRefreshLayout(Context context) {
        super(context);
        init();
    }

    public DefaultRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setColorSchemeResources(R.color.name_color,
                R.color.name_color,
                R.color.name_color,
                R.color.name_color,
                R.color.name_color,
                R.color.name_color);
    }
}
