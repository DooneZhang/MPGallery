package com.beiing.gifmaker.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiing.gifmaker.R;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

//自定义适配器
 public class PictureAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Picture> pictures;

    public PictureAdapter(String[] titles, String[] images, Context context) {
        super();
        pictures = new ArrayList<Picture>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < images.length; i++) {
            Picture picture = new Picture(titles[i], images[i]);
            pictures.add(picture);
        }
    }

    @Override
    public int getCount() {
        if (null != pictures) {
            return pictures.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder2 viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.picture_item, null);
            viewHolder = new ViewHolder2();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.image = convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder2) convertView.getTag();
        }

        String uy = pictures.get(position).getImageId();
        String sdf =uy;

        viewHolder.title.setText(pictures.get(position).getTitle());
       // viewHolder.image.setImageURI(Uri.fromFile(new File(pictures.get(position).getImageId())));

        //  (pictures.get(position).getImageId());
        {
     //       BitmapFactory.decodeFile(pictures.get(position).getImageId())
        }
/*
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        */

        try {
            GifDrawable gifDrawable = new GifDrawable(new File(pictures.get(position).getImageId()));
          //  GifDrawable gifDrawable = new GifDrawable(context.getAssets(), faceFileName[position]);
            viewHolder.image.setImageDrawable(gifDrawable);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //把路径转换为bitmap显示

        return convertView;
    }
    class ViewHolder2 {
        public TextView title;
        public GifImageView image;
    }

}

