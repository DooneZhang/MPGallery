package cn.mepstudio.mpgallery.compressor;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import net.bither.util.NativeUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.album.activity.SettingsActivity;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.iwf.photopicker.PhotoPicker;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static cn.mepstudio.mpgallery.compressor.Utils.SDCardHelper.getSDCardBaseDir;
import static me.iwf.photopicker.PhotoPicker.REQUEST_CODE;

/**
 * Created by ZhangDong on 2017/9/9.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */
public class CompressActivity extends AppCompatActivity {
    private List<ImageBean> mImageList = new ArrayList<>();
    private ImageAdapter mAdapter = new ImageAdapter(mImageList);
    public static final int REQUEST_PICK_IMAGE = 10011;
    public static final int REQUEST_KITKAT_PICK_IMAGE = 10012;
    public static final int REQUEST_TAKE_PHOTO = 10013;
    public static final int REQUEST_N_PICK_IMAGE = 10014;
    //定义用户可选择的变量：尺寸和质量
    public static int SIZE=0,QUALITY=100;
    //定义选取或拍照所得照片的尺寸
    public int W,H;
    //定义压缩前后图片所占空间
    public long before_save,after_save;
    //定义本次压缩节约空间与总共节约空间
    public long now_save,sum_save;
    public long bat_save_memory,origin_Size,thumb_Size,thumbSize_sum,originSize_sum;
    //批量压缩的当前次数
    public int times;
    //定义压缩之前与之后图片的URI地址
    private static Uri PATH_BEFORE= null;
    private static Uri PATH_AFTER= null;
    //定义用户自定义设置的文件夹名的变量
    public static String user_defined_files = null;
    //定义拍照所得照片和压缩后照片的具体路径包含文件后缀
    //定义拍照所得照片和压缩后照片的储存路径名称字符串变量
    public static String save_path_string = null;
    public String shot_name = null;
    //public static String src_name;
    public static String real_path = null;
    //定义Bitmap变量
    public static Bitmap bitmap = null;
    public File shot_file= null,file_before = null,file_after = null;
    public boolean is_shot;
    private ImageView actualImageView,compressedImageView;
    private TextView actualSizeTextView,compressedSizeTextView,actualPPiTextView,compressedPPiTextView,nowTextView,sumTextView;
    private ProgressBar compressing;
    /*获取当前系统的android版本号*/
    int api_version = android.os.Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.compressor_activity_main);
        //小米更新

        final LinearLayout vs_view = (LinearLayout) findViewById(R.id.vs_view);
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        actualImageView = (ImageView) findViewById(R.id.actual_image);
        actualImageView.setBackgroundColor(getRandomColor());
        compressedImageView = (ImageView) findViewById(R.id.compressed_image);
        compressedImageView.setBackgroundColor(getRandomColor());
        actualSizeTextView = (TextView) findViewById(R.id.actual_size);
        compressedSizeTextView = (TextView) findViewById(R.id.compressed_size);

        actualPPiTextView=(TextView) findViewById(R.id.actual_ppi);
        compressedPPiTextView=(TextView) findViewById(R.id.compressed_ppi);
        nowTextView=(TextView) findViewById(R.id.now_save);
        sumTextView=(TextView) findViewById(R.id.sum_save);
        compressing =(ProgressBar) findViewById(R.id.compressing);
        //清空图片
        clearImage();

        FloatingActionButton fab_add =  findViewById(R.id.fab_add);
        FloatingActionButton fab_shot = findViewById(R.id.fab_shot);
        final FloatingActionButton fab_bat =  findViewById(R.id.fab_bat);

        ImageView actualImage =findViewById(R.id.actual_image) ;
        final ImageView compressedImage = findViewById(R.id.compressed_image) ;

        ImageButton menu = findViewById(R.id.nav_menu);
        ImageButton setting = findViewById(R.id.nav_setting);
        /**
         *点击设置按钮的监听事件
         */
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CompressActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
       /**
       *点击菜单按钮的监听事件
        */
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                item_menu();
            }
        });
       /**
         *点击添加按钮的监听事件
         */
        fab_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               // clearImage();
                //清空照片画布
                actualImageView.setImageBitmap(null);
                compressedImage.setImageBitmap(null);

                if(vs_view.getVisibility() == View.GONE)
                {
                    mRecyclerView.setVisibility(View.GONE);
                    vs_view.setVisibility(View.VISIBLE);
                }
                else{
                    // holder.report_layout.setVisibility(View.GONE);
                }
                //选择照片
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent,REQUEST_KITKAT_PICK_IMAGE);
                }
                //系统版本大于7.0
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_N_PICK_IMAGE);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "您的系统版本可能暂时无法使用", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_PICK_IMAGE);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "您的系统版本可能暂时无法使用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
           /**
            **点击拍照按钮的监听事件
            **/
        fab_shot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(vs_view.getVisibility() == View.GONE)
                {
                    mRecyclerView.setVisibility(View.GONE);
                    vs_view.setVisibility(View.VISIBLE);
                }
                else{
                    // holder.report_layout.setVisibility(View.GONE);
                }
                try {
                    // 激活系统的照相机进行拍照
                    Intent intent = new Intent();
                    intent.setAction("android.media.action.IMAGE_CAPTURE");
                    //获取用户自定义的储存文件夹名
                    SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
                    user_defined_files = userSettings.getString("user_defined_files", "");
                    save_path_string = getSDCardBaseDir() + File.separator + user_defined_files;
                    boolean temp = userSettings.getBoolean("no_save_shot_key", false);
                    //拍照所的照片的名字字符串
                    if (temp == true) {
                        shot_name = "/拍照已压缩_" + System.currentTimeMillis() + ".jpg";
                    } else {
                        shot_name = "/拍摄未压缩_" + System.currentTimeMillis() + ".jpg";
                    }
                    //生成拍照的文件
                    shot_file = new File(save_path_string + shot_name);
                    file_before = shot_file;
                    //定义拍照所得照片的uri路径也就是照片获取路径
                    PATH_BEFORE = Uri.fromFile(shot_file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, PATH_BEFORE);
                    //7.0出错点
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "您的系统为7.0以上，尚不兼容，下个版本就可以使用啦~", Toast.LENGTH_SHORT).show();
                }
              //  clearImage();
            }
        });
            /**
             * 点击批量按钮的监听事件
             **/
              fab_bat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //让压缩后图片显示列表显示出来并隐藏vs_view空间
                if(mRecyclerView.getVisibility() == View.GONE)
                {
                    vs_view.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                else{
                   // holder.report_layout.setVisibility(View.GONE);
                }
                mImageList.clear();
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(true)
                        .start(CompressActivity.this, REQUEST_CODE);
            }
        });
        //点击压缩前图片的监听事件
        actualImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file_before!=null) {
                    // 点击确认查看图片
                    try{ Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file_before), "image/*");
                    startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "哇！您的系统为7.0以上，暂时无法使用，我们正在努力适配~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //点击压缩后图片的监听事件
        compressedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file_after!=null) {
                    // 点击确认查看图片
                    //  "content://media/internal/images/media"
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file_after), "image/*");
                    //7.0出错点
                    startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "哇！您的系统为7.0以上，暂时无法使用，我们正在努力适配~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

   //清理图片
    private void clearImage() {
        actualImageView.setBackgroundColor(Color.parseColor("#00ffffff"));
        compressedImageView.setBackgroundColor(Color.parseColor("#00ffffff"));
        compressedSizeTextView.setText("大小 : -");
        actualSizeTextView.setText("大小 : -");
    }
   //随机背景颜色
    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }
   //获取照片大小
    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    //Activity加载完毕事件
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
      //  Toast.makeText(getApplicationContext(), "页面加载完成", Toast.LENGTH_SHORT).show();
    }
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
   /*
    //双击返回退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                file_before = null;
                file_after = null;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    */

    //获得图片的真实地址
    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Uri ensureUriPermission(Context context, Intent intent) {
        PATH_BEFORE = intent.getData();
        //如果当前API版本号小于4.4系统_19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int takeFlags = intent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            context.getContentResolver().takePersistableUriPermission(PATH_BEFORE, takeFlags);
        }
        return PATH_BEFORE;
    }
    //获得图片的真实地址
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( uri==null ) return null;
        //定义头标识字符串，将返回当前链接使用的协议，返回头标识file或content
        final String scheme = uri.getScheme();
        if ( scheme == null )
            real_path = uri.getPath();
        //如果链接头为：file
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            real_path = uri.getPath();
            //如果链接头为：content
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) )
            try{
            //定义光标
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
           //     Cursor cursor = context.query(uri, filePathColumn, null, null, null);
            if (  cursor !=null  ) {
                if ( cursor.moveToFirst() ) {
                    //将指定行的数据赋值给INDEX
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        real_path = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }catch (Exception e){
        }
        return real_path;
    }
    //用户选择完成后根据设备返回值选择不同系统的方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取设置的自定义储存文件夹名
        SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
        boolean always_ask = userSettings.getBoolean("always_ask_key",false);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //4.4系统返回方法
                case REQUEST_KITKAT_PICK_IMAGE:
                    if (data != null) {
                        //获取用户加载图片的地址
                        ensureUriPermission(this, data);
                        //获取用于获取大小的地址
                        getRealFilePath(this,PATH_BEFORE);
                        if(real_path!=null) {
                            try {
                                File srcfile = new File(real_path.toString());
                                // 用于点击图片查看
                                file_before = srcfile;
                                before_save = srcfile.length();
                                actualSizeTextView.setText(String.format("大小 : %s", getReadableFileSize(before_save)));
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "咦！暂时还不能在7.0及以上版本的手机显示原始文件大小哦！", Toast.LENGTH_LONG).show();
                            }
                        }
                        try {
                            //通过文件路径转成Bitmap并赋值给bitmap
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PATH_BEFORE);
                            if(api_version <= 23) {
                                //将图片显示在imageview上
                                actualImageView.setImageBitmap(bitmap);
                            }
                            //获取未压缩照片的宽度与高度
                            W = bitmap.getWidth();
                            H = bitmap.getHeight();
                            //回收bitmap
                            //      bitmap.recycle();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "呜呜呜！出了点小问题！可能软件还没有适配你的手机，请及时向开发者反馈！或试试其他压缩方式吧！", Toast.LENGTH_LONG).show();
                        }
                        //将图片的大小和尺寸信息显示在textview上
                        actualPPiTextView.setText(String.format("尺寸 : " + W + "*" + H));
                        if(always_ask==true){
                            //如果用户选择每次弹出参数调整，则显示对话框
                            snowSizeItems();
                        }
                        //如果用户没有选择每次弹出参数调整，则弹出询问用户是否确认选择对话框
                        else yes_no();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "哇！你居然选了一个空白文件！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        try {
                            Uri selectedImage = data.getData();
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePathColumns[0]);
                            String imagePath = c.getString(columnIndex);

                            File srcfile = new File(imagePath);
                            // 用于点击图片查看
                            file_before = srcfile;
                            PATH_BEFORE = Uri.fromFile(file_before);
                            before_save = srcfile.length();
                            actualSizeTextView.setText(String.format("大小 : %s", getReadableFileSize(before_save)));
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "您的系统版本可能暂时无法使用", Toast.LENGTH_SHORT).show();
                        }
                        //通过文件路径转成Bitmap并赋值给bitmap
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file_before));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //将图片显示在imageview上
                        actualImageView.setImageBitmap(bitmap);

                        //获取未压缩照片的宽度与高度
                        W = bitmap.getWidth();
                        H = bitmap.getHeight();

                //将图片的大小和尺寸信息显示在textview上
                actualPPiTextView.setText(String.format("尺寸 : " + W + "*" + H));
                if(always_ask==true){
                    //如果用户选择每次弹出参数调整，则显示对话框
                    snowSizeItems();
                }
                //如果用户没有选择每次弹出参数调整，则弹出询问用户是否确认选择对话框
                else yes_no();
                //储存文件夹完整路径
                save_path_string = getSDCardBaseDir() + File.separator + user_defined_files;

                        /**

                       //获取用户加载图片的地址
                    //    ensureUriPermission(this, data);
                       //获取用于获取大小的地址
                     //   getRealFilePath(this,PATH_BEFORE);
                        if(real_path!=null) {
                            try {
                                File srcfile = new File(real_path.toString());
                               // 用于点击图片查看
                                file_before = srcfile;
                                before_save = srcfile.length();
                                actualSizeTextView.setText(String.format("大小 : %s", getReadableFileSize(before_save)));
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "咦！暂时还不能在您的手机版本上显示原始文件大小哦！", Toast.LENGTH_LONG).show();
                            }
                        }
                        try {
                            //通过文件路径转成Bitmap并赋值给bitmap
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PATH_BEFORE);
                            if(api_version <= 23) {
                                //将图片显示在imageview上
                                actualImageView.setImageBitmap(bitmap);
                            }
                            //获取未压缩照片的宽度与高度
                            W = bitmap.getWidth();
                            H = bitmap.getHeight();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "呜呜呜！出了点小问题！可能软件还没有适配你的手机，请及时向开发者反馈！或试试其他压缩方式吧！", Toast.LENGTH_LONG).show();
                        }
                        //将图片的大小和尺寸信息显示在textview上
                        actualPPiTextView.setText(String.format("尺寸 : " + W + "*" + H));
                        if(always_ask==true){
                            //如果用户选择每次弹出参数调整，则显示对话框
                            snowSizeItems();
                        }
                        //如果用户没有选择每次弹出参数调整，则弹出询问用户是否确认选择对话框
                        else yes_no();
                        //储存文件夹完整路径
                        save_path_string = getSDCardBaseDir() + File.separator + user_defined_files;
                    }
                    else {
                       // Log.e("======", "========图片为空======");
                        Toast.makeText(getApplicationContext(), "哇！你居然选了一个空白文件！", Toast.LENGTH_SHORT).show();
                    }

                         **/
                    }
                    break;
                    //拍照返回方法
                case REQUEST_TAKE_PHOTO:
                    try {
                        is_shot=true;
                        //通过文件路径转成Bitmap并赋值给bitmap
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PATH_BEFORE);
                        if(api_version <= 23) {
                            //将图片显示在imageview上
                            actualImageView.setImageBitmap(bitmap);
                        }
                        //获取未压缩照片的宽度与高度
                        W = bitmap.getWidth();
                        H = bitmap.getHeight();
                        //回收bitmap
                     //   bitmap.recycle();
                        //将图片的大小和尺寸信息显示在textview上
                        before_save = shot_file.length();
                        actualSizeTextView.setText(String.format("大小 : %s", getReadableFileSize(before_save)));
                        actualPPiTextView.setText(String.format("尺寸 : " + W + "*" + H));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "呜呜呜！出了点小问题！可能软件还没有适配你的手机，请及时向开发者反馈！或试试其他压缩方式吧！", Toast.LENGTH_LONG).show();
                    }
                    if(always_ask==true){
                        //如果用户选择每次弹出参数调整，则显示对话框
                        snowSizeItems();
                    }
                    //如果用户没有选择每次弹出参数调整，则弹出询问用户是否确认选择对话框
                    else yes_no();
                    //完成选择开始压缩
                  //  compressImage(PATH_BEFORE);
                    break;
                    //7.0以上返回方法
                case REQUEST_N_PICK_IMAGE:
                    if (data != null) {
                        //获取用户加载图片的地址
                        ensureUriPermission(this, data);
                        //获取用于获取大小的地址
                        getRealFilePath(this,PATH_BEFORE);
                        if(real_path!=null) {
                            try {
                                File srcfile = new File(real_path.toString());
                                // 用于点击图片查看
                                file_before = srcfile;
                                before_save = srcfile.length();
                                actualSizeTextView.setText(String.format("大小 : %s", getReadableFileSize(before_save)));
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), "咦！暂时还不能在你的手机显示原始文件大小哦！", Toast.LENGTH_LONG).show();
                            }
                        }
                        try {
                            //通过文件路径转成Bitmap并赋值给bitmap
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PATH_BEFORE);
                            if(api_version <= 23) {
                                //将图片显示在imageview上
                                actualImageView.setImageBitmap(bitmap);
                            }
                            //获取未压缩照片的宽度与高度
                            W = bitmap.getWidth();
                            H = bitmap.getHeight();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "呜呜呜！出了点小问题！可能软件还没有适配你的手机，请及时向开发者反馈！或试试其他压缩方式吧！", Toast.LENGTH_LONG).show();
                        }
                        //将图片的大小和尺寸信息显示在textview上
                        actualPPiTextView.setText(String.format("尺寸 : " + W + "*" + H));

                        if(always_ask==true){
                            //如果用户选择每次弹出参数调整，则显示对话框
                            snowSizeItems();
                        }
                        //如果用户没有选择每次弹出参数调整，则弹出询问用户是否确认选择对话框
                        else yes_no();
                        //储存文件夹完整路径
                        save_path_string = getSDCardBaseDir() + File.separator + user_defined_files;
                    }
                    else {
                        // Log.e("======", "========图片为空======");
                        Toast.makeText(getApplicationContext(), "哇！你居然选了一个空白文件！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //批量压缩方式
                case REQUEST_CODE:
                    if (data != null) {
                        //情况图片列表
                        mImageList.clear();
                        ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        //调用批量压缩方法
                        compressWithLs(photos);
                        //  compressWithRx(photos);
                    }
                break;

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
             //   Uri uri = FileProvider.getUriForFile(this, "包名.fileprovider", picture);
            }
        }

    }
    @SuppressLint("CheckResult")
    private void compressWithRx(final List<String> photos) {
        Flowable.just(photos)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override public List<File> apply(@NonNull List<String> list) throws Exception {
                        return Luban.with(CompressActivity.this).load(list).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override public void accept(@NonNull List<File> list) throws Exception {
                        for (File file : list) {
                            showResult(photos, file);
                        }
                    }
                });
    }
    /**
     * 压缩图片 Listener 方式
     */
    private void compressWithLs(final List<String> photos) {
           Luban.with(this)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onSuccess(File file) {

                        showResult(photos, file);
                        if(times==1){
                            bat_save_memory=thumbSize_sum+originSize_sum;
                            //获取设置的自定义储存文件夹名
                            SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
                            //获取初始总共节约内存
                            SharedPreferences.Editor editor = userSettings.edit();
                            sum_save = userSettings.getLong("sum_save",1);
                            // now_save = before_save - after_save;
                            // float bat_compress_rate =100-((float)thumbSize_sum/(float)originSize_sum*100);
                            //将本次总共节约空间加在总量上
                            sum_save += bat_save_memory;
                            editor.putLong("sum_save",sum_save);
                            //提交修改
                            editor.commit();

                            nowTextView.setText(String.format("本次压缩掉 : %s", getReadableFileSize(bat_save_memory)));
                            sumTextView.setText(String.format("累计压缩掉 : %s",getReadableFileSize(sum_save)));
                            Toast.makeText(getApplicationContext(), "压缩完成！", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "保存在手机储存的'魔方相册'文件夹下", Toast.LENGTH_LONG).show();
                            //将判断因子N还原
                             n = 0;
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                })
                   .launch();
    }
    //获取文件夹路径
    private String getPath() {
        //获取设置的自定义储存文件夹名
        SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
        user_defined_files = userSettings.getString("user_defined_files","");
        //储存文件夹完整路径
        save_path_string = getSDCardBaseDir() + File.separator + user_defined_files;
        String path = save_path_string;
        //压缩后的照片起名
     //   final File save_file = new File(save_path_string, "已压缩_" + System.currentTimeMillis() + ".jpg");
        return path;
    }
    int n = 0;
    private void showResult(List<String> photos, File file) {
        int[] originSize = computeSize(photos.get(mAdapter.getItemCount()));
        int[] thumbSize = computeSize(file.getAbsolutePath());

       if (n==0) {
           times = photos.size()-1+1;
           n=n+1;
       }
       else {
            times = times - 1;
        }
        origin_Size = new File(photos.get(mAdapter.getItemCount())).length();
        thumb_Size = file.length();

        thumbSize_sum+=origin_Size;
        originSize_sum += origin_Size;

        String originArg = String.format(Locale.CHINA, "原图参数：%d*%d, %s", originSize[0], originSize[1], getReadableFileSize(1024*new File(photos.get(mAdapter.getItemCount())).length() >> 10));
        String thumbArg = String.format(Locale.CHINA, "压缩后参数：%d*%d, %s", thumbSize[0], thumbSize[1], getReadableFileSize(1024*file.length() >> 10));

     //   getReadableFileSize(bat_save_memory);
        ImageBean imageBean = new ImageBean(originArg, thumbArg, file.getAbsolutePath());
        mImageList.add(imageBean);
        mAdapter.notifyDataSetChanged();
    }
    private int[] computeSize(String srcImg) {
        int[] size = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg, options);
        //宽
        size[0] = options.outWidth;
        //高
        size[1] = options.outHeight;

        return size;
    }
    //是否确认选择照片
    private void yes_no(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
      //  LayoutInflater inflater = getLayoutInflater();
        View yes_no = getLayoutInflater().inflate(R.layout.compressor_home_isstart_dialog, null);//获取自定义布局

         builder.setView(yes_no)//设置login_layout为对话提示框
                .setTitle("确定选择")
              //  .setMessage("确定设定并开始压缩")
                .setCancelable(false)//设置为不可取消
        //设置正面按钮，并做事件处理
                .setPositiveButton("开始", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //开始压缩的进度出现
                compressing.setVisibility(View.VISIBLE);
                //延迟0.5秒开始压缩
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //用户确认选择开始压缩
                        compressImage(PATH_BEFORE);
                    }
                }, 500);
            }
        });
        //设置取消按钮，并做事件处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //用户取消选择清除选择的图片
              //  clearImage();
                actualImageView.setImageBitmap(null);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();//显示Dialog对话框
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
   //     WindowManager m = getWindowManager();
     //   Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
    //    WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//设置高度和宽度
     //   p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
     //   p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.65
