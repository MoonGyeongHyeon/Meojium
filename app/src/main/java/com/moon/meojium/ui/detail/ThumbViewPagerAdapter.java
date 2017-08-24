package com.moon.meojium.ui.detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moon.meojium.R;
import com.moon.meojium.base.FrescoImageViewer;
import com.moon.meojium.base.ImageOverlayView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by moon on 2017. 8. 22..
 */

public class ThumbViewPagerAdapter extends PagerAdapter
        implements FrescoImageViewer {
    @BindView(R.id.imageview_detail_thumb)
    ImageView imageView;

    @OnClick(R.id.imageview_detail_thumb)
    public void onClick(View view) {
        showPicker();
    }

    private List<String> imageUrlList;
    private Context context;
    private int currentPosition;
    private ViewPager viewPager;

    public ThumbViewPagerAdapter(List<String> imageUrlList, Context context) {
        this.imageUrlList = imageUrlList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageUrlList != null ? imageUrlList.size() : 0;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.viewpager_thumb, null);
        ButterKnife.bind(this, view);

        Glide.with(container.getContext())
                .load(imageUrlList.get(position))
                .placeholder(R.drawable.img_placeholder)
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v==obj;
    }

    @Override
    public void showPicker() {
        final ImageOverlayView imageOverlayView = new ImageOverlayView(context, imageUrlList.size());
        new ImageViewer.Builder<>(context, imageUrlList)
                .setStartPosition(getCurrentPosition())
                .setImageChangeListener(new ImageViewer.OnImageChangeListener() {
                    @Override
                    public void onImageChange(int position) {
                        setCurrentPosition(position);
                        viewPager.setCurrentItem(getCurrentPosition());
                        imageOverlayView.setPosition(position);
                    }
                })
                .setOverlayView(imageOverlayView)
                .show();

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
