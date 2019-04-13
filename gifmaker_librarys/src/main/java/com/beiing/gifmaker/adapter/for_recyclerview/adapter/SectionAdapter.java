package com.beiing.gifmaker.adapter.for_recyclerview.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.beiing.gifmaker.adapter.ViewHolder;
import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemSupport;
import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemType;
import com.beiing.gifmaker.adapter.for_recyclerview.support.SectionSupport;

import java.util.List;

/**
 * Created by chenliu on 2016/4/25.<br/>
 * 描述：待完成
 * </br>
 */
public abstract class SectionAdapter<T extends SectionSupport> extends CommonAdapter<T> {


    protected int sectionViewRes;

    protected int sectionCount;

    public SectionAdapter(Context context, List datas, ItemSupport<T> multiItemTypeSupport) {
        super(context, datas, multiItemTypeSupport);
    }

    public int getSectionViewRes() {
        return sectionViewRes;
    }

    public void setSectionViewRes(int sectionViewRes) {
        this.sectionViewRes = sectionViewRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if(mMultiItemTypeSupport != null){
            if (hasHeader() && viewType == ItemType.TYPE_HEADER) {
                viewHolder = ViewHolder.get(mContext, null, parent, headerViewRes, -1);
            } else if (hasFooter() && viewType == ItemType.TYPE_FOOTER) {
                viewHolder = ViewHolder.get(mContext, null, parent, footerViewRes, -1);
            } else if(viewType == ItemType.TYPE_SECTION){
                viewHolder = ViewHolder.get(mContext, null, parent, sectionViewRes, -1);
            } else {
                int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
                viewHolder = ViewHolder.get(mContext, null, parent, layoutId, -1);
                setListener(parent, viewHolder, viewType);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        if (getItemViewType(position) == ItemType.TYPE_HEADER) {
            bindHeaderView(holder);
        } else if (getItemViewType(position) == ItemType.TYPE_FOOTER) {
            bindFooterView(holder);
        } else if(getItemViewType(position) == ItemType.TYPE_SECTION){
            bindSectionView(holder, position);
        } else {
            bindItemView(holder, mDatas.get(position - getHeaderCount()));
        }
    }


    //这里只考虑是否是头部或底部
    @Override
    public int getItemViewType(int position) {
        int itemType = -1;
        if (mMultiItemTypeSupport != null){
            int size = mDatas.size();
            if (hasHeader()) {
                if (position == 0) {
                    itemType =  ItemType.TYPE_HEADER;
                } else {
                    if (position == size + 1) {
                        itemType =  ItemType.TYPE_FOOTER;
                    } else{
                        itemType =  mMultiItemTypeSupport.getItemViewType(position - getHeaderCount(), mDatas.get(position - getHeaderCount()));
                    }
                }
            } else {
                if (position == size) {
                    itemType =  ItemType.TYPE_FOOTER;
                } else {
                    itemType =  mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
                }
            }
        } else {
            itemType = super.getItemViewType(position);
        }
        return itemType;
    }


    protected  abstract void bindSectionView(ViewHolder holder, int position);



}



















