package cn.mepstudio.mpgallery.compressor;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.mepstudio.mpgallery.R;
import cn.mepstudio.mpgallery.album.activity.SettingsActivity;

/**
 * Created by ZhangDong on 2017/10/19.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */

public class MainpageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compressor_activity_mainpage);



        final ImageView img_loading = (ImageView) findViewById(R.id.img_loading);
        final ImageView img_info = (ImageView) findViewById(R.id.img_info);
        final ImageButton ib_add = (ImageButton) findViewById(R.id.imgbtn_add);
        final LinearLayout vs_view = (LinearLayout) findViewById(R.id.vs_view);

        final LinearLayout summary = (LinearLayout) findViewById(R.id.summary);


//使背景自由运动
        runAnimation();


        ImageButton menu = (ImageButton) findViewById(R.id.nav_menu);
        ImageButton setting = (ImageButton) findViewById(R.id.nav_setting);

        /**
         *点击设置按钮的监听事件
         */
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainpageActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

/**
 *点击菜单按钮的监听事件
 */
        menu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainpageActivity.this, MainpageActivity.class);
                startActivity(intent);


            }
        });



/*
        ScaleAnimation animation_breath_mormel =new ScaleAnimation(1.0f, 0.7f, 1.0f, 0.7f,
                Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        animation_breath_mormel.setDuration(2500);//设置动画持续时间
        //停留在缩放后的状态
        //  animation.setFillAfter(true);
        //设置呼吸循环

        //无限循环
        animation_breath_mormel.setRepeatCount(Integer.MAX_VALUE);
        //设置执行的模式 RESTART为结束后重新开始，
        //     animation_breath.setRepeatMode(Animation.RESTART);
//        //设置执行的模式为按原来的轨迹逆向返回
        animation_breath_mormel.setRepeatMode(Animation.REVERSE);
        //无限循环
        //       animation_breath.setRepeatMode(Animation.INFINITE);

        //添加动画在控件上
        ib_add.setAnimation(animation_breath_mormel);
        /** 开始缩放动画 */
      //  animation_breath_mormel.startNow();



        /** 设置缩放动画 */
//        final ScaleAnimation animation =new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f,
        //              Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //    animation.setDuration(1500);//设置动画持续时间
        //停留在缩放后的状态
        //  animation.setFillAfter(true);
//添加动画在控件上
        // ib_add.setAnimation(animation);
        /** 开始缩放动画 */
        // animation.startNow();


        /** 设置位移动画 向右位移150 */
        // final TranslateAnimation animation1 = new TranslateAnimation(0, 0,0,500);
        //  animation1.setDuration(1000);//设置动画持续时间
        // animation1.setRepeatCount(1);//设置重复次数
        // animation1.setRepeatMode(Animation.REVERSE);//设置反方向执行
        // ib_add.setAnimation(animation1);
        /** 开始动画 */
        //  animation1.startNow();




        AlphaAnimation alphaAnimation_bs = new AlphaAnimation(1.0f, 0.3f);
        //设置动画持续时长
        alphaAnimation_bs.setDuration(3000);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
      //  alphaAnimation_bs.setFillAfter(true);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
       // alphaAnimation_bs.setFillBefore(true);
        //设置动画的重复模式：反转REVERSE和重新开始RESTART
        alphaAnimation_bs.setRepeatMode(AlphaAnimation.REVERSE);
        //设置动画播放次数
        alphaAnimation_bs.setRepeatCount(Integer.MAX_VALUE);
        //开始动画
        ib_add.startAnimation(alphaAnimation_bs);
        //清除动画
      //  ib_add.clearAnimation();
        //同样cancel()也能取消掉动画
      //  alphaAnimation_bs.cancel();





        //找到imageview的背景资源 这个背景就是一个帧动画 强转成帧动画对应的AnimationDrawable对象
        final AnimationDrawable anim = (AnimationDrawable) img_loading.getBackground();

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //让图片文字提示消失
                img_info.setVisibility(View.INVISIBLE);

                //加载动画出现
                img_loading.setVisibility(View.VISIBLE);

                //开启圆圈加载动画
                anim.start();




/*
                /** 设置旋转动画 */
                       final RotateAnimation animation_add =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
                                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                      animation_add.setDuration(3000);//设置动画持续时间
/** 常用方法 */
                       animation_add.setRepeatCount(Integer.MAX_VALUE);
                  animation_add.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
//animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状态
//animation.setStartOffset(long startOffset);//执行前的等待时间
                    ib_add.setAnimation(animation_add);
