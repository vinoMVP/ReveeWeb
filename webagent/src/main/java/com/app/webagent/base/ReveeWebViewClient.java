package com.app.webagent.base;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ReveeWebViewClient extends WebViewClient {

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        if (!webView.getSettings().getLoadsImagesAutomatically()) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        }
    }
}
