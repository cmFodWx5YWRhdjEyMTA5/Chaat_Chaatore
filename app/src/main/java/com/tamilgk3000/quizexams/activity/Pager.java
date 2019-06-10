package com.tamilgk3000.quizexams.activity;

/**
 * Created by AndroidTeam on 05-Jun-19.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tamilgk3000.quizexams.Fragment.first;
import com.tamilgk3000.quizexams.Fragment.four;
import com.tamilgk3000.quizexams.Fragment.second;
import com.tamilgk3000.quizexams.Fragment.three_1;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                first tab1 = new first();
                return tab1;
            case 1:
                second tab2 = new second();
                return tab2;
            case 2:
                three_1 tab3 = new three_1();
                return tab3;
            case 3:
                four tab4 = new four();
                return tab4;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}