package com.moon.meojium.ui.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.moon.meojium.base.FrescoImageViewer;
import com.moon.meojium.base.ImageOverlayView;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.model.story.Story;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by moon on 2017. 8. 9..
 */

public class StoryActivity extends AppCompatActivity
        implements ImageViewer.OnDismissListener,
        FrescoImageViewer {
    private Story story;
    private List<String> imageUrlList, contentList;
    private ImageOverlayView overlayView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        try {
            if (intent.getBooleanExtra("cascade", false)) {
                Museum museum = Parcels.unwrap(intent.getParcelableExtra("museum"));
                story = museum.getStoryList().get(0);
            } else {
                story = Parcels.unwrap(intent.getParcelableExtra("story"));
            }

            Log.d("Meojium/Story", "Story id: " + story.getId());
            Log.d("Meojium/Story", "Story id: " + story.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.info(this, "일시적인 오류가 발생했습니다.").show();
            onBackPressed();
        }

        imageUrlList = getImageUrlList();
        contentList = getContentList();

        showPicker();
    }

    @Override
    public void showPicker() {
        ImageViewer.Builder builder = new ImageViewer.Builder<>(this, imageUrlList)
                .setStartPosition(0);

        overlayView = new ImageOverlayView(this);
        builder.setOverlayView(overlayView);
        builder.setImageChangeListener(getImageChangeListener());
        builder.setOnDismissListener(this);

        builder.show();
    }

    private ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                overlayView.setContent(contentList.get(position));
            }
        };
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

    public List<String> getImageUrlList() {
        List<String> list = new ArrayList<>();

        list.add("http://www.ggilbo.com/news/photo/201605/285380_212898_5959.jpg");
        list.add("https://mblogthumb-phinf.pstatic.net/MjAxNzAxMDVfMTIy/MDAxNDgzNjE1NzQ3ODc4.ahQHC8GE-oo8POSsvsiDVuvLzKrqRmYOL6AZ-DHkTtcg.vh-GPlxrlxWzSlR3UYMdny7NVFNVAfneZE1kyDTjGlkg.JPEG.aralog1/IMG_2066.JPG?type=w800");
        list.add("http://img.insight.co.kr/static/2017/04/27/700/358JM6BWGMA13BPYS34Y.jpg");
        list.add("http://postfiles16.naver.net/MjAxNzA2MTlfNTcg/MDAxNDk3ODM0OTk1MzIy.2idxFdNg3VcPbyDkPxE4dEMLzjZ3IfXxa_eIsv30l7og.3l3levb6eKvW0Yw2usJ9MN5zG6UNP084pju9ZoDtxM8g.JPEG.blogkamco/4.jpg?type=w966");

        return list;
    }

    public List<String> getContentList() {
        List<String> list = new ArrayList<>();

        list.add("독립기념관-대한민국 육군 상호협력 업무협약 체결이 2016년 5월 26일에 이루어져, 휴가 중인 육군 병사가 독립 기념관을 방문하면 보상 휴가 1일을 받을 수 있게 되었습니다.");
        list.add("그 덕분에, 장병들의 자발적인 방문 동기를 생기게 해 독립 기념관엔 군인을 포함해 많은 방문객들로 성화를 이루고 있습니다.");
        list.add("게다가 2017년 5월, 대상이 육군에서 전군으로 확대됨에 따라 모든 병사가 이 혜택을 누릴 수 있게 되었습니다.");
        list.add("방문하실 때 휴가증이 없으면 보상 휴가를 받을 수 없으니 잊지 말고 꼭 가져가기!");

        return list;
    }

    @Override
    public void onDismiss() {
        finish();
    }
}
