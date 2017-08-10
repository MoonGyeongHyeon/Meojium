package com.moon.meojium.ui.story;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moon.meojium.model.story.StoryContent;

import java.util.List;

/**
 * Created by moon on 2017. 8. 10..
 */

public class StoryContentViewPagerAdapter extends FragmentPagerAdapter {
    private List<StoryContent> list;

    public StoryContentViewPagerAdapter(FragmentManager fm, List<StoryContent> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return StoryContentFragment.newInstance(list.get(position));
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }
}
