package com.signaturemaker.app.Nucleo;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<SplashFragment> fragments;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
    }

    @Override
    public SplashFragment getItem(int i) {
        return fragments.get(i);
    }

    public ArrayList<SplashFragment> getAllFragment() {
        return fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(SplashFragment fragment) {
        this.fragments.add(fragment);
    }
}