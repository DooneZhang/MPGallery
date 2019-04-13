package cn.mepstudio.mpgallery.album.base;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.mepstudio.mpgallery.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rick Ge on 2016/12/20.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected ArrayList<T> mItems;

    public int BEHAVIOR_MODE;

    public static final int NEITHER = 0;
    public static final int ONLY_HEADER = 1;
    public static final int ONLY_FOOTER = 2;
    public static final int BOTH_HEADER_FOOTER = 3;

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_HEADER = -1;
    public static final int VIEW_TYPE_FOOTER = -2;

    public static final int STATE_HIDE = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_NO_MORE = 3;
    public static final int STATE_LOAD_ERROR = 4;

    private int mState;
    protected View mHeaderView;

    private OnLoadingHeaderListener onLoadingHeaderListener;

    // 暴露给外部的接口
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    // View.OnClickListener的实现类
    private OnClickListener onClickListener;
    // View.OnLongClickListener 的实现类
    private OnLongClickListener onLongClickListener;

    public BaseRecyclerAdapter(Context context, int mode) {
        this.mContext = context;
        mItems = new ArrayList<>();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        BEHAVIOR_MODE = mode;
        mState = STATE_HIDE;
        initListener();
    }

    private void initListener() {
        onClickListener = new OnClickListener();
        onLongClickListener = new OnLongClickListener();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderListener != null)
                    return onLoadingHeaderListener.onCreateHeaderViewHolder(parent);
                else
                    throw new IllegalArgumentException("you have to impl the interface when using this viewType");
            case VIEW_TYPE_FOOTER:
                return new FooterViewHolder(mLayoutInflater.inflate(R.layout.album_recycler_footer_view, parent, false));
            default:
                final RecyclerView.ViewHolder holder = onCreateNormalViewHolder(parent, viewType);
                if (holder != null) {
                    holder.itemView.setTag(holder);
                    holder.itemView.setOnLongClickListener(onLongClickListener);
                    holder.itemView.setOnClickListener(onClickListener);
                }
                return holder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderListener != null)
                    onLoadingHeaderListener.onBindHeaderViewHolder(holder, position);
                break;
            case VIEW_TYPE_FOOTER:
                onBindFooterViewHolder(holder);
                break;
            default:
                onBindNormalViewHolder(holder, mItems.get(getIndex(position)), position);
                break;
        }

    }

    protected abstract RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindNormalViewHolder(RecyclerView.ViewHolder holder, T item, int position);

    private void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        FooterViewHolder fvh = (FooterViewHolder) holder;
        fvh.itemView.setVisibility(View.VISIBLE);
        switch (mState) {
            case STATE_LOADING:
                fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_loading));
                fvh.pb_footer.setVisibility(View.VISIBLE);
                break;
            case STATE_NO_MORE:
                fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_no_more));
                fvh.pb_footer.setVisibility(View.GONE);
                break;
            case STATE_LOAD_ERROR:
                fvh.tv_footer.setText(mContext.getResources().getString(R.string.state_load_error));
                fvh.pb_footer.setVisibility(View.GONE);
                break;
            case STATE_HIDE:
                fvh.itemView.setVisibility(View.GONE);
                break;
        }
    }

    private int getIndex(int position) {
        return BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER ? position - 1 : position;
    }

    @Override
    public int getItemCount() {
        if (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE == ONLY_HEADER) {
            return mItems.size() + 1;
        } else if (BEHAVIOR_MODE == BOTH_HEADER_FOOTER) {
            return mItems.size() + 2;
        } else return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && (BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER))
            return VIEW_TYPE_HEADER;
        if (position + 1 == getItemCount() && (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER))
            return VIEW_TYPE_FOOTER;
        else return VIEW_TYPE_NORMAL;
    }

    public int getCount() {
        return mItems.size();
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pb_footer)
        ProgressBar pb_footer;
        @BindView(R.id.tv_footer)
        TextView tv_footer;

        public FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public final View getHeaderView() {
        return this.mHeaderView;
    }

    public final void setHeaderView(View view) {
        this.mHeaderView = view;
    }

    public void setState(int state) {
        if (this.mState != state) {
            this.mState = state;
            updateItem(getItemCount() - 1);
        }
    }

    public void updateItem(final int position) {
        if (getItemCount() > position) {

            //notifyItemChanged(position);

            // Cannot call this method in a scroll callback.
            // Scroll callbacks might be run during a measure & layout pass where you cannot change the RecyclerView data.
            // Any method call that might change the structure of the RecyclerView or the adapter contents should be postponed to the next frame.


            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    notifyItemChanged(position);
                }
            };
            handler.post(r);
        }
    }

    public final void resetItem(List<T> items) {
        if (items != null) {
            clear();
            addAll(items);
        }
    }

    public void addAll(final List<T> items) {
        if (items != null) {
            int positionStart = this.mItems.size();
            if(BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER){
                positionStart += 1;
            }
            this.mItems.addAll(items);
            notifyItemRangeInserted(positionStart, items.size());
        }
    }

    public final void addItem(T item) {
        if (item != null) {
            this.mItems.add(item);
            notifyItemChanged(mItems.size());
        }
    }

    public void replaceItem(int position, T item) {
        if (item != null) {
            this.mItems.set(getIndex(position), item);
            notifyItemChanged(position);
        }
    }

    public final T getItem(int position) {
        int p = getIndex(position);
        if (p < 0 || p >= mItems.size())
            return null;
        return mItems.get(p);
    }

    public void clear() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    public void setOnLoadingHeaderListener(OnLoadingHeaderListener listener) {
        onLoadingHeaderListener = listener;
    }

    /**
     * for load header view
     */
    public interface OnLoadingHeaderListener {
        RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

        void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);
    }

    private class OnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        }
    }

    private class OnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            if(onItemLongClickListener != null){
                RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
                onItemLongClickListener.onLongClick(holder.getAdapterPosition());
                return true;
            }
            return false;
        }
    }


    /**
     * 添加条目点击事件
     *
     * @param onItemClickListener the RecyclerView item click listener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加条目长点击事件
     *
     * @param onItemLongClickListener the RecyclerView item long click listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public interface OnItemLongClickListener {
        void onLongClick(int position);
    }

}