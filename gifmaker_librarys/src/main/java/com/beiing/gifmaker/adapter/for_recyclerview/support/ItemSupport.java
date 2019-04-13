package com.beiing.gifmaker.adapter.for_recyclerview.support;

/**
 * Created by chenliu on 2016/4/22.<br/>
 * 描述：提供布局Id 或 布局类型
 */
public abstract class ItemSupport<T> {
    public abstract int getLayoutId(int itemType);

    /**
     * 当只有一种布局时，不用重写该方法，默认返回 TYPE_ONLY_ONE
     *
     * @param position
     * @param t
     * @return
     */
    public int getItemViewType(int position, T t) {
        return ItemType.TYPE_ONLY_ONE;
    }
}