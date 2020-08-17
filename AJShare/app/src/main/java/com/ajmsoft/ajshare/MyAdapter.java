package com.ajmsoft.ajshare;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs){
        super(fm);
        context = c;
        this.totalTabs=totalTabs;

    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Connect connectFragment = new Connect();
                return connectFragment;
            case 1:
                Myfiles myfilesFragment = new Myfiles();
                return  myfilesFragment;


            case 2:

                History historyFragment = new History();
                return historyFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
/*

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
*/

/*
public class MyAdapter extends FragmentStateAdapter{
    Context c;
    int totalTabs;

    public MyAdapter(Context c, FragmentActivity fa, int totalTabs) {
        super();
        */
/* super(fa);*//*

        this.c=c;
        this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                Connect connectFragment = new Connect();
                return connectFragment;
            case 1:
                Myfiles myfilesFragment = new Myfiles();
                return  myfilesFragment;
            case 2:
                History historyFragment = new History();
                return historyFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
*/
