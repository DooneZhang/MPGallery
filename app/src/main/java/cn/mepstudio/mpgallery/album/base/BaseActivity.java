package cn.mepstudio.mpgallery.album.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import butterknife.ButterKnife;
import cn.mepstudio.mpgallery.R;

/**
 * Created by Rick Ge on 2016/12/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected RequestManager mImageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(initBundle(getIntent().getExtras())){
            setContentView(getContentView());
            ButterKnife.bind(this);
            initWindow();
            initWidget();
            initData();

        }
        else{
            finish();
        }
    }

    protected abstract int getContentView();

    protected boolean initBundle(Bundle bundle) {
        return true;
    }

    protected void initWindow() {
    }

    protected void initData() {
    }

    protected void initWidget() {
    }

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = Glide.with(this);
        return mImageLoader;
    }
}
