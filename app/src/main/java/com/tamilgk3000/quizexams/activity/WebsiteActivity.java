package com.tamilgk3000.quizexams.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tamilgk3000.quizexams.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by AswinBalaji on 22-Aug-17.
 */

public class WebsiteActivity extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    public WebView webView;
    TextView textView;

    ProgressDialog mProgressDialog;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webpage);

        textView=(TextView)findViewById(R.id.toolbar_title);
        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close on ic_back button pressed
                onBackPressed();
            }
        });
        webView = (WebView) findViewById(R.id.webpage);


        mProgressDialog = new ProgressDialog(this);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addKeyword("recipes")
                .addKeyword("computer")
                .addKeyword("utilities")
                .addKeyword("mobile")
                .addKeyword("communication")
                .addKeyword("language")
                .addKeyword("education")
                .addKeyword("baby").addKeyword("world")
                .addKeyword("offers").build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitials));
        //prepare ads
        AdRequest adRequest2 = new AdRequest.Builder()
                .addKeyword("recipes")
                .addKeyword("computer")
                .addKeyword("utilities")
                .addKeyword("mobile")
                .addKeyword("communication")
                .addKeyword("language")
                .addKeyword("education")
                .addKeyword("baby").addKeyword("world")
                .addKeyword("offers").build();
        mInterstitialAd.loadAd(adRequest2);


        mProgressDialog.setMessage("Loading...");

        webView.setWebViewClient(new CustomClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        String url;

        try {
            Bundle where = getIntent().getExtras();
            url = where.getString("web_link");
            textView.setText(where.getString("web_title"));
        } catch (NullPointerException e) {
            url = "http://www.appsarasan.com";
            textView.setText("Apps Arasan");

        }

        webView.loadUrl(url);

    }

    private class CustomClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Log.d(TAG, "Loading URL: " + url);

            mProgressDialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressDialog.cancel();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if ((Uri.parse(url).getHost().equals("www.play.google.com"))
                    || (Uri.parse(url).getHost().equals("play.google.com"))) {

                // Get the Query part
                String str = Uri.parse(url).getQuery();
                //find the string "com" and get everthing after that string
                String requiredStr = str.substring(str.indexOf("com"));
                Log.i("url", requiredStr);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + requiredStr)));
                    return true;

                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + requiredStr)));
                    return false;
                }
            }
            return false;
        }

    }

    public void showInterstitial() {
        // Show the ad if it's ready
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void onBackPressed() {
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            finish();
            showInterstitial();
        }
    }
}
