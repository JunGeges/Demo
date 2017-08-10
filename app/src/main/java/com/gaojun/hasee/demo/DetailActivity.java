package com.gaojun.hasee.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class DetailActivity extends AppCompatActivity {
    private WebView mWebView;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mWebView =new WebView(getApplicationContext());
        mRelativeLayout =(RelativeLayout)findViewById(R.id.activity_detail);
        LinearLayoutCompat.LayoutParams layoutParams =new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);
        mRelativeLayout.addView(mWebView);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl("http://www.baidu.com");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView!=null){
            mWebView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            mWebView.clearHistory();

            //从容器移除再销毁
            mRelativeLayout.removeView(mWebView);
            mWebView.destroy();
            mWebView=null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
