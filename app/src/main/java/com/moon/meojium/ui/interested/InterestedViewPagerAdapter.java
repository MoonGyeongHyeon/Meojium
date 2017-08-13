package com.moon.meojium.ui.interested;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.museum.MuseumFragment;

import java.util.List;

/**
 * Created by moon on 2017. 8. 13..
 */

public class InterestedViewPagerAdapter extends FragmentPagerAdapter {
    private List<List<Museum>> lists;

    public InterestedViewPagerAdapter(FragmentManager fm, List<List<Museum>> lists) {
        super(fm);
        this.lists = lists;
    }

    @Override
    public Fragment getItem(int position) {
        return MuseumFragment.newInstance(lists.get(position));
    }

    @Override
    public int getCount() {
        return lists != null ? lists.size() : 0;
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
