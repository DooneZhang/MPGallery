package com.beiing.gifmaker.adapter.for_recyclerview.support;

public interface OnLoadMoreListener{
    /**
     * 加载更多前回调，比如显示Footer的操作
     */
    void onStart();

    /**
     * 加载更多业务处理，如网络请求数据
     */
    void onLoadMore();

}
