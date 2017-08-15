package com.app.webagent.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.webagent.R;
import com.app.webagent.ReveeWeb;
import com.app.webagent.base.ReveeWebViewClient;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * 系定义的webview
 */

public class ReveeWebView extends RelativeLayout implements View.OnClickListener {

    private Context context;
    private WebView webView;
    private PageStatusLayout psl_web;
    private ImageView iv_refresh;
    private ImageView iv_back;
    private ProgressBar pb;
    private ReveeWeb reveeWeb;
    private TextView tv_refresh;

    public ReveeWebView(Context context) {
        this(context, null);
    }

    public ReveeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = View.inflate(context, R.layout.layout_revee_web, null);
        webView = (WebView) view.findViewById(R.id.wv);
        psl_web = (PageStatusLayout) view.findViewById(R.id.psl_web);
        iv_refresh = (ImageView) view.findViewById(R.id.iv_refresh);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        reveeWeb = new ReveeWeb();

        addView(view);
        // 一些初始化操作
        reveeWeb.setWebView(webView).prepare(context).setWebViewClient(new ReveeWebViewClient() {
            private Boolean isError = false;

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
    }

    /**
     * 生命周期回调
     */
    public void onPause() {
        reveeWeb.onPause();
    }

    /**
     * 生命周期回调
     */
    public void destroy() {
        reveeWeb.destroy();
    }

}
