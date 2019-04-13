package com.beiing.gifmaker.adapter.for_recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemSupport;

import java.util.List;
/**
 * Created by chenliu on 2016/4/22.<br/>
 * 描述：Grid通用适配器->GridLayoutManager， 添加 header、footer<br/>
 * 支持多布局
 */
public abstract class GridLayoutAdapter<T> extends CommonAdapter<T> {

    private GridSpanSizeLookup mGridSpanSizeLookup;
    private GridLayoutManager gridManager;

    public GridLayoutAdapter(Context context, List<T> datas, ItemSupport<T> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            gridManager = ((GridLayoutManager) manager);
            if (mGridSpanSizeLookup == null) {
                mGridSpanSizeLookup = new GridSpanSizeLookup();
            }
            gridManager.setSpanSizeLookup(mGridSpanSizeLookup);
        }
    }

    class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        //如果是header 或 footer就需要占满整个屏幕
        @Override
        public int getSpanSize(int position) {
            if (isHeader(position) || isFooter(position)) {
                return gridManager.getSpanCount();
            }
            return 1;
        }
    }


}
