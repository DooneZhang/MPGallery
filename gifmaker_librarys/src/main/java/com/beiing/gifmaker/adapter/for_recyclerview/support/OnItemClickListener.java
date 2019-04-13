package com.beiing.gifmaker.adapter.for_recyclerview.support;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public interface OnItemClickListener<T> {
    void onItemClick(@NonNull ViewGroup parent, @NonNull View view, T t, int position);

    boolean onItemLongClick(@NonNull ViewGroup parent, @NonNull View view, T t, int position);
}