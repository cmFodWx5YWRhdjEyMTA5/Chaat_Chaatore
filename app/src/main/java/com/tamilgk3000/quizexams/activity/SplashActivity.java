package com.tamilgk3000.quizexams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tamilgk3000.quizexams.R;



public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        }, 2000);
    }


}



/*
a.setOnClickListener(new OnClickListener() {

@Override
public void onClick(View v) {
        // TODO Auto-generated method stub
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        switch (which) {
        case DialogInterface.BUTTON_POSITIVE:

        if (coins > 0) {
        coins = getCoins(Kelvipathildetails.this, "Coins");
        coins = coins - 100;
        Coins(Kelvipathildetails.this, "Coins", coins);
        TextView coins_txt = findViewById(R.id.multiple);
        coins_txt.setText("" + coins);
        a.setVisibility(View.INVISIBLE);

        word_btn[0].setText(word_array[0].toUpperCase());
        word_btn[0].setOnClickListener(null);
        for (int i = 0; i < 10; i++) {
        if (randBtn[i].getText().equals(
        word_array[0].toUpperCase())) {
        randBtn[i]
        .setVisibility(View.VISIBLE);
        i = 10;
        }
        }
        } else {
        a.setVisibility(View.VISIBLE);

        }
        break;

        case DialogInterface.BUTTON_NEGATIVE:
        break;
        }
        }
        };

        // Check if sufficient coins
        AlertDialog.Builder builder = new AlertDialog.Builder(
        Kelvipathildetails.this);
        builder.setTitle(getString(R.string.first_letter_msg_3)).setIcon(
        R.drawable.help);
        if(word_btn[0].getText() != "")  {
        if (Integer.valueOf(coins) >= (Integer.valueOf(coins))) {
        if (getCoins(Kelvipathildetails.this, "Coins") > 100) {
        builder.setMessage(getString(R.string.first_letter_msg_1));
        builder.setNegativeButton(getString(R.string.no), dialogClickListener)
        .setPositiveButton(getString(R.string.yes), dialogClickListener)
        .show();
        } else {
        builder.setMessage(getString(R.string.first_letter_msg_2));
        builder.setNegativeButton(getString(R.string.ok), dialogClickListener)
        .show();
        }

        }
        }else  if(word_btn[1].getText() != ""){
        if (Integer.valueOf(coins) >= (Integer.valueOf(coins))) {
        if (getCoins(Kelvipathildetails.this, "Coins") > 200) {
        builder.setMessage(getString(R.string.first_letter_msg_1));
        builder.setNegativeButton(getString(R.string.no), dialogClickListener)
        .setPositiveButton(getString(R.string.yes), dialogClickListener)
        .show();
        } else {
        builder.setMessage(getString(R.string.first_letter_msg_2));
        builder.setNegativeButton(getString(R.string.ok), dialogClickListener)
        .show();
        }

        }
        }

        }
        });*/
