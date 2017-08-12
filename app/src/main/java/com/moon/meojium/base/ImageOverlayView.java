package com.moon.meojium.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;

/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
public class ImageOverlayView extends RelativeLayout {

    private TextView contentTextView;

    public ImageOverlayView(Context context) {
        super(context);
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

    private void init() {
        View view = inflate(getContext(), R.layout.view_image_overlay, this);
        contentTextView = view.findViewById(R.id.textview_story_content);
    }
}
