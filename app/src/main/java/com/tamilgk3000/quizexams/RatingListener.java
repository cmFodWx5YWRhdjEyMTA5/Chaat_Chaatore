package com.tamilgk3000.quizexams;

public interface RatingListener {
    void onStart();
    void onEnd(String success, String message, float rating);
}
