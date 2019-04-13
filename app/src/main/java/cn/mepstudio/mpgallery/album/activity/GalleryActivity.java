package cn.mepstudio.mpgallery.album.activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.album.base.BaseActivity;
import cn.mepstudio.mpgallery.album.widget.PreviewViewPager;

import butterknife.BindView;
import cn.mepstudio.mpgallery.compressor.CompressActivity;

/**
 * Created by Rick Ge on 2017/1/12.
 */

public class GalleryActivity extends BaseActivity {
    public static final String KEY_IMAGE = "images";
    public static final String KEY_POSITION = "position";

    @BindView(R.id.vp_image) PreviewViewPager mImagePager;

    private String[] mImageSources;
    private int mCurPosition;
    private static int select_num;

    @Override
    protected int getContentView() {
        return R.layout.album_activity_image_gallery;

    }

    public static void show(Context context, String[] images, int position) {
        if (images == null || images.length == 0)
            return;
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(KEY_IMAGE, images);
        intent.putExtra(KEY_POSITION, position);
        context.startActivity(intent);
        select_num = position;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        mImageSources = bundle.getStringArray(KEY_IMAGE);
        mCurPosition = bundle.getInt(KEY_POSITION, 0);
        return mImageSources != null;
    }

    @Override
    protected void initWindow() {
        super.initWindow();
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void initData() {
        super.initData();
        int len = mImageSources.length;
        if (mCurPosition < 0 || mCurPosition >= len)
            mCurPosition = 0;

        mImagePager.setAdapter(new ViewPagerAdapter());
        mImagePager.setCurrentItem(mCurPosition);

    }

    private class ViewPagerAdapter extends PagerAdapter {

        private View.OnClickListener mFinishClickListener;

        @Override
        public int getCount() {
            return mImageSources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.album_gallery_page_item, container, false);
            ImageView previewView = view.findViewById(R.id.iv_preview);
            ProgressBar loading =  view.findViewById(R.id.loading);
            ImageView defaultView =  view.findViewById(R.id.iv_default);

            loadImage(mImageSources[position], previewView, defaultView, loading);
            view.setOnClickListener(getFinishListener());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

        final LinearLayout image_menu_up = findViewById(R.id.image_menu_up);
        final LinearLayout image_menu_down = findViewById(R.id.image_menu_down);
        private View.OnClickListener getFinishListener() {

            if (mFinishClickListener == null) {
                mFinishClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(image_menu_up.getVisibility() == View.INVISIBLE)
                        {
                            image_menu_up.setVisibility(View.VISIBLE);
                            image_menu_down.setVisibility(View.VISIBLE);

                        }
                        else{
                            image_menu_up.setVisibility(View.INVISIBLE);
                            image_menu_down.setVisibility(View.INVISIBLE);
                        }

                        Button share_btn =  findViewById(R.id.share);
                        Button edit_btn =  findViewById(R.id.edit);
                        Button more_btn =  findViewById(R.id.more);
                        Button info_btn =  findViewById(R.id.info);
                        Button delete_btn =  findViewById(R.id.delete);
                        Button compress_btn =  findViewById(R.id.compress);
                        Button back_btn =  findViewById(R.id.back);

                        share_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //通过文件路径转成Bitmap并赋值给bitmap

                                String path = mImageSources[select_num];
                                Bitmap bm = BitmapFactory.decodeFile(path);
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, null,null));

                            //    Uri uri = getImageStreamFromExternal(path);;
                                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                                imageIntent.setType("image/jpeg");
                                imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                startActivity(Intent.createChooser(imageIntent, "分享"));
                            }
                        });
                        more_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast tot = Toast.makeText(GalleryActivity.this,"暂无更多选项",Toast.LENGTH_LONG);tot.show();

                            }
                        });
                        edit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast tot = Toast.makeText(GalleryActivity.this,"请点击分享后编辑",Toast.LENGTH_LONG);tot.show();

                            }
                        });
                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast tot = Toast.makeText(GalleryActivity.this,"已删除，请退出后刷新",Toast.LENGTH_LONG);tot.show();

                            }
                        });
                        info_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast tot = Toast.makeText(GalleryActivity.this,"请稍后查看详细",Toast.LENGTH_LONG);tot.show();

                            }
                        });
                        compress_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(GalleryActivity.this, CompressActivity.class);
                                startActivity(intent);
                            }
                        });
                        back_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              finish();
                            }
                        });
                        //点击后关闭图片
                     //   finish();
                    }
                };
            }
            return mFinishClickListener;
        }
    }



    private void loadImage(String path, ImageView previewView, final ImageView defaultView, final ProgressBar loading) {

        DrawableRequestBuilder builder = getImageLoader()
                .load(path)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (e != null)
                            e.printStackTrace();
                        loading.setVisibility(View.GONE);
                        defaultView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.SOURCE);

        builder.into(previewView);
    }


}
