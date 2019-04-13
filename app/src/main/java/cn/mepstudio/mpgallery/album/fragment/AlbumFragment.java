package cn.mepstudio.mpgallery.album.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.album.activity.GalleryActivity;
import cn.mepstudio.mpgallery.album.adapter.ImageAdapter;
import cn.mepstudio.mpgallery.album.adapter.ImageFolderAdapter;
import cn.mepstudio.mpgallery.album.base.BaseFragment;
import cn.mepstudio.mpgallery.album.base.BaseRecyclerAdapter;
import cn.mepstudio.mpgallery.album.bean.Image;
import cn.mepstudio.mpgallery.album.bean.ImageFolder;
import cn.mepstudio.mpgallery.album.listener.ImageLoaderListener;
import cn.mepstudio.mpgallery.album.util.DeviceUtil;
import cn.mepstudio.mpgallery.album.widget.ImageFolderPopupWindow;
import cn.mepstudio.mpgallery.album.widget.SpaceGridItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Rick Ge on 2017/1/10.
 */

public class AlbumFragment extends BaseFragment implements ImageLoaderListener, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener{
    @BindView(R.id.rv_imageslist) RecyclerView mContentView;
    @BindView(R.id.btn_title_select)
    Button mSelectFolderView;
    @BindView(R.id.iv_title_select)
    ImageView mSelectFolderIcon;
    @BindView(R.id.select_album)
    View mToolbar;

    private LoaderListener mCursorLoader;
    private ImageFolderAdapter mImageFolderAdapter;
    private ImageAdapter mImageAdapter;
    private ImageFolderPopupWindow mFolderPopupWindow;

    private String[] mImageSources;
    private int[] mImages;

    @Override
    protected int getLayoutId() {
        return R.layout.album_fragment_select_image;
    }

    //
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        //相册每行显示的个数
        mContentView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mContentView.addItemDecoration(new SpaceGridItemDecoration((int) DeviceUtil.dipToPx(getResources(), 0.5f)));
        mImageAdapter = new ImageAdapter(getContext(), this);
        mImageFolderAdapter = new ImageFolderAdapter(getContext(), this);
        mContentView.setAdapter(mImageAdapter);

       // mContentView.setAdapter(new CorkiAdapter());

      //  mContentView.setItemAnimator(null);
        mImageAdapter.setOnItemClickListener(this);

        mImages = new int[]{

        };

    }

    @Override
    protected void initData() {
        super.initData();
        mCursorLoader = new LoaderListener();
        getLoaderManager().initLoader(0, null, mCursorLoader);
    }

    @Override
    public void displayImage(ImageView iv, String path) {
        // Load image
        getImgLoader().load(path)
                .asBitmap()
                .centerCrop()
                .error(R.mipmap.ic_split_graph)
                .into(iv);
    }

    @OnClick({R.id.btn_title_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_title_select:
                //点击弹出相册按钮
                showPopupFolderList();
                break;
        }
    }

    /**
     * 创建弹出的相册
     */
    private void showPopupFolderList() {
        if (mFolderPopupWindow == null) {
            ImageFolderPopupWindow popupWindow = new ImageFolderPopupWindow(getContext(), new ImageFolderPopupWindow.Callback() {

                @Override
                public void onSelect(ImageFolder imageFolder) {
                    addImagesToAdapter(imageFolder.getImages());
                }

                @Override
                public void onDismiss() {
                    mSelectFolderIcon.setImageResource(R.mipmap.ic_arrow_bottom);
                }

                @Override
                public void onShow() {
                    mSelectFolderIcon.setImageResource(R.mipmap.ic_arrow_top);
                }
        /*         */
            });
            popupWindow.setAdapter(mImageFolderAdapter);
            mFolderPopupWindow = popupWindow;
        }
        mFolderPopupWindow.showAsDropDown(mToolbar);
    }

    @Override
    public void onItemClick(int position) {
        GalleryActivity.show(getContext(), mImageSources, position);
    }

    private class LoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                //数据库光标加载器
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                final ArrayList<Image> images = new ArrayList<>();
                final List<ImageFolder> imageFolders = new ArrayList<>();

                final ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("全部照片");
                defaultFolder.setPath("");
                imageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));

                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);

                        images.add(image);


                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder folder = new ImageFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        if (!imageFolders.contains(folder)) {
                            folder.getImages().add(image);
                            folder.setAlbumPath(image.getPath());//默认相册封面
                            imageFolders.add(folder);
                        } else {
                            // 更新
                            ImageFolder f = imageFolders.get(imageFolders.indexOf(folder));
                            f.getImages().add(image);
                        }
                    } while (data.moveToNext());
                }

                addImagesToAdapter(images);
                defaultFolder.getImages().addAll(images);
                defaultFolder.setAlbumPath(images.size() > 0 ? images.get(0).getPath() : null);
                mImageFolderAdapter.resetItem(imageFolders);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private void addImagesToAdapter(ArrayList<Image> images) {
        mImageAdapter.resetItem(images);
        mImageSources = toArray(images);
    }


    private static String[] toArray(List<Image> images) {
        if (images == null)
            return null;
        int len = images.size();
        if (len == 0)
            return null;

        String[] strings = new String[len];
        int i = 0;
        for (Image image : images) {
            strings[i] = image.getPath();
            i++;
        }
        return strings;
    }

}
