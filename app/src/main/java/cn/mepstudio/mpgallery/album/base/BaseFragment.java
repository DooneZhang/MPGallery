package cn.mepstudio.mpgallery.album.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import butterknife.ButterKnife;

/**
 * Created by Rick Ge on 2016/12/6.
 */

public abstract class BaseFragment extends Fragment {
    protected View mRoot;
    private Bundle mBundle;
    private RequestManager mImgLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    protected void initBundle(Bundle bundle) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 如果mRoot不为空，那么不需要重新创建，保持Fragment已有的状态。
        if (mRoot == null) {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            // Do something
            onBindViewBefore(mRoot);
            // Bind view
            ButterKnife.bind(this, mRoot);
            // Get savedInstanceState
            if(savedInstanceState != null){
                onRestartInstance(savedInstanceState);
            }

            initWidget(mRoot);
            initData();
        }

        return mRoot;
    }

    protected abstract int getLayoutId();

    /**
     * 获取一个图片加载管理器
     *
     * @return RequestManager
     */
    public synchronized RequestManager getImgLoader() {
        if (mImgLoader == null)
            mImgLoader = Glide.with(this);
        return mImgLoader;
    }

    protected void onRestartInstance(Bundle bundle) {
    }

    protected void onBindViewBefore(View root) {
    }


    protected void initData() {
    }

    protected void initWidget(View root) {
    }
}
