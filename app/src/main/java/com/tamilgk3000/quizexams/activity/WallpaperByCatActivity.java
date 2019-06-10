package com.tamilgk3000.quizexams.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.tamilgk3000.quizexams.AdapterWallpaper;
import com.tamilgk3000.quizexams.Constant;
import com.tamilgk3000.quizexams.EndlessRecyclerViewScrollListener;
import com.tamilgk3000.quizexams.ItemWallpaper;
import com.tamilgk3000.quizexams.LatestWallListener;
import com.tamilgk3000.quizexams.LoadLatestWall;
import com.tamilgk3000.quizexams.R;
import com.tamilgk3000.quizexams.RecyclerViewClickListener;


import java.util.ArrayList;
import java.util.List;

public class WallpaperByCatActivity extends AppCompatActivity {

    public static final String URL_WALLPAPER_BY_CAT = "http://www.sportspider.in/gallery_gifs/web/api.php?cat_id=";
    public static final int NUMBER_OF_ADS = 1;
    private static Boolean isLoadMore = false;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterWallpaper adapter;
    ArrayList<ItemWallpaper> arrayList;
    Boolean isOver = false, isScroll = false;
    TextView textView_empty;
    LoadLatestWall loadWallpaper;
    int page = 1;
    GridLayoutManager grid;
    String cid;
    InterstitialAd interstitial;
    private AdLoader adLoader;
    private List<Object> dataCombined;

    public void showvideo() {
        // Show the ad if it's ready
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        } else {
            if (interstitial != null) {
                interstitial.loadAd(new AdRequest.Builder().build());
            }

        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showvideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.intersitials));
        interstitial.loadAd(new AdRequest.Builder().build());
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (interstitial != null) {
                    interstitial.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wall_activity_wall_by_cat);


        if (getIntent() != null) {
            cid = getIntent().getStringExtra("cat_id");
        } else {
            cid = "1";
        }
        toolbar = this.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arrayList = new ArrayList<>();
        if (dataCombined == null || !isLoadMore) {
            dataCombined = new ArrayList<>();
        }

        textView_empty = findViewById(R.id.tv_empty_wallcat);

        /*recyclerView = findViewById(R.id.rv_wall_by_cat);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(WallpaperByCatActivity.this, 1);
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

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
        });*/
        recyclerView = findViewById(R.id.rv_wall_by_cat);
        recyclerView.setHasFixedSize(true);
        grid = new GridLayoutManager(WallpaperByCatActivity.this, 1);
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? grid.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(grid);

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;


    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getWallpaperData() {
        if (isNetworkAvailable()) {
            if (isNetworkAvailable()) {
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
    }

    public void setAdapter() {

        dataCombined.clear();
        for (int i = 0; i < arrayList.size(); i++) {


//            if ((i % 4 == 0 && i != 0)) {
//                for (UnifiedNativeAd ad : mNativeAds) {
//                    dataCombined.add(i, ad);
//                }
//            }
            dataCombined.add(arrayList.get(i));

        }


        if (!isScroll && !isLoadMore) {

            adapter = new AdapterWallpaper(WallpaperByCatActivity.this, dataCombined, new RecyclerViewClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(WallpaperByCatActivity.this, WallPaperDetailsActivity.class);
                    intent.putExtra("pos", position);
                    Constant.arrayList.clear();
                    Constant.arrayList.addAll(arrayList);
                    startActivity(intent);
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
            textView_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textView_empty.setVisibility(View.GONE);
        }
    }
}
