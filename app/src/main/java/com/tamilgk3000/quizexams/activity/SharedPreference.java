package com.tamilgk3000.quizexams.activity;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String PREFERENCE_SHAREVALUE = "MY_pref";

    public static int getCoins(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREFERENCE_SHAREVALUE, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }


    public static int getQuestioninLevels(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREFERENCE_SHAREVALUE, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static int Coins(Activity activity, String key, int num) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREFERENCE_SHAREVALUE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.putInt(key, num);
        editor.commit();
        return num;
    }


    public static void CurrentQuestionLevels(Activity activity, String key, int num) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREFERENCE_SHAREVALUE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.putInt(key, num);
        editor.commit();
    }
    public static void game_level (Activity activity, String key, boolean num) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREFERENCE_SHAREVALUE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.putBoolean(key, num);
        editor.commit();
    }

    public static boolean get_game_lavel (Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                PREFERENCE_SHAREVALUE, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }



}