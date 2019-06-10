package com.tamilgk3000.quizexams.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.Message;
import com.adapter.MessagesAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tamilgk3000.quizexams.AdapterWallpaper;
import com.tamilgk3000.quizexams.AdapterWallpaper1;
import com.tamilgk3000.quizexams.EndlessRecyclerViewScrollListener;
import com.tamilgk3000.quizexams.Http.HttpServicesClass;
import com.tamilgk3000.quizexams.ImageLoaderDefintion;
import com.tamilgk3000.quizexams.ItemWallpaper;
import com.tamilgk3000.quizexams.LatestWallListener;
import com.tamilgk3000.quizexams.LoadLatestWall;
import com.tamilgk3000.quizexams.RecyclerViewClickListener;
import com.tamilgk3000.quizexams.ServerConfig;
import com.tamilgk3000.quizexams.ui.CustomeWebView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tamilgk3000.quizexams.ConnectionDetector;
import com.tamilgk3000.quizexams.R;
import com.tamilgk3000.quizexams.database.DatabaseHandler;
import com.tamilgk3000.quizexams.ui.NotificationActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import hotchemi.android.rate.StoreType;
import me.relex.circleindicator.CircleIndicator;


public class HomeActivity extends AppCompatActivity {
    SharedPreferences sp1;
    ImageView Menu;
    AdView adView;
    RelativeLayout common;
    private int coins;
    NavigationView navigationView;
    DrawerLayout drawer;
    private boolean mSlideState;
    MessagesAdapter adp;

    MessagesAdapter dbAdp;

    RelativeLayout notification;
    TextView notification_count;
    ArrayList<Message> AllRecipes;

    DatabaseHandler handler;

