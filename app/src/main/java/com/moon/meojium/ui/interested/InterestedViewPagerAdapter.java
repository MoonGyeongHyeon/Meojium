package com.moon.meojium.ui.interested;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moon.meojium.ui.museum.MuseumFragment;

import java.util.List;

/**
 * Created by moon on 2017. 8. 13..
 */

public class InterestedViewPagerAdapter extends FragmentPagerAdapter {
    private List<String> categoryList;

    public InterestedViewPagerAdapter(FragmentManager fm, List<String> categoryList) {
        super(fm);
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {
        return MuseumFragment.newInstance(categoryList.get(position));
    }

    @Override
    public int getCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "찜목록";

            case 1:
                return "다녀왔음";

            default:
                return null;
        }
    }
}
