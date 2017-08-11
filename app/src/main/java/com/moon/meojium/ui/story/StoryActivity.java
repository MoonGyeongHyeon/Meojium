package com.moon.meojium.ui.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.story.Story;
import com.moon.meojium.model.story.StoryContent;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * Created by moon on 2017. 8. 9..
 */

public class StoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textview_story_title)
    TextView titleTextView;
    @BindView(R.id.tablayout_story)
    TabLayout tabLayout;
    @BindView(R.id.viewpager_story)
    ViewPager viewPager;

    private Story story;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            if (intent.getBooleanExtra("cascade", false)) {
                Museum museum = Parcels.unwrap(intent.getParcelableExtra("museum"));
                story = museum.getStoryList().get(0);
            } else {
                story = Parcels.unwrap(intent.getParcelableExtra("story"));
            }

            Log.d("Meojium/Detail", "Story id: " + story.getId());
            Log.d("Meojium/Detail", "Story id: " + story.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        initToolbar();
        initTextView();
        createStoryContentData();

        initTabLayout();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initTextView() {
        titleTextView.setText(story.getTitle());
    }

    private void createStoryContentData() {
        List<StoryContent> storyContentList = new ArrayList<>();

        StoryContent storyContent = new StoryContent();
        storyContent.setImage(R.drawable.img_seokjangni_2_1);
        storyContent.setContent("유쾌한 5인이 진행하는 알쓸신잡 팀이 6회 - 공주&세종&부여 편에서 석장리 박물관을 방문하여 화제가 됐었는데요.");
        storyContentList.add(storyContent);

        storyContent = new StoryContent();
        storyContent.setImage(R.drawable.img_seokjangni_2_2);
        storyContent.setContent("5인이 식사를 하며 나누었던 얘기 중 하나로, 인간은 영장류 중 턱이 가장 부실했었다고 합니다.");
        storyContentList.add(storyContent);

        storyContent = new StoryContent();
        storyContent.setImage(R.drawable.img_seokjangni_2_3);
        storyContent.setContent("하지만 음식을 불에 익혀 부드럽게 만들어, 약한 턱으로도 음식을 쉽게 소화시킬 수 있게 됐는데요.");
        storyContentList.add(storyContent);

        storyContent = new StoryContent();
        storyContent.setImage(R.drawable.img_seokjangni_2_4);
        storyContent.setContent("자연스럽게, 하관은 작아지고 두뇌는 커져 현재와 같이 진화할 수 있었다고 합니다.");
        storyContentList.add(storyContent);

        storyContent = new StoryContent();
        storyContent.setImage(R.drawable.img_seokjangni_2_5);
        storyContent.setContent("석장리 박물관에서도 불을 이용하는 선사인들의 모습을 쉽게 볼 수 있는데요.");
        storyContentList.add(storyContent);

        storyContent = new StoryContent();
        storyContent.setImage(R.drawable.img_seokjangni_2_6);
        storyContent.setContent("이러한 선사인들의 삶을 한 눈에 볼 수 있는 석장리 박물관으로 가볼까요?");
        storyContentList.add(storyContent);

        story.setContentList(storyContentList);
    }

    private void initTabLayout() {
        StoryContentViewPagerAdapter adapter = new StoryContentViewPagerAdapter(getSupportFragmentManager(), story.getContentList());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
