package com.tamilgk3000.quizexams;


import java.util.ArrayList;

public interface LatestWallListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemWallpaper> arrayListCat);
}
