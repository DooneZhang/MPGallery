package com.beiing.gifmaker.adapter.for_recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.beiing.gifmaker.adapter.ViewHolder;
import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemSupport;

import java.util.List;
/**
 * Created by chenliu on 2016/4/22.<br/>
 * 描述：瀑布流通用适配器->StaggeredGridLayoutManager， 添加 header、footer<br/>
 * 支持多布局
 */
public abstract class StaggeredGridLayoutAdapter<T> extends CommonAdapter<T> {

    public StaggeredGridLayoutAdapter(Context context, List<T> datas, ItemSupport<T> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return true;
        }
        return false;
    }

    protected void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position) || isFooter(position)) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
    }
}
