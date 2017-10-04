package com.example.w10.reddit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by W10 on 9/28/2017.
 */

public class WebviewActivity extends AppCompatActivity{

    private static final String LOG_TAG = "WebviewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webview = (WebView) findViewById(R.id.webview);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.webview_progress_bar);
        final TextView loadingTextView = (TextView) findViewById(R.id.webview_loading_text);

        progressBar.setVisibility(View.VISIBLE);
        loadingTextView.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(url);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                loadingTextView.setVisibility(View.GONE);
            }
        });
    }
}
