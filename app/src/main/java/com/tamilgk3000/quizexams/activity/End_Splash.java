package com.tamilgk3000.quizexams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamilgk3000.quizexams.R;


public class End_Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView view;
        LinearLayout hide;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 2000);
        view=(TextView)findViewById(R.id.end_splash);
        hide=(LinearLayout)findViewById(R.id.apps_apasan);
        hide.setVisibility(View.GONE);
        view.setText("ஆப்ஸ் அரசனின் பொது அறிவு  வினா  விடை செயலி பயன்படுத்தியமைக்கு நன்றி");
    }


}