//设置位置
     //   p.gravity = Gravity.BOTTOM;
//设置透明度
       // p.alpha = 0.2f;
        dialogWindow.setBackgroundDrawableResource(R.color.transtparent_pink);
       // dialogWindow.setAttributes(p);
        Spinner spinner = yes_no.findViewById(R.id.spinner_model);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String[] model = getResources().getStringArray(R.array.model);
                switch(model[pos]){
                    case "画质优先":
                        QUALITY=100;
                        SIZE=0;
                        break;
                    case"平衡模式":
                        QUALITY=80;
                        SIZE=720;
                        break;
                    case"压缩模式":
                        QUALITY=50;
                        SIZE=480;
                        break;
                    case"朋友圈模式":
                        QUALITY=100;
                        SIZE=720;
                        break;
                    case"聊天模式":
                        QUALITY=70;
                        SIZE=480;
                        break;
                }
                Toast.makeText(CompressActivity.this, "你选择了:"+ model[pos], Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        Button btn_more = yes_no.findViewById(R.id.btn_more);
        final LinearLayout edit_more = yes_no.findViewById(R.id.edit_more);
        btn_more.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(edit_more.getVisibility() == View.GONE) {
                    edit_more.setVisibility(View.VISIBLE);

                }else edit_more.setVisibility(View.GONE);
        }
        }));
    }
    //弹出菜单
    private void item_menu(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //  LayoutInflater inflater = getLayoutInflater();
        View menu_dialog = getLayoutInflater().inflate(R.layout.compressor_home_mune_dialog, null);//获取自定义布局

        AlertDialog dialog = builder.create();
        dialog.getWindow().setDimAmount(0);
        dialog.show();//显示Dialog对话框

        builder.setView(menu_dialog);//设置login_layout为对话提示框

        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();

        dialogWindow.setContentView(R.layout.compressor_home_mune_dialog);
        final CheckBox nosave_shot_box = dialogWindow.findViewById(R.id.nosave_shot);
        final CheckBox always_ask_box = dialogWindow.findViewById(R.id.always_ask);
        //获取设置
        SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
        boolean temp_nosave = userSettings.getBoolean("no_save_shot_key",false);
        boolean temp_ask = userSettings.getBoolean("always_ask_key",false);
        if (temp_nosave == true)
        {
            nosave_shot_box.setChecked(true);
        }
        if (temp_ask == true)
        {
            always_ask_box.setChecked(true);
        }
        nosave_shot_box.setOnClickListener(new android.view.View.OnClickListener() {
            //获取设置
            SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
            boolean temp_nosave = userSettings.getBoolean("no_save_shot_key",false);
            @Override
            public void onClick(View v) {
                //获取初始总共节约内存
                SharedPreferences.Editor editor = userSettings.edit();
                //点击后再true和false切换
                if(temp_nosave==true){
                    temp_nosave=false;
                }
                else temp_nosave=true;
                editor.putBoolean("no_save_shot_key",temp_nosave);
                //提交修改
                editor.commit();
            }
    });
        always_ask_box.setOnClickListener(new android.view.View.OnClickListener() {
            //获取设置
            SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
            boolean temp_ask = userSettings.getBoolean("always_ask_key",false);
            @Override
            public void onClick(View v) {
                //获取初始总共节约内存
                SharedPreferences.Editor editor = userSettings.edit();
                //点击后再true和false切换
                if(temp_ask==true){
                    temp_ask=false;
                }
                else temp_ask=true;
                editor.putBoolean("always_ask_key",temp_ask);
                //提交修改
                editor.commit();
            }
        });

        WindowManager m = getWindowManager();
           Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//设置高度和宽度
          // p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
           p.width = (int) (d.getWidth() * 0.5); // 宽度设置为屏幕的0.65
//设置位置
           p.x=150;
           p.y=150;
           p.gravity = Gravity.LEFT | Gravity.TOP;
//设置透明度
          // p.alpha = 0.f;
         dialogWindow.setBackgroundDrawableResource(R.color.white_transtparent);
         dialogWindow.setAttributes(p);
    }
    //弹出尺寸选择
    public void snowSizeItems(){
        final String[] size = new String[] { "原尺寸","320P", "360P", "480P", "720P","1080P","2K","4K" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置标题
                builder.setTitle("选择缩放尺寸").
                // 设置可选择的内容，并添加点击事件
                        setItems(size, new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which代表的是选择的标签的序列号
                        switch (size[which]) {
                            case"原尺寸":
                                SIZE=0;
                                break;
                            case "320P":
                                SIZE=320;
                                break;
                            case "360P":
                                SIZE=360;
                                break;
                            case "480P":
                                SIZE=480;
                                break;
                            case "720P":
                                SIZE=720;
                                break;
                            case "1080P":
                                SIZE=1080;
                                break;
                            case "2K":
                                SIZE=1440;
                                break;
                            case "4K":
                                SIZE=2160;
                                break;
                        }
                       Toast.makeText(getApplicationContext(), "选择了"+ size[which],Toast.LENGTH_SHORT).show();
                       //弹出质量选项对话框
                        snowQualityItems();
                    }
                }).
                // 产生对话框，并显示出来
                        create().show();
    }
    //弹出质量选择
    public void snowQualityItems(){
        final String[] quality = new String[] { "100%", "90%", "80%", "70%","60%","50%","40%","30%","20%","10%" };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置标题
        builder.setTitle("选择压缩质量").
                // 设置可选择的内容，并添加点击事件
                        setItems(quality, new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // which代表的是选择的标签的序列号
                        switch (quality[which]) {
                            case "100%":
                                QUALITY=100;
                                break;
                            case "90%":
                                QUALITY=90;
                                break;
                            case "80%":
                                QUALITY=80;
                                break;
                            case "70%":
                                QUALITY=70;
                                break;
                            case "60%":
                                QUALITY=60;
                                break;
                            case "50%":
                                QUALITY=50;
                                break;
                            case "40%":
                                QUALITY=40;
                                break;
                            case "30%":
                                QUALITY=30;
                                break;
                            case "20%":
                                QUALITY=20;
                                break;
                            case "10%":
                                QUALITY=10;
                                break;
                        }
                       Toast.makeText(getApplicationContext(), "选择了"+ quality[which],Toast.LENGTH_SHORT).show();
                        //完成选择开始压缩
                       compressImage(PATH_BEFORE);
                    }
                }).
                // 产生对话框，并显示出来
                        create().show();
    }
    //压缩功能的一系列设置
    public void compressImage(final Uri PATH_BEFORE) {
      //  Log.e("===compressImage===", "====开始====uri==" + uri.getPath());
        try {
            //获取设置的自定义储存文件夹名
            SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
            user_defined_files = userSettings.getString("user_defined_files","");
            //储存文件夹完整路径
            save_path_string = getSDCardBaseDir() + File.separator + user_defined_files;
            //压缩后的照片起名
            boolean temp = userSettings.getBoolean("no_save_shot_key",false);
            //拍照所的照片的名字字符串
            File save_file;
            if(temp==true&&is_shot==true){
                    save_file = new File(save_path_string, shot_name);
            }
           else {
              save_file = new File(save_path_string, "已压缩_" + System.currentTimeMillis() + ".jpg");
            }
             file_after = save_file;
             //创建文件夹
                File files = new File(save_path_string);
                if (!files.exists()) {
                    //如果没有此文件夹就新建一个
                    files.mkdirs();
                }
               //通过文件路径转成Bitmap并赋值给bitmap
              //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PATH_BEFORE);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file_before));
                //    Log.e("===compressImage===", "====开始==压缩==saveFile==" + saveFile.getAbsolutePath());
                Toast.makeText(getApplicationContext(), "正在压缩...", Toast.LENGTH_LONG).show();
                NativeUtil.compressBitmap(bitmap, save_file.getAbsolutePath());
                //    Log.e("===compressImage===", "====完成==压缩==saveFile==" + saveFile.getAbsolutePath());
                Toast.makeText(getApplicationContext(), "压缩完成！", Toast.LENGTH_SHORT).show();
                //定义压缩之后的照片的路径
                PATH_AFTER = Uri.fromFile(save_file);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PATH_AFTER);
                //将压缩后的图片显示在Imageview上
                compressedImageView.setImageBitmap(bitmap);
               //回收bitmap
              //  bitmap.recycle();
                //将图片的大小和尺寸信息显示在textview上
                after_save = save_file.length();
                compressedSizeTextView.setText(String.format("大小 : %s", getReadableFileSize(after_save)));
                compressedPPiTextView.setText(String.format(bitmap.getWidth() + "*" + bitmap.getHeight(), bitmap.getWidth(),bitmap.getHeight()));

            //获取初始总共节约内存
            SharedPreferences.Editor editor = userSettings.edit();
            sum_save = userSettings.getLong("sum_save",1);
            now_save = before_save - after_save;
            float compress_rate =100-((float)after_save/(float)before_save*100);
            sum_save += now_save;
            editor.putLong("sum_save",sum_save);
            editor.commit();
                //将本次节约空间和总共节约空间显示在textview上
                //nowTextView.setText(String.format("本次压缩了 : %s"+"%",compress_rate));
                nowTextView.setText(String.format("本次压缩掉 : %.2f%%", compress_rate));
                sumTextView.setText(String.format("累计压缩掉 : %s",getReadableFileSize(sum_save)));
                //进度条消失
                 compressing.setVisibility(View.GONE);
            final LinearLayout vs_view = (LinearLayout) findViewById(R.id.vs_view);
                //图片对比控件出现
                vs_view.setVisibility(View.VISIBLE);
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "呜呜呜！出了点小问题！可能软件还没有适配你的手机，请及时向开发者反馈！或试试其他压缩方式吧！", Toast.LENGTH_LONG).show();
        }
    }

}