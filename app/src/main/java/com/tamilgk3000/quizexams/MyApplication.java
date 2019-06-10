package com.tamilgk3000.quizexams;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tamilgk3000.quizexams.database.DatabaseHandler;
import com.tamilgk3000.quizexams.database.News;
import com.tamilgk3000.quizexams.notification.ExampleNotificationOpenedHandler;
import com.tamilgk3000.quizexams.notification.ExampleNotificationReceivedHandler;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AswinBalaji on 20-Oct-16.
 */
public class MyApplication extends Application {

    DatabaseHandler db1;
    String currentDateandTime;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        FacebookSdk.setLimitEventAndDataUsage(this, true);
        AppEventsLogger.activateApp(this);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler(this))
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(this))
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }


}
