package com.tamilgk3000.quizexams.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tamilgk3000.quizexams.ImageLoaderDefintion;
import com.tamilgk3000.quizexams.R;
import com.tamilgk3000.quizexams.database.DatabaseHandler;
import com.tamilgk3000.quizexams.database.ListViewSwipeGesture;
import com.tamilgk3000.quizexams.database.News;

import java.util.List;


public class NotificationActivity extends AppCompatActivity {


    DatabaseHandler db;

    List<News> contacts;
    SampleAdapter adapter;
    ListView list;
    LayoutInflater inflater;
    LinearLayout No_Notification;
    ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {
        @Override
        public void FullSwipeListView(int position) {
            // Call back function for second action
        }

        @Override
        public void HalfSwipeListView(int position) {

        }

        @Override
        public void LoadDataForScroll(int count) {
        }

        @Override
        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            try {
                for (int i : reverseSortedPositions) {
                    if (contacts.get(i).getID() != -1) {
                        db.deleteContact(contacts.get(i));
                        adapter.remove(contacts.get(i));
                        adapter.notifyDataSetChanged();
                        contacts.remove(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void OnClickListView(int position) {
            try {
                if (contacts.get(position).getID() != -1) {
                    if (contacts.get(position).getLink() != null) {
                        Intent intent = new Intent(NotificationActivity.this,
                                CustomeWebView.class);
                        intent.putExtra("title", contacts.get(position).getName());
                        intent.putExtra("openURL", contacts.get(position).getLink());
                        intent.putExtra("FromActivity", 2);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification");

        list = (ListView) findViewById(R.id.list);
        No_Notification = (LinearLayout) findViewById(R.id.no_notify);

        db = new DatabaseHandler(this);
        ImageLoaderDefintion.initImageLoader(this);


        AdView adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);


        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        contacts = db.getAllContacts(DatabaseHandler.TABLE_NEWS);
        if (contacts.size() != 0) {
            list.setVisibility(View.VISIBLE);
            No_Notification.setVisibility(View.GONE);
            ListViewSwipeGesture touchListener = new ListViewSwipeGesture(list,
                    swipeListener, this);
            touchListener.SwipeType = ListViewSwipeGesture.Dismiss;
            touchListener.HalfDrawable = getResources().getDrawable(R.drawable.ic_delete);
            list.setOnTouchListener(touchListener);
        } else {
            list.setVisibility(View.GONE);
            No_Notification.setVisibility(View.VISIBLE);
        }


        adapter = new SampleAdapter(this);
        if (adapter != null)
            list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NotificationActivity.this);
            alertDialogBuilder.setTitle(R.string.notification_delete);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.deleteAllNews();
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class SampleAdapter extends ArrayAdapter<News> {
        LayoutInflater inflater;
        List<News> DataList;

        SampleAdapter(Context context) {
            super(context, 0);
            inflater = LayoutInflater.from(context);
            this.DataList = contacts;
        }

        @Override
        public int getCount() {
            return DataList.size();
        }

        @SuppressLint("ViewHolder")
        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.cat_swipe_layout, parent,
                    false);
            if (convertView != null) {
                TextView title = (TextView) convertView.findViewById(R.id.title); // title
                TextView branch = (TextView) convertView.findViewById(R.id.msg);
                TextView dateformat = (TextView) convertView.findViewById(R.id.dateformat);// title
                ImageView v = (ImageView) convertView.findViewById(R.id.img);
                RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
                try {
                    title.setText((DataList.get(position).getName()));
                    branch.setText(DataList.get(position).getDate());
                    dateformat.setText(DataList.get(position).getDateformat());
                    if (DataList.get(position).getImage() != null) {
                        v.setVisibility(View.VISIBLE);
                        ImageLoader imgloader = ImageLoader.getInstance();
                        imgloader.displayImage(DataList.get(position).getImage(), v);
                    } else {
                        v.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return convertView;
        }
    }

}
