package cn.mepstudio.mpgallery.album.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.album.adapter.ImageFolderAdapter;
import cn.mepstudio.mpgallery.album.base.BaseRecyclerAdapter;
import cn.mepstudio.mpgallery.album.bean.ImageFolder;


/**
 * Created by Rick Ge on 2017/1/11.
 */

public class ImageFolderPopupWindow extends PopupWindow implements View.OnAttachStateChangeListener, BaseRecyclerAdapter.OnItemClickListener{
    private ImageFolderAdapter mAdapter;
    private RecyclerView mFolderView;

    private Callback mCallback;

    public ImageFolderPopupWindow(Context context, Callback callback) {
        super(LayoutInflater.from(context).inflate(R.layout.album_popup_window_folder, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallback = callback;

        // init
        setAnimationStyle(R.style.popup_anim_style_alpha);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setFocusable(true);

        // content
        View content = getContentView();
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        content.addOnAttachStateChangeListener(this);

        mFolderView = (RecyclerView) content.findViewById(R.id.rv_popup_folder);
        mFolderView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setAdapter(ImageFolderAdapter adapter) {
        this.mAdapter = adapter;
        mFolderView.setAdapter(adapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        if (mCallback != null) mCallback.onSelect(mAdapter.getItem(position));
        dismiss();
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        if(mCallback != null) mCallback.onShow();
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if(mCallback != null) mCallback.onDismiss();
    }


    public interface Callback {
        void onSelect(ImageFolder imageFolder);

        void onDismiss();

        void onShow();
    }
}
