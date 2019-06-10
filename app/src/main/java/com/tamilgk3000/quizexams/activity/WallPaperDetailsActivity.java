package com.tamilgk3000.quizexams.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.internal.LikeButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tamilgk3000.quizexams.Constant;
import com.tamilgk3000.quizexams.InterAdListener;
import com.tamilgk3000.quizexams.LoadRating;
import com.tamilgk3000.quizexams.Methods;
import com.tamilgk3000.quizexams.R;
import com.tamilgk3000.quizexams.RatingListener;
import com.tamilgk3000.quizexams.ShareImages;
import com.tamilgk3000.quizexams.SharedPref;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class WallPaperDetailsActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;
    Toolbar toolbar;
    Methods methods;
    ViewPager viewpager;
    int position;
    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout ll_download, ll_share, ll_rate, ll_fb, ll_insta, ll_whatsapp;
    LikeButton button_fav;
    TextView tv_views, tv_tags, tv_downloads, rating_details;
    LoadRating loadRating;
    Dialog dialog_rate;
    CoordinatorLayout coordinatorLayout;
    SharedPref sharedPref;
    String path,array;
    Uri uri;
    URL myFileUrl;
    File file;
    int counter = 1;
//    ArrayList<ItemWallpaper> arrayList;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.wall_activity_wallpaper_details);

        mInterstitialAd = new InterstitialAd(WallPaperDetailsActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.intersitials));

//        toolbar = findViewById(R.id.toolbar_wall_details);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = 55;
            toolbar.setLayoutParams(params);
        }*/


        sharedPref = new SharedPref(this);

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                switch (type) {
                    case "download":
                        new SaveTask("save").execute(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
                        break;
                    case "share":
                        new SaveTask("share").execute(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
                        break;
                    case "set":
                        new SaveTask("set").execute(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
                        break;
                }
            }
        });

       methods.forceRTLIfSupported(getWindow());
       methods.setStatusColor(getWindow());

