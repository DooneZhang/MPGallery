package cn.mepstudio.mpgallery.album.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.compressor.Settings.AboutActivity;
import cn.mepstudio.mpgallery.compressor.Settings.HelpActivity;

import static cn.mepstudio.mpgallery.compressor.CompressActivity.QUALITY;
import static net.bither.util.NativeUtil.QUALITY_DEFAULT;

/**
 * Created by ZhangDong on 2017/9/19.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text_help;
    private TextView text_compress_quality;
    private TextView text_bat_compress_quality;
    private TextView text_compress_size;
    private TextView text_path;
    private TextView text_about;
    private TextView text_feedback;
    private TextView text_more;
    private ImageButton back;


    //定义用户可选择的批量变量：尺寸和质量
    public static int QUALITY_BAT = 80;
    int snow_quality_sync;
    String snow_size_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.compressor_activity_settings);

        text_help = (TextView) findViewById(R.id.btn_Help);
        text_compress_quality = (TextView) findViewById(R.id.btn_compress_quality);
        text_compress_size = (TextView) findViewById(R.id.btn_compress_size);
        text_bat_compress_quality = (TextView) findViewById(R.id.btn_bat_compress_quality);
        text_path = (TextView) findViewById(R.id.btn_path);
        text_about = (TextView) findViewById(R.id.btn_about);
        text_feedback = (TextView) findViewById(R.id.btn_feedback);
        text_more = (TextView) findViewById(R.id.btn_more);
        back=(ImageButton)findViewById(R.id.settings_nav_back);

        text_help.setOnClickListener(this);
        text_compress_quality.setOnClickListener(this);
        text_bat_compress_quality.setOnClickListener(this);
        text_compress_size.setOnClickListener(this);
        text_path.setOnClickListener(this);
        text_about.setOnClickListener(this);
        text_feedback.setOnClickListener(this);
        text_more.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //返回
                finish();
            }
        });
        //获取默认设置
        SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
        if(userSettings.getInt("user_defined_size",0)==0){
            snow_size_string="原尺寸";}
        else snow_size_string = userSettings.getInt("user_defined_size",0) +"P";

        text_compress_size.setText(String.format("压缩尺寸:"+ snow_size_string));
        text_compress_quality.setText(String.format("压缩画质:"+ userSettings.getInt("user_defined_quality",0)));
        text_bat_compress_quality.setText(String.format("批量压缩画质:"+ userSettings.getInt("user_defined_bat_quality",0)));
        text_path.setText(String.format("默认保存路径:"+ userSettings.getString("user_defined_files","")));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        //获取默认设置
        SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
        final SharedPreferences.Editor editor = userSettings.edit();

        switch (v.getId()) {

            case R.id.btn_Help:
                intent.setClass(SettingsActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_compress_quality:
                int snow_quality = userSettings.getInt("user_defined_quality",0);

                final SeekBar sbar_quality = new SeekBar(this);

                        sbar_quality.setProgress(snow_quality);
                        sbar_quality.setMax(100);
                //seebar的监听事件
                        sbar_quality.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                            @Override
                            // 触发操作，拖动
                            public void onProgressChanged(SeekBar sbar_quality, int progress, boolean fromUser) {
                                text_compress_quality.setText("压缩画质:"+sbar_quality.getProgress());
                            }
                            @Override
                            // 表示进度条刚开始拖动，开始拖动时候触发的操作
                            public void onStartTrackingTouch(SeekBar sbar_quality) {

                            }
                            @Override
                            // 停止拖动时候
                            public void onStopTrackingTouch(SeekBar sbar_quality) {
                                // TODO Auto-generated method stub
                            }


                        });

                  //     TextView sbar_qualityText =new TextView(this);

                new  AlertDialog.Builder(this)
//sbar_quality.getProgress()
                        .setTitle("修改压缩品质")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(sbar_quality)
                        .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                QUALITY = sbar_quality.getProgress();

                                editor.putInt("user_defined_quality",QUALITY);
                                //提交修改
                                editor.commit();

                                Toast.makeText(getApplicationContext(), "压缩品质已修改为"+ QUALITY, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消" , null )
                        .show();
                break;


            case R.id.btn_compress_size:
            {

                int snow_size = userSettings.getInt("user_defined_size",0);
                final String[] size = new String[]{"原尺寸", "320P", "360P", "480P", "720P", "1080P", "2K", "4K"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // 设置标题
                if(snow_size==0){
                  snow_size_string="原尺寸";}
                else snow_size_string = snow_size +"P";

                builder.setTitle("压缩尺寸："+ snow_size_string)
                        // 设置可选择的内容，并添加点击事件
                                .setItems(size, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // which代表的是选择的标签的序列号
                                switch (size[which]) {
                                    case "原尺寸":
                                        QUALITY_DEFAULT = 0;
                                        break;
                                    case "320P":
                                        QUALITY_DEFAULT = 320;
                                        break;
                                    case "360P":
                                        QUALITY_DEFAULT = 360;
                                        break;
                                    case "480P":
                                        QUALITY_DEFAULT = 480;
                                        break;
                                    case "720P":
                                        QUALITY_DEFAULT = 720;
                                        break;
                                    case "1080P":
                                        QUALITY_DEFAULT = 1080;
                                        break;
                                    case "2K":
                                        QUALITY_DEFAULT = 1440;

                                        break;
                                    case "4K":
                                        QUALITY_DEFAULT = 2160;

                                        break;
                                }
                                editor.putInt("user_defined_size",QUALITY_DEFAULT);
                                //提交修改
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "压缩尺寸已修改为" + size[which]+"，退出后生效", Toast.LENGTH_SHORT).show();

                            }
                        }).
                        // 产生对话框，并显示出来
                                create().show();
            }
            break;

            case R.id.btn_bat_compress_quality:

                int snow_bat_quality = userSettings.getInt("user_defined_bat_quality",0);

                final SeekBar sbar_bat_quality = new SeekBar(this);

                sbar_bat_quality.setProgress(snow_bat_quality);
                sbar_bat_quality.setMax(100);
                //seebar的监听事件
                sbar_bat_quality.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                    @Override
                    // 触发操作，拖动
                    public void onProgressChanged(SeekBar sbar_bat_quality, int progress, boolean fromUser) {
                        text_bat_compress_quality.setText("批量压缩画质:"+sbar_bat_quality.getProgress());
                    }
                    @Override
                    // 表示进度条刚开始拖动，开始拖动时候触发的操作
                    public void onStartTrackingTouch(SeekBar sbar_bat_quality) {

                    }
                    @Override
                    // 停止拖动时候
                    public void onStopTrackingTouch(SeekBar sbar_bat_quality) {
                        // TODO Auto-generated method stub
                    }


                });

                //     TextView sbar_qualityText =new TextView(this);

                new  AlertDialog.Builder(this)
//sbar_quality.getProgress()
                        .setTitle("修改批量压缩品质")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(sbar_bat_quality)
                        .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                QUALITY_BAT = sbar_bat_quality.getProgress();

                                editor.putInt("user_defined_bat_quality",QUALITY_BAT);
                                //提交修改
                                editor.commit();

                                Toast.makeText(getApplicationContext(), "批量压缩品质已修改为"+ QUALITY_BAT, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消" , null )
                        .show();

                break;

            case R.id.btn_path:

                final String snow_path = userSettings.getString("user_defined_files","");

                final EditText Etext = new EditText(this);
                               Etext.setText(snow_path);//这里更改输入框里的内容
                               Etext.requestFocus(); //输入框激活
                               Etext.setSelection(Etext.getText().length()); //将输入点挪到最后
                    new  AlertDialog.Builder(this)

                            .setTitle("文件夹名称" )
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setView(Etext)
                            .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击确认传递文件夹名
                                    String rename_path = Etext.getText().toString();
                                    //第一个参数为生成的文件名，第二个为储存模式
                                    SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = userSettings.edit();
                                    //使用editor保存数据，（键值名，数值）
                                    editor.putString("user_defined_files",rename_path);
                                    editor.commit();
                                    String snow_path = userSettings.getString("user_defined_files","");
                                    Toast.makeText(getApplicationContext(), "文件夹名已修改为"+ snow_path, Toast.LENGTH_SHORT).show();
                                }
                            })

                            .setNegativeButton("取消" ,  null )
                            .show();
                break;

            case R.id.btn_about:
                intent.setClass(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_feedback:
                break;

                case R.id.btn_tip:

                    break;
        }
    }

}