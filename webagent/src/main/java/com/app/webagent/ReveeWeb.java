package com.app.webagent;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.app.webagent.base.ReveeWebViewClient;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * webview的代理
 */

public class ReveeWeb {
    private WebView mWebView = null;

    /**
     * 设置webview
     */
    public ReveeWeb setWebView(WebView webView) {
        this.mWebView = webView;
        return this;
    }

    /**
     * 做一些初始化的准备
     */
    public ReveeWeb prepare(Context context) {
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setAlwaysDrawnWithCacheEnabled(true);
        mWebView.setAnimationCacheEnabled(true);
        mWebView.setDrawingCacheBackgroundColor(Color.WHITE);
        mWebView.setDrawingCacheEnabled(true);
        mWebView.setWillNotCacheDrawing(false);
        mWebView.setSaveEnabled(true);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // 图片延迟加载，加快web的加载速度
        settings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= 19);
        // 根据网络状态改变缓存策略
        if (isNetworkConnected(context)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(context.getCacheDir().toString());

        mWebView.setWebViewClient(new ReveeWebViewClient());
        return this;
    }

    /**
     * 判断网络链接是否正常
     *
     * @param context 上下文
     * @return
     */
    private boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    /**
     * 获取settings对象
     */
    public WebSettings getWebSettings() {
        return mWebView.getSettings();
    }

    /**
     * 设置webViewClient
     */
    public ReveeWeb setWebViewClient(ReveeWebViewClient reveeWebViewClient) {
        mWebView.setWebViewClient(reveeWebViewClient);
        return this;
    }

    public ReveeWeb setWebChromeClient(WebChromeClient webChromeClient) {
        mWebView.setWebChromeClient(webChromeClient);
        return this;
    }

    /**
     * 加载页面
     */
    public void load(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 加载js的方法
     *
     * @param name 方法名
     * @param strs 参数的数组
     */
    public void loadJs(String name, String[] strs) {
        String params = "";
        if (strs != null && strs.length > 0) {
            for (String str : strs) {
                params += "'" + str + "',";
            }
            params = params.substring(0, params.length() - 1);
        }
        mWebView.loadUrl("javascript:" + name + "(" + params + ")");
    }

    /**
     * 加载js的方法
     *
     * @param name          方法名
     * @param valueCallback 返回值的回调
     */
    public void loadJs(String name, ValueCallback<String> valueCallback) {
        mWebView.evaluateJavascript(name, valueCallback);
    }

    /**
     * 添加网页接口回调
     *
     * @param object
     * @param s
     * @return
     */
    public ReveeWeb addJavascriptInterface(Object object, String s) {
        mWebView.addJavascriptInterface(object, s);
        return this;
    }

    /**
     * 网页回退的方法
     */
    public boolean back() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    /**
     * 网页前进的方法
     */
    public boolean forward() {
        if (mWebView.canGoForward()) {
            mWebView.goForward();
            return true;
        }
        return false;
    }

    /**
     * 是否可以回退
     */
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    /**
     * 重新加载当前页面
     */
    public void refresh() {
        mWebView.reload();
    }

    /**
     * 生命周期回调
     */
    public void onResume() {
        mWebView.onResume();
    }

    /**
     * 生命周期回调
     */
    public void onPause() {
        mWebView.onPause();
    }

    /**
     * 生命周期回调
     */
    public void destroy() {
        mWebView.destroy();
    }

    /**
     * 设置ua的方法
     */
    public void setUserAgent(String userAgent) {
        getWebSettings().setUserAgent(userAgent);
    }

}