    public void showvideo() {
        // Show the ad if it's ready
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    Random r = new Random();

    ViewPager mViewPager;
    CircleIndicator indicator;
    ArrayList<AddBanner> addBanners;


    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        String JSonResult;
        ProgressDialog progressDialog;
        ArrayList<AddBanner> studentList;

        public GetHttpResponse(Context context) {
            this.context = context;
            studentList = new ArrayList<AddBanner>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(ServerConfig.Banner_url);
            try {
                httpServicesClass.ExecutePostRequest();

                if (httpServicesClass.getResponseCode() == 200) {
                    JSonResult = httpServicesClass.getResponse();

                    if (JSonResult != null) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = new JSONArray(JSonResult);
                            JSONObject jsonObject;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);

                                studentList.add(new AddBanner(jsonObject.getString("sno"),
                                        jsonObject.getString("Image_Banner"),
                                        jsonObject.getString("Link")));
                            }
                        } catch (JSONException e) {

                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    return null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            mViewPager.setAdapter(new CustomPagerAdapter(HomeActivity.this, studentList));
            indicator.setViewPager(mViewPager);

            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage == studentList.size()) {
                        currentPage = 0;
                    }
                    mViewPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, 5000, 5000);
        }
    }
    int currentPage = 0;
    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        ArrayList<AddBanner> addBanners;

        public CustomPagerAdapter(Context context, ArrayList<AddBanner> studentList) {
            this.mContext = context;
            this.addBanners = studentList;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.banner_slidingimages_layout, collection, false);
            collection.addView(layout);
            RoundedImageView touchImageView = (RoundedImageView) layout.findViewById(R.id.image);
            try {
                ImageLoader imgloader = ImageLoader.getInstance();
                imgloader.displayImage(addBanners.get(position).getImage_Banner(), touchImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            touchImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gov_intent = new Intent(HomeActivity.this, CustomeWebView.class);
                    gov_intent.putExtra("title", getResources().getString(R.string.app_name));
                    gov_intent.putExtra("openURL", addBanners.get(position).getLink());
                    startActivity(gov_intent);
                }
            });

            return layout;
        }

        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        public int getCount() {
            return addBanners.size();
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        public CharSequence getPageTitle(int position) {
            return null;
        }

    }
    public static final String URL_WALLPAPER_BY_CAT = "http://www.sportspider.in/gallery_gifs/web/api.php?cat_id=";
    private static Boolean isLoadMore = false;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterWallpaper1 adapter;
    ArrayList<ItemWallpaper> arrayList;
    ProgressBar progressBar;
    Boolean isOver = false, isScroll = false;
    TextView textView_empty,title1,title2,title3,title4,title5,title6,title7;
    LoadLatestWall loadWallpaper;
    int page = 1;
    GridLayoutManager grid;
    InterstitialAd interstitial;
    private List<Object> dataCombined;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        arrayList = new ArrayList<>();
        if (dataCombined == null || !isLoadMore) {
            dataCombined = new ArrayList<>();
        }
        progressBar = findViewById(R.id.pb_wallcat);
        title1 = findViewById(R.id.title_1);
        title2 = findViewById(R.id.title_2);
        title3 = findViewById(R.id.title_3);
        title4 = findViewById(R.id.title_4);
        title5 = findViewById(R.id.title_5);
        title6 = findViewById(R.id.title_6);
        title7 = findViewById(R.id.title_7);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "alice_title_1_7.ttf");

        title1.setTypeface(face);
        title2.setTypeface(face);
        title3.setTypeface(face);
        title4.setTypeface(face);
        title5.setTypeface(face);
        title6.setTypeface(face);
        title7.setTypeface(face);


        recyclerView = findViewById(R.id.rv_wall_by_cat);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(HomeActivity.this, 1);
       /* grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(grid) {
            @Override
            public void onLoadMore(int p, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isScroll = true;
                            getWallpaperData();
                        }
                    }, 0);
                } else {
                    adapter.hideHeader();
                }
            }
        });

        getWallpaperData();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        ImageLoaderDefintion.initImageLoader(HomeActivity.this);

        addBanners = new ArrayList<AddBanner>();
        new GetHttpResponse(HomeActivity.this).execute();
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        notification_count = (TextView) findViewById(R.id.notification_count);


        notification = (RelativeLayout) findViewById(R.id.notification);
        handler = new DatabaseHandler(HomeActivity.this);
        if (handler.getContactsCount() != 0 && handler.getContactsCount() > 10) {
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText("10+");
        } else if (handler.getContactsCount() != 0 && handler.getContactsCount() < 10) {
            notification_count.setVisibility(View.VISIBLE);
            notification_count.setText("" + handler.getContactsCount());
        } else if (handler.getContactsCount() == 0) {
            notification_count.setVisibility(View.GONE);
        }



        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
            }
        });
        sp1 = getSharedPreferences("rating", 0);

        dbAdp = new MessagesAdapter(HomeActivity.this);
        dbAdp.createDatabase();
        dbAdp.open();
        dbAdp.close();

        final String PREFS_NAME = "MyPrefsFile";
        settings = getSharedPreferences(PREFS_NAME, 0);
        dialog1 = new Dialog(this);
        adp = new MessagesAdapter(HomeActivity.this);

        initDrawerMenu();

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(2) // default 10 times.
                .setRemindInterval(1) // default 1 day.
                .setShowLaterButton(true) // default true.
                .setDebug(false) // default false.
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(HomeActivity.class.getName(), Integer.toString(which));
                    }
                })
                .setMessage(R.string.rate_dialog_message)
                .setTitle(R.string.rate_dialog_title)
                .setTextLater(R.string.rate_dialog_cancel)
                .setTextNever(R.string.rate_dialog_no)
                .setTextRateNow(R.string.rate_dialog_ok)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
        // Show a dialog if meets conditions


        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.intersitials));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(getApplicationContext(), End_Splash.class));
                finish();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                startActivity(new Intent(getApplicationContext(), End_Splash.class));
                finish();
            }
        });

        adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(adRequest);
        ConnectionDetector cd = new ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            adView.setVisibility(View.GONE);
        } else {
            adView.setVisibility(View.VISIBLE);
        }
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                adView.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8489787360");
            }
        });

        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 10.3852067, 78.8170934, "Pudukkottai bsnl location");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        findViewById(R.id.view_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WallpaperByCatActivity.class)
                        .putExtra("cat_id","7"));
            }
        });
        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url2 = getResources().getString(R.string.facebook1);
                Intent intent2 = new Intent(HomeActivity.this, CustomeWebView.class);
                intent2.putExtra("openURL", url2);
                intent2.putExtra("title", getString(R.string.title_social));
                intent2.putExtra("FromActivity", 1);
                startActivity(intent2);
            }
        });
        findViewById(R.id.insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/?hl=en");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/?hl=en")));
                }
            }
        });
        Menu = (ImageView) findViewById(R.id.menu);
        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSlideState) {
                    drawer.closeDrawer(Gravity.END);
                } else {
                    drawer.openDrawer(Gravity.START);
                }
            }
        });

        ((RelativeLayout) findViewById(R.id.layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, tab_layout.class));
            }
        });

    }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
    private void initDrawerMenu() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true;//is Opened
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false;//is Closed
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.main_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                displayActivity(menuItem.getItemId());
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void displayActivity(int itemId) {
        switch (itemId) {
            case R.id.notifiy:
                Intent i4 = new Intent(getApplicationContext(), NotificationActivity.class); // Your list's Intent
                startActivity(i4);
                break;
            case R.id.drawer_privacy:
                String url = getResources().getString(R.string.privacypolicy);
                Intent intent = new Intent(HomeActivity.this, CustomeWebView.class);
                intent.putExtra("openURL", url);
                intent.putExtra("title", getString(R.string.privacy));
                intent.putExtra("FromActivity", 1);
                startActivity(intent);
                break;
            case R.id.facebook:
                String url2 = getResources().getString(R.string.facebook);
                Intent intent2 = new Intent(HomeActivity.this, CustomeWebView.class);
                intent2.putExtra("openURL", url2);
                intent2.putExtra("title", getString(R.string.title_social));
                intent2.putExtra("FromActivity", 1);
                startActivity(intent2);
                break;
            case R.id.nav_email:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email)});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback of our apps " + getPackageName());
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;

            case R.id.drawer_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_content) + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.drawer_rate:
                final String appName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                }
                break;
            case R.id.drawer_about:
                String url4 = getResources().getString(R.string.website);
                Intent intent4 = new Intent(HomeActivity.this, CustomeWebView.class);
                intent4.putExtra("openURL", url4);
                intent4.putExtra("FromActivity", 1);
                intent4.putExtra("title", getString(R.string.app_name));
                startActivity(intent4);
                break;
            case R.id.drawer_more_from_developer:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.store_name))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.Store_name_1))));
                }
                break;

            case R.id.setting:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.drawer_terms:
                String url5 = getResources().getString(R.string.terms_conditions);
                Intent intent5 = new Intent(HomeActivity.this, CustomeWebView.class);
                intent5.putExtra("FromActivity", 1);
                intent5.putExtra("openURL", url5);
                intent5.putExtra("title", getString(R.string.tems_conditions));
                startActivity(intent5);
                break;

        }
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            if (doubleBackToExitPressedOnce) {
                if (isConnectingToInternet() && interstitial.isLoaded()) {
                    showvideo();
                } else {
                    startActivity(new Intent(getApplicationContext(), End_Splash.class));
                    finish();
                }
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "ஆப்ஸ் விட்டு வெளியேற மீண்டும் கிளிக் செய்யவும்", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    // for permission android M (8.0)
    public static String[] ALL_REQUIRED_PERMISSION = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    Dialog dialog1;
    SharedPreferences settings;

    @SuppressWarnings("deprecation")
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivity.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isConnectingToInternet()) {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean agreed = sharedPreferences.getBoolean("agreed", false);
            if (!agreed) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("agreed", true);
                editor.commit();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (!isAllPermissionGranted(HomeActivity.this)) {
                        showDialogPermission();
                    }
                }
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (!isAllPermissionGranted(HomeActivity.this)) {
                    showDialogPermission();
                }
            }
        }
    }

    private void showDialogPermission() {
        if (!isAllPermissionGranted(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(ALL_REQUIRED_PERMISSION, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static boolean isAllPermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permission = ALL_REQUIRED_PERMISSION;
            if (permission.length == 0) return false;
            for (String s : permission) {
                if (ActivityCompat.checkSelfPermission(activity, s) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getWallpaperData() {
        if (isConnectingToInternet()) {
            loadWallpaper = new LoadLatestWall(new LatestWallListener() {
                @Override
                public void onStart() {
                    if (arrayList.size() == 0) {
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<ItemWallpaper> arrayListWall) {
                    if (arrayListWall.size() == 0) {
                        isOver = true;
                        try {
                            adapter.hideHeader();
                        } catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            setEmptTextView();
                            e.printStackTrace();
                        }
                    } else {
                        for (int i = 0; i < arrayListWall.size(); i++) {
                        }
                        page = page + 1;
                        arrayList.addAll(arrayListWall);
                        setAdapter();
                    }
                }
            });
            loadWallpaper.execute(URL_WALLPAPER_BY_CAT + 7 + "&page=" + page);
        }
    }

    public void setAdapter() {

        dataCombined.clear();
        for (int i = 0; i < arrayList.size(); i++) {

           /* if ((i % 4 == 0 && i != 0)) {
                dataCombined.add("ad");
            }*/
            dataCombined.add(arrayList.get(i));

        }


        if (!isScroll && !isLoadMore) {
            adapter = new AdapterWallpaper1(HomeActivity.this, dataCombined, new RecyclerViewClickListener() {
                @Override
                public void onClick(int position) {
                }
            });
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setEmptTextView();
        } else {
            adapter.notifyDataSetChanged();
            isLoadMore = false;
        }
    }

    private void setEmptTextView() {
        if (arrayList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
