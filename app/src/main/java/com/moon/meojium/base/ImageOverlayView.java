package com.moon.meojium.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
public class ImageOverlayView extends RelativeLayout {
    @BindView(R.id.textview_fresco_content)
    TextView contentTextView;
    @BindView(R.id.textview_fresco_page_count)
    TextView pageCountTextView;

    private int lastPagePosition;

    public ImageOverlayView(Context context, int lastPagePosition) {
        super(context);
        this.lastPagePosition = lastPagePosition;
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setContent(String content) {
        contentTextView.setText(content);
    }

    public void setPosition(int position) {
        pageCountTextView.setText(String.format(getResources().getString(R.string.fresco_page), position + 1, lastPagePosition));
    }


    private void init() {
        View view = inflate(getContext(), R.layout.view_image_overlay, this);
        ButterKnife.bind(this, view);
    }

    public int getLastPagePosition() {
        return lastPagePosition;
    }

    public void setLastPagePosition(int lastPagePosition) {
        this.lastPagePosition = lastPagePosition;
    }
}
