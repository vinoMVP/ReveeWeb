package com.app.webagent.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.webagent.R;
import com.app.webagent.ReveeWeb;
import com.app.webagent.base.ReveeWebViewClient;

/**
 * 系定义的webview
 */

public class ReveeWebView extends RelativeLayout implements View.OnClickListener {

    private PageStatusLayout psl_web;
    private ImageView iv_refresh;
    private ImageView iv_back;
    private ProgressBar pb;
    private ReveeWeb reveeWeb;
    private TextView tv_refresh;
    private Boolean isError = false;
    private ReveeWebListener listener;

    public ReveeWebView(Context context) {
        this(context, null);
    }

    public ReveeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        View view = View.inflate(context, R.layout.layout_revee_web, null);
        WebView webView = (WebView) view.findViewById(R.id.wv);
        psl_web = (PageStatusLayout) view.findViewById(R.id.psl_web);
        iv_refresh = (ImageView) view.findViewById(R.id.iv_refresh);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        reveeWeb = new ReveeWeb();

        addView(view);
        // 一些初始化操作
        reveeWeb.setWebView(webView).prepare(context).setWebViewClient(new ReveeWebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String schemeUrl = "";
                if (url.contains("taobao") && !url.contains("login")) {
                    if (url.startsWith("https")) {
                        schemeUrl = url.replace("https", "taobao");
                    } else if (url.startsWith("http")) {
                        schemeUrl = url.replace("http", "taobao");
                    }
                } else {
                    schemeUrl = url;
                }
                Log.e("guoxing", "schemeUrl:" + schemeUrl);
                Log.e("guoxing", "url:" + url);
                if (!schemeUrl.startsWith("http")) {
                    if (startActivityByUri(context, schemeUrl)) {
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                isError = true;
                psl_web.showError();
                pb.setVisibility(GONE);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                isError = false;
                psl_web.showLoading();
            }

            @Override
            public void onPageFinished(WebView p0, String p1) {
                super.onPageFinished(p0, p1);
                if (!isError) {
                    psl_web.showContent();
                }

                if (reveeWeb.canGoBack()) {
                    iv_back.setVisibility(VISIBLE);
                } else {
                    iv_back.setVisibility(GONE);
                }

                pb.setVisibility(GONE);
            }

        }).setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                pb.setProgress(i);
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (s.contains("404") || s.contains("500") || s.contains("Error") || s.contains("找不到网页")) {
                    isError = true;
                    psl_web.showError();
                }
                if (listener != null) {
                    listener.onReceivedTitle(webView, s);
                }
            }
        });

        View errorView = View.inflate(context, R.layout.layout_web_error, null);
        tv_refresh = (TextView) errorView.findViewById(R.id.tv_refresh);

        psl_web.setErrorView(errorView);
        psl_web.setLoadingView(View.inflate(context, R.layout.layout_loading, null));
        psl_web.showContent();
        initEvent();
    }

    private void initEvent() {
        iv_back.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);
        tv_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_refresh || id == R.id.iv_refresh) {
            reveeWeb.refresh();
        } else if (id == R.id.iv_back) {
            reveeWeb.back();
        }
    }

    public boolean onBackPress() {
        return reveeWeb.back();
    }

    /**
     * 加载js的方法
     */
    public void loadJs(String name, String... params) {
        reveeWeb.loadJs(name, params);
    }

    /**
     * 加载js的方法，带返回值
     */
    public void loadJs(String name, ValueCallback<String> valueCallback) {
        reveeWeb.loadJs(name, valueCallback);
    }

    /**
     * 加载网页
     */
    public void loadUrl(String url) {
        reveeWeb.load(url);
    }

    /**
     * 生命周期回调
     */
    public void onResume() {
        reveeWeb.onResume();
        reveeWeb.getWebSettings().setJavaScriptEnabled(true);
    }

    /**
     * 生命周期回调
     */
    public void onPause() {
        reveeWeb.onPause();
        reveeWeb.getWebSettings().setJavaScriptEnabled(false);
    }

    /**
     * 生命周期回调
     */
    public void destroy() {
        reveeWeb.destroy();
    }

    public void reload() {
        reveeWeb.refresh();
    }

    /**
     * 添加js交互接口
     *
     * @param jsToJava 接口对象
     * @param s        js调用的名称
     */
    public void addJavascriptInterface(Object jsToJava, String s) {
        reveeWeb.addJavascriptInterface(jsToJava, s);
    }

    /**
     * 获取ua
     *
     * @return ua字符串
     */
    public String getUserAgent() {
        return reveeWeb.getWebSettings().getUserAgentString();
    }

    /**
     * 设置ua
     *
     * @param s ua的字符串
     */
    public void setUserAgent(String s) {
        reveeWeb.setUserAgent(s);
    }

    /**
     * 前进
     */
    public void forward() {
        reveeWeb.forward();
    }

    /**
     * webview的回调接口
     */
    public interface ReveeWebListener {

        /**
         * 返回标题的回调
         *
         * @param webView
         * @param title   标题
         */
        void onReceivedTitle(WebView webView, String title);
    }

    /**
     * 设置web的listener
     *
     * @param listener
     */
    public void setReveeWebListener(ReveeWebListener listener) {
        this.listener = listener;
    }

    /**
     * 通过url启动activity
     *
     * @param context 上下文
     * @param url     url
     * @return 是否启动成功
     */
    private boolean startActivityByUri(Context context, String url) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
