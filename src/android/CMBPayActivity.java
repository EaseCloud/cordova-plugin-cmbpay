package com.aiunit.cmbpay;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import cn.easecloud.demo.R;

import org.apache.http.util.EncodingUtils;
import cmb.pb.util.CMBKeyboardFunc;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

public class CMBPayActivity extends Activity implements View.OnClickListener {

    private WebView webView;

    //进度条
    private ProgressBar pb;

    //post参数
    private  String url;
    private String jsonRequestData;

    private String filterUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmbpay);

        Intent i = getIntent();
        url = i.getStringExtra("url");

        jsonRequestData= i.getStringExtra("jsonRequestData");
        //初始化页面
        initView();
        //加载页面
        postUrl();

    }

    private void initView() {

        //返回图片、招行title
        ImageView ivBanner = (ImageView) findViewById(R.id.cmbpay_banner);
        ivBanner.setOnClickListener(this);

        pb = (ProgressBar) findViewById(R.id.cmbpay_progressbar);

        webView = (WebView) findViewById(R.id.cmbpay_webview);
        WebSettings webSettings = webView.getSettings();

        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(webSettings.LOAD_NO_CACHE);
        webSettings.setSaveFormData(false);

        //设置支持缩放
        webSettings.setSupportZoom(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent i;

                CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(CMBPayActivity.this);
                if(kbFunc.HandleUrlCall(view, url) == false)
                {
                    if (url.contains("cmbls/cmbKeyboard")) {
                        i = new Intent();
                        setResult(1,i);
                        finish();
                        return false;
                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                } else {
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap bitmap) {
            }

            public void onPageFinished(WebView view, String url) {
                filterUrl = url;
            }

        });


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    if (View.GONE == pb.getVisibility()) {
                        pb.setVisibility(View.VISIBLE);
                    }
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

    }

    @SuppressWarnings("deprecation")
    private void postUrl() {
        try {
            CookieSyncManager.createInstance(CMBPayActivity.this.getApplicationContext());
            CookieManager.getInstance().removeAllCookie();
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {

        }

        String body = "jsonRequestData=" + jsonRequestData;
        webView.postUrl(url, EncodingUtils.getBytes(body, "base64"));
        //byte[] data = text.getBytes("UTF-8");
        //String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        //webView.postUrl(url, );

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.cmbpay_banner:
                if(filterUrl.contains("MB_EUserP_PayOK")){
                    i = new Intent();
                    setResult(1,i);
                    finish();
                }else{
                    finish();
                }
                break;
        }
    }
}
