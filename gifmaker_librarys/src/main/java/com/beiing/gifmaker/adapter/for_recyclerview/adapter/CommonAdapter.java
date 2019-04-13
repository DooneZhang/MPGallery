package com.beiing.gifmaker.adapter.for_recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beiing.gifmaker.adapter.ViewHolder;
import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemSupport;
import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemType;
import com.beiing.gifmaker.adapter.for_recyclerview.support.OnItemClickListener;

import java.util.List;

/**
 * Created by chenliu on 2016/4/22.<br/>
 * 描述：基础通用适配器->支持LinearLayoutManager， 添加 header、footer<br/>
 * 支持多布局
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    //-------添加header、footer处理-------
    protected int headerViewRes;
    protected int footerViewRes;
    protected boolean hasHeader = false;
    protected boolean hasFooter = false;

    //----支持多布局处理---
    protected ItemSupport<T> mMultiItemTypeSupport;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public CommonAdapter(Context context, List<T> datas, @NonNull ItemSupport<T> multiItemTypeSupport) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mMultiItemTypeSupport = multiItemTypeSupport;

        if (mMultiItemTypeSupport == null)
            throw new IllegalArgumentException("the mMultiItemTypeSupport can not be null.");
    }

    public List<T> getDatas() {
        return mDatas;
    }


    public boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isFooter(int position) {
        if (hasHeader()) {
            return hasFooter() && position == mDatas.size() + 1;
        } else {
            return hasFooter() && position == mDatas.size();
        }
    }

    public int getHeaderViewRes() {
        return headerViewRes;
    }

    public int getFooterViewRes() {
        return footerViewRes;
    }


    public void setHeaderView(int headerViewRes) {
        if (headerViewRes != 0) {
            if (!hasHeader()) {
                this.headerViewRes = headerViewRes;
                this.hasHeader = true;
                notifyItemInserted(0);
            } else {
                this.headerViewRes = headerViewRes;
                notifyDataSetChanged();
            }
        }
    }

    public void setFooterView(int footerViewRes) {
        if (footerViewRes != 0) {
            if (!hasFooter()) {
                this.footerViewRes = footerViewRes;
                this.hasFooter = true;
                if (hasHeader()) {
                    notifyItemInserted(mDatas.size() + 1);
                } else {
                    notifyItemInserted(mDatas.size());
                }
            } else {
                this.footerViewRes = footerViewRes;
                notifyDataSetChanged();
            }
        }
    }

    public void removeHeaderView() {
        if (hasHeader) {
            this.hasHeader = false;
            notifyItemRemoved(0);
        }
    }

    public void removeFooterView() {
        if (hasFooter) {
            this.hasFooter = false;
            if (hasHeader()) {
                notifyItemRemoved(mDatas.size() + 1);
            } else {
                notifyItemRemoved(mDatas.size());
            }
        }
    }


    public boolean hasHeader() {
        return hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }


    /**
     * 只能添加一个headerview
     **/
    public int getHeaderCount() {
        return hasHeader ? 1 : 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        if (mMultiItemTypeSupport != null) {
            if (hasHeader() && viewType == ItemType.TYPE_HEADER) {
                viewHolder = ViewHolder.get(mContext, null, parent, headerViewRes, -1);
            } else if (hasFooter() && viewType == ItemType.TYPE_FOOTER) {
                viewHolder = ViewHolder.get(mContext, null, parent, footerViewRes, -1);
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
        } else {
            bindItemView(holder, mDatas.get(position - getHeaderCount()));
        }
    }

    /**
     * 考虑header 和 footer
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int count = 0;
        count += (hasHeader() ? 1 : 0);
        count += (hasFooter() ? 1 : 0);
        count += mDatas.size();
        return count;
    }

    //这里只考虑是否是头部或底部
    @Override
    public int getItemViewType(int position) {
        int itemType = -1;
        if (mMultiItemTypeSupport != null) {
            int size = mDatas.size();
            if (hasHeader()) {
                if (position == 0) {
                    itemType = ItemType.TYPE_HEADER;
                } else {
                    if (position == size + 1) {
                        itemType = ItemType.TYPE_FOOTER;
                    } else {
                        itemType = mMultiItemTypeSupport.getItemViewType(position - getHeaderCount(), mDatas.get(position - getHeaderCount()));
                    }
                }
            } else {
                if (position == size) {
                    itemType = ItemType.TYPE_FOOTER;
                } else {
                    itemType = mMultiItemTypeSupport.getItemViewType(position, mDatas.get(position));
                }
            }
        } else {
            itemType = super.getItemViewType(position);
        }
        return itemType;
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected T getItemByPosition(int position) {
        int size = mDatas.size();
        if (hasHeader()) {
            return mDatas.get(position - getHeaderCount());
        } else {
            return mDatas.get(position);
        }
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    /**
     * 设置点击事件
     *
     * @param parent
     * @param viewHolder
     * @param viewType
     */
    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    mOnItemClickListener.onItemClick(parent, v, mDatas.get(position - getHeaderCount()), position - getHeaderCount());
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("CommonAdapter", "onLongClick");
                if (mOnItemClickListener != null) {
                    int position = getPosition(viewHolder);
                    return mOnItemClickListener.onItemLongClick(parent, v, mDatas.get(position - getHeaderCount()), position - getHeaderCount());
                }
                return true;
            }
        });
    }

    //绑定数据
    protected abstract void bindItemView(ViewHolder holder, T t);

    //------这两个方法不是必须的，所以父类写个空方法
    protected void bindFooterView(ViewHolder holder) {

    }

    protected void bindHeaderView(ViewHolder holder) {

    }


}
