package com.beiing.gifmaker.gifmake;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.beiing.gifmaker.adapter.ViewHolder;
import com.beiing.gifmaker.adapter.for_recyclerview.adapter.CommonAdapter;
import com.beiing.gifmaker.adapter.for_recyclerview.support.ItemSupport;
import com.beiing.gifmaker.supports.OnClickListener;
import com.beiing.gifmaker.R;
import com.beiing.gifmaker.bean.GifImageFrame;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Created by chenliu on 2016/7/1.<br/>
 * 描述：
 * </br>
 */
public class ImageAdapter extends CommonAdapter<GifImageFrame> {

    public static final int MODE_COMMON = 0;
    public static final int MODE_DELETE = 1;
    private int mode;

    OnClickListener<GifImageFrame> clickListener;

    public ImageAdapter(Context context, List<GifImageFrame> datas) {
        super(context, datas, new ItemSupport<GifImageFrame>() {
            @Override
            public int getLayoutId(int itemType) {
                return R.layout.item_gif_image;

            }
        });
    }

    @Override
    protected void bindItemView(final ViewHolder holder, final GifImageFrame image) {
        ImageView view = holder.getView(R.id.iv_add);
        int type = image.getType();
        if(type == GifImageFrame.TYPE_IMAGE){
            Glide.with(mContext).load(new File(image.getPath())).into(view);
        } else if(type == GifImageFrame.TYPE_ICON){
            view.setImageResource(R.mipmap.icon_plus);
        }

        ImageView icon = holder.getView(R.id.iv_delete);
        if(type == GifImageFrame.TYPE_IMAGE){
            if(mode == MODE_COMMON){
                icon.setVisibility(View.GONE);
            } else if(mode == MODE_DELETE){
                icon.setVisibility(View.VISIBLE);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clickListener != null){
                            clickListener.onClick(holder.getAdapterPosition(), R.id.iv_delete, image);
                        }
                    }
                });
            }
        }else {
            icon.setVisibility(View.GONE);
        }
    }

    public void setClickListener(OnClickListener<GifImageFrame> clickListener) {
        this.clickListener = clickListener;
    }

    public void setMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public int getMode() {
        return mode;
    }
}






