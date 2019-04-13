package cn.mepstudio.mpgallery.album.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.mepstudio.mpgallery.R;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.album_activity_web);

        //方法2：在Activity的layout文件里添加webview控件：
        WebView webview = (WebView) findViewById(R.id.website);
        webview.loadUrl("https://wappass.baidu.com");
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient()
        {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings = webview.getSettings();
        settings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 6.0; zh-cn) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.9 Mobile Safari/537.36");//添加UA,  “app/XXX”：是与h5商量好的标识，h5确认UA为app/XXX就认为该请求的终端为App
        settings.setJavaScriptEnabled(true);

        //设置参数
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);// 设置缓存


    }
}
