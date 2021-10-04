package com.quest.fragment;
  
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.quest.R;

public class MainActivity extends AppCompatActivity {
    String url;
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);

       WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // we handle the url ourselves if it's a network url (http / https)
                return ! URLUtil.isNetworkUrl(url);
            }
        });

        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setSupportZoom(true);
        mywebview.getSettings().setBuiltInZoomControls(true);


        mywebview.loadUrl("https://www.payfast.co.za/eng/process/payment?p=ss7c9tobnlavvagth4u996oph1");


    }  
}  