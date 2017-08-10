package com.moon.meojium.ui.story;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.story.StoryContent;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 10..
 */

public class StoryContentFragment extends Fragment {
    @BindView(R.id.imageview_story_content)
    ImageView contentImageView;
    @BindView(R.id.textview_story_content)
    TextView contentTextView;

    private StoryContent content;

    public static Fragment newInstance(StoryContent content) {
        Fragment fragment = new StoryContentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("content", Parcels.wrap(content));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_story_content, container, false);
        ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        content = Parcels.unwrap(bundle.getParcelable("content"));

        updateView();
    }

    private void updateView() {
        contentImageView.setImageResource(content.getImage());
        contentTextView.setText(content.getContent());
    }
}
