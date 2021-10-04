package com.quest.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.quest.R;


/**
 * Created by CISS31 on 4/12/2018.
 */

public class WebViewActivity extends AppCompatActivity  {
    private WebView myWebView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentManager fragmentManager;
    CardView cardView;
    ImageView btn_back,home_icon;
    String URL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        sharedPreferences=getSharedPreferences("Report",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        URL=sharedPreferences.getString("URL","");



        myWebView = (WebView) findViewById(R.id.webView);


        // Make sure we handle clicked links ourselves
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // we handle the url ourselves if it's a network url (http / https)
                return ! URLUtil.isNetworkUrl(url);
            }
        });

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        if (myWebView != null && URL != null) {
            myWebView.loadData(URL,"text/html", null);
        }
    }

}