/** 开始动画 */
                     animation_add.startNow();



                ScaleAnimation animation_breath =new ScaleAnimation(1.0f, 0.7f, 1.0f, 0.7f,
                                      Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
                            animation_breath.setDuration(800);//设置动画持续时间
                        //停留在缩放后的状态
                        //  animation.setFillAfter(true);
                //设置呼吸循环

                    //无限循环
                  animation_breath.setRepeatCount(Integer.MAX_VALUE);
                //设置执行的模式 RESTART为结束后重新开始，
           //     animation_breath.setRepeatMode(Animation.RESTART);
//        //设置执行的模式为按原来的轨迹逆向返回
                animation_breath.setRepeatMode(Animation.REVERSE);
                //无限循环
         //       animation_breath.setRepeatMode(Animation.INFINITE);

                //添加动画在控件上
                ib_add.setAnimation(animation_breath);
                /** 开始缩放动画 */
                animation_breath.startNow();



/*
                /** 设置旋转动画 */
         //       final RotateAnimation animation_circle =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
        //                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
          //      animation_circle.setDuration(3000);//设置动画持续时间
/** 常用方法 */
         //       animation_circle.setRepeatCount(Integer.MAX_VALUE);
              //  animation_circle.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
//animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状态
//animation.setStartOffset(long startOffset);//执行前的等待时间
            //    img_loading.setAnimation(animation_circle);
/** 开始动画 */
           //     animation_circle.startNow();



                //延迟两秒以假设压缩用时
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //关闭圆圈加载动画
                        anim.stop();
                //        animation_circle.cancel();

                        //让圆圈加载动画消失
                        img_loading.setVisibility(View.INVISIBLE);

                        //图片对比框出现
                        vs_view.setVisibility(View.VISIBLE);

                        //总结框出现
                       summary.setVisibility(View.VISIBLE);

                        animation_add.cancel();


                        Animation mScaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f,
                                0.5f,// 整个屏幕就0.0到1.0的大小//缩放
                                Animation.INFINITE, 0.5f,
                                Animation.INFINITE, 0.5f);

                        mScaleAnimation.setDuration(1000);
                        mScaleAnimation.setFillAfter(true);
                        Animation mTranslateAnimation = new TranslateAnimation(0, 120, 0, 800);// 移动
                        mTranslateAnimation.setDuration(1000);
                        AnimationSet mAnimationSet = new AnimationSet(false);
                        mAnimationSet.addAnimation(mScaleAnimation);
                        mAnimationSet.setFillAfter(true);
                        mAnimationSet.addAnimation(mTranslateAnimation);
                        ib_add.startAnimation(mAnimationSet);


                        Animation mScaleAnimation_VS = new ScaleAnimation(0.0f, 1.0f, 0.0f,
                                1.0f,// 整个屏幕就0.0到1.0的大小//缩放
                                Animation.INFINITE, 0.5f,
                                Animation.INFINITE, 0.5f);
                        Animation mTranslateAnimation_VS = new TranslateAnimation(100, 0, 0, 0);// 移动

                        mScaleAnimation_VS.setDuration(500);
                        mScaleAnimation_VS.setFillAfter(true);
                        mTranslateAnimation_VS.setDuration(500);

                        AnimationSet mAnimationSet_VS = new AnimationSet(false);
                        mAnimationSet_VS.addAnimation(mScaleAnimation_VS);
                        mAnimationSet_VS.setFillAfter(true);
                        mAnimationSet_VS.addAnimation(mTranslateAnimation_VS);
                        vs_view.startAnimation(mAnimationSet_VS);


                    }
                }, 3000);


            }
        });

}



    /**
     * 背景图片移动动画
     */
    public void runAnimation() {
       // final LinearLayout background =(LinearLayout)findViewById(R.id.background);
        final ImageView background =(ImageView)findViewById(R.id.background);

        final   ScaleAnimation big = new  ScaleAnimation(1.0f, 2.5f, 1.0f, 2.5f,
                              Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    big.setDuration(5000);//设置动画持续时间
                //停留在缩放后的状态
                //  animation.setFillAfter(true);
       final  ScaleAnimation small = new ScaleAnimation(2.5f, 1.0f, 2.5f, 1.0f,
               Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        big.setDuration(3000);//设置动画持续时间
        small.setDuration(3000);//设置动画持续时间
     //   big.setDuration(25000);// 背景右移
      //  small.setDuration(25000);// 背景左边移动

        big.setFillAfter(true);
        small.setFillAfter(true);

        big.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                background.startAnimation(small);
            }
        });
        small.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                background.startAnimation(big);
            }
        });
        background.startAnimation(big);
    }
}