package com.tamilgk3000.quizexams.ui;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tamilgk3000.quizexams.activity.SplashActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tamilgk3000.quizexams.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CustomeWebView extends AppCompatActivity {

    public WebView webView;
    int activity;
    ProgressDialog progressDialog;
    String url = null;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.webview);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        webView = findViewById(R.id.webView1);

        findViewById(R.id.appbarlayout).setVisibility(View.GONE);

        mAdView = (AdView) findViewById(R.id.adView);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitials));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd.loadAd(adRequest);


        progressDialog = new ProgressDialog(CustomeWebView.this);
        progressDialog.setMessage("Loading, Please Wait");
        progressDialog.show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        webView.setWebViewClient(new CustomClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                getSupportActionBar().setTitle(progress + "% Loading...");
                setProgress(progress); //Make the bar disappear after URL is loaded
                if (progress == 100) {
                    getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
                    progressDialog.dismiss();
                }
            }
        });

        try {
            url = getIntent().getStringExtra("openURL");
        } catch (NullPointerException e) {
            e.printStackTrace();
            url = "http://www.appsarasan.com/";
        }

        activity = getIntent().getIntExtra("FromActivity", 0);
        if (activity == 1) {
            mAdView.setVisibility(View.GONE);
        } else {
            mAdView.setVisibility(View.VISIBLE);
        }
        if (url != null)
            webView.loadUrl(url);

        if (getIntent().getStringExtra("Share") != null) {
            if (getIntent().getStringExtra("Share").equals("share_image")) {
                new RetrieveBitmap().execute(url);
            }
        }
    }

    public void showInterstitial() {
        // Show the ad if it's ready
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void onBackPressed() {
        showInterstitial();
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {
            if (activity == 1) {
                finish();
            } else if (activity == 0) {
                startActivity(new Intent(CustomeWebView.this, SplashActivity.class));
                finish();
            } else {
                onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap getBitmap(String fileurl) {
        try {
            Bitmap bitmap = null;
            URL myFileUrl = new URL(fileurl);
            String path = myFileUrl.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            File dir = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.app_name) + "/Notification");
            dir.mkdirs();
            File file = new File(dir, fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class RetrieveBitmap extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CustomeWebView.this);
            progressDialog.setMessage("Images Sharing....");
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                URL myFileUrl = new URL(urls[0]);
                String path = myFileUrl.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/" + getResources().getString(R.string.app_name) + "/Notification");
                dir.mkdirs();
                File file = new File(dir, fileName);

                if (!file.exists()) {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    FileOutputStream fos = new FileOutputStream(file);
                    byte data[] = new byte[4096];
                    int count;
                    while ((count = is.read(data)) != -1) {
                        if (isCancelled()) {
                            is.close();
                            return null;
                        }
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                    fos.close();
                    return String.valueOf(myFileUrl);
                } else {
                    return String.valueOf(myFileUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String fileurl) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if (fileurl != null) {
                // Construct a ShareIntent with link to image
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),
                        getBitmap(fileurl), "Share Image", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
                progressDialog.dismiss();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class DownloadImages extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CustomeWebView.this);
            progressDialog.setMessage("Downloading Images....");
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            try {
                URL myFileUrl = new URL(urls[0]);
                String path = myFileUrl.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/" + getResources().getString(R.string.app_name) + "/Notification");
                dir.mkdirs();
                File file = new File(dir, fileName);

                if (!file.exists()) {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    FileOutputStream fos = new FileOutputStream(file);
                    byte data[] = new byte[4096];
                    int count;
                    while ((count = is.read(data)) != -1) {
                        if (isCancelled()) {
                            is.close();
                            return null;
                        }
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                    fos.close();
                    return "image";
                } else {
                    return "already";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String bitmap) {
            // TODO: check this.exception
            // TODO: do something with the feed
            progressDialog.dismiss();
            if (bitmap.equals("image")) {
                Toast.makeText(getApplicationContext(), "Download Image Completed", Toast.LENGTH_SHORT).show();
            } else if (bitmap.equals("already")) {
                Toast.makeText(getApplicationContext(), "Already Image Downloaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Download Image failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class CustomClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("image:")) {
                new RetrieveBitmap().execute(url.replace("image:", ""));
                return true;
            } else if (url.startsWith("download:")) {
                new DownloadImages().execute(url.replace("download:", ""));
                return true;
            } else if ((Uri.parse(url).getHost().equals("www.play.google.com"))
                    || (Uri.parse(url).getHost().equals("play.google.com"))) {

                String str = Uri.parse(url).getQuery();
                String requiredStr = str.substring(str.indexOf("com"));

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + requiredStr)));
                    return true;

                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + requiredStr)));
                    return true;
                }
            } else if ((Uri.parse(url).getHost().equals("www.youtube.com"))
                    || (Uri.parse(url).getHost().equals("youtu.be"))) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + extractYTId(url)));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + extractYTId(url)));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
                return true;
            } else if (url.contains("http")) {
                view.loadUrl(url);
                return false;
            } else {
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        }
    }
}