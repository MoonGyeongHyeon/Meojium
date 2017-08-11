package com.moon.meojium.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moon.meojium.model.museum.Museum;

import java.util.List;

/**
 * Created by moon on 2017. 8. 11..
 */

public class MuseumViewPagerAdapter extends FragmentPagerAdapter {
    private List<Museum> museumList;

    public MuseumViewPagerAdapter(FragmentManager fm, List<Museum> museumList) {
        super(fm);
        this.museumList = museumList;
    }

    @Override
    public Fragment getItem(int position) {
        return MuseumViewPagerFragment.newInstance(museumList.get(position));
    }

    @Override
    public int getCount() {
        return museumList != null ? museumList.size() : 0;
    }
}