//        toolbar.setTitle("");
//        this.setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        position = getIntent().getIntExtra("pos", 0);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.ll_hideshow));
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        coordinatorLayout = findViewById(R.id.bgLayout);
//        button_fav = findViewById(R.id.button_wall_fav);
        ll_download = findViewById(R.id.ll_download);
        ll_share = findViewById(R.id.ll_share);
        ll_fb = findViewById(R.id.ll_facebook);
        ll_insta = findViewById(R.id.ll_instagram);
        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        ll_rate = findViewById(R.id.ll_rate);
        loadViewed(position);

        ll_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareImages(WallPaperDetailsActivity.this,
                        "facebook").execute(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
            }
        });
        ll_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareImages(WallPaperDetailsActivity.this, "instagram")
                        .execute(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
            }
        });
        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareImages(WallPaperDetailsActivity.this, "whatsapp")
                        .execute(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
            }
        });


        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewpager = findViewById(R.id.vp_wall_details);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(position);

        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    methods.showInter(0, "download");
                }
            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPer()) {
                    methods.showInter(0, "share");
                }
            }
        });


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                position = viewpager.getCurrentItem();

                loadViewed(position);
                if (counter == 5) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    counter = 1;
                } else {
                    if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adRequest);
                    }
                    counter++;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int position) {
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallpaper, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_info:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public Bitmap getBitmap() {
        try {
            myFileUrl = new URL(Constant.arrayList.get(viewpager.getCurrentItem()).getImage());
            Bitmap bitmap = null;
            String path = myFileUrl.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            File dir = new File(Environment.getExternalStorageDirectory()
                    + "/" + getResources().getString(R.string.app_name) + "/Wallpapers");
            dir.mkdirs();
            file = new File(dir, fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private Bitmap ProcessingBitmap(Bitmap bm1) {
        Bitmap newBitmap = null;

        Bitmap.Config config = bm1.getConfig();
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }

        newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
        Canvas newCanvas = new Canvas(newBitmap);

        newCanvas.drawBitmap(bm1, 0, 0, null);

        String captionString = "Download Ula on Play Store";
        if (captionString != null) {

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.WHITE);
            paintText.setTextSize(35);
            paintText.setTextAlign(Paint.Align.LEFT);
            paintText.setStyle(Paint.Style.STROKE);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

            Rect rectText = new Rect();
            paintText.getTextBounds(captionString, 0, captionString.length(), rectText);

            newCanvas.drawText(captionString, 0, rectText.height(), paintText);
        }
        return newBitmap;
    }

    private void loadViewed(int pos) {
       if (isNetworkAvailable()) {
           new MyTask("").execute(Constant.URL_WALLPAPER +Constant.arrayList.get(pos).getId(), String.valueOf(pos));
      }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void loadRatingApi(String rate) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(WallPaperDetailsActivity.this);
        progressDialog.setMessage("loading");

        loadRating = new LoadRating(new RatingListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message, float rating) {
                if (success.equals("true")) {
                   methods.showSnackBar(coordinatorLayout, message);
                    Toast.makeText(WallPaperDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                    if (!message.contains("already")) {
                        Constant.arrayList.get(viewpager.getCurrentItem()).setAverageRate(String.valueOf(rating));
                        Constant.arrayList.get(viewpager.getCurrentItem()).setTotalRate(String.valueOf(Integer.parseInt(Constant.arrayList.get(viewpager.getCurrentItem()).getTotalRate() + 1)));
                    }
                } else {
//                    methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.server_no_conn));
//                    Toast.makeText(WallPaperDetailsActivity.this, getResources().getString(R.string.server_no_conn), Toast.LENGTH_SHORT).show();
                }
                dialog_rate.dismiss();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        loadRating.execute(Constant.URL_RATING_1 +
                Constant.arrayList.get(viewpager.getCurrentItem()).getId() +
                Constant.URL_RATING_2 + deviceId + Constant.URL_RATING_3 + rate);
    }

    private void setTotalView(String views) {
//        tv_views.setText(methods.format(Double.parseDouble(views)));
    }

    private void setTotalDownloads(String downloads) {
        tv_downloads.setText(methods.format(Double.parseDouble(downloads)));
    }

    public Boolean checkPer() {
        if ((ContextCompat.checkSelfPermission(WallPaperDetailsActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return Constant.arrayList.size();

        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View imageLayout = inflater.inflate(R.layout.wall_layout_vp_wall, container, false);
            assert imageLayout != null;
            ImageView imageView = imageLayout.findViewById(R.id.iv_wall_details);
            final ProgressBar progressBar = imageLayout.findViewById(R.id.pb_wall_details);

            Picasso.get()
                    .load(Constant.arrayList.get(position).getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            container.addView(imageLayout, 0);
            return imageLayout;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public class SaveTask extends AsyncTask<String, String, String> {
        URL myFileUrl;
        String option;
        Bitmap bmImg = null;
        File file;
        private ProgressDialog pDialog;


        SaveTask(String option) {
            this.option = option;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(WallPaperDetailsActivity.this, AlertDialog.THEME_HOLO_LIGHT);
            if (option.equals("save")) {
                pDialog.setMessage(getResources().getString(R.string.downloading_wallpaper));
            } else {
                pDialog.setMessage(getResources().getString(R.string.please_wait));
            }
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                myFileUrl = new URL(args[0]);
                String path = myFileUrl.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/Wallpapers");
                dir.mkdirs();
                file = new File(dir, fileName);

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

                    if (option.equals("save")) {
                        MediaScannerConnection.scanFile(WallPaperDetailsActivity.this, new String[]{file.getAbsolutePath()},
                                null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {

                                    }
                                });
                    }
                    return "1";
                } else {
                    return "2";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String args) {
            if (args.equals("1") || args.equals("2")) {
                switch (option) {
                    case "save":
                        if (args.equals("2")) {
//                           methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.wallpaper_already_saved));
//                            Toast.makeText(WallPaperDetailsActivity.this, getResources().getString(R.string.wallpaper_already_saved), Toast.LENGTH_SHORT).show();
                        } else {
                            if (methods.isNetworkAvailable()) {
                           //   new MyTask("download").execute(Constant.URL_WALLPAPER_DOWNLOAD + Constant.arrayList.get(viewpager.getCurrentItem()).getId(), String.valueOf(viewpager.getCurrentItem()));
                            }
//                          methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.wallpaper_saved));
//                            Toast.makeText(WallPaperDetailsActivity.this, getResources().getString(R.string.wallpaper_saved), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "set":
                        bmImg = BitmapFactory.decodeFile(file.getAbsolutePath());
                        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            myWallpaperManager.setWallpaperOffsetSteps(1, 1);
                          myWallpaperManager.suggestDesiredDimensions(methods.getScreenWidth(), methods.getScreenHeight());
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            int height = metrics.heightPixels;
                            int width = metrics.widthPixels;
                            Bitmap yourbitmap = Bitmap.createScaledBitmap(bmImg, width, height, true);
                            myWallpaperManager.setBitmap(yourbitmap);
//                           methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.wallpaper_set));
//                            Toast.makeText(WallPaperDetailsActivity.this, getString(R.string.wallpaper_set), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                       /* Intent share = new Intent(Intent.ACTION_SEND);
                        path = MediaStore.Images.Media.insertImage(getContentResolver(),
                                ProcessingBitmap(getBitmap()), "Design", null);
                        uri = Uri.parse(path);
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.setType("image*//*");
                        share.putExtra(Intent.EXTRA_TEXT,
                                getString(R.string.get_more_wall)
                                        + "\n" + getString(R.string.app_name)
                                        + " - "
                                        + "https://play.google.com/store/apps/details?id="
                                        + getPackageName());
                        startActivity(Intent.createChooser(share, getResources().getString(R.string.share_wallpaper)));*/
                        pDialog.dismiss();
                        break;
                }
            } else {
//               methods.showSnackBar(coordinatorLayout, getResources().getString(R.string.please_try_again));
//                Toast.makeText(WallPaperDetailsActivity.this, getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<String, Void, String> {

        String downloads = "", type = "";

        MyTask(String type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String json = Methods.getJSONString(params[0]);
            try {
                JSONObject jOb = new JSONObject(json);
                JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objJson = jsonArray.getJSONObject(i);
                    downloads = objJson.getString(Constant.TAG_WALL_DOWNLOADS);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return params[1];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

}