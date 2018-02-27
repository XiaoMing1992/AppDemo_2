package guyuanjun.com.myappdemo.fragment.my.store.tv;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import guyuanjun.com.myappdemo.R;
import guyuanjun.com.myappdemo.utils.LogUtil;

public class TVActivity extends AppCompatActivity {
    private final String TAG = TVActivity.class.getSimpleName();

    private WebView webView;
    private ProgressBar mProgressBar;

    private final String URL = "http://www.iqiyi.com/dianshiju/";
    //private final String URL = "http://baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        initView();
        initData(URL);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar)findViewById(R.id.mProgressBar);
    }

    private void initData(final String url_str) {
        if (url_str == null || url_str.isEmpty())
            return;
        webView.loadUrl(url_str);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
/*            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return shouldOverrideUrlLoading(view, request.getUrl().toString());
            }*/

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
/*                view.loadUrl(url);
                return false;*/
                LogUtil.d(TAG, "url = "+ url);
                if(url.startsWith("http:") || url.startsWith("https:") ) {
                    view.loadUrl(url);
                    return true;
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if(intent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(intent, 0);
                    return false;
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    // 加载中
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });

        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存
        // settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
