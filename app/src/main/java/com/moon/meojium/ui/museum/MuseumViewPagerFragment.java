package com.moon.meojium.ui.museum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.museumdetail.DetailActivity;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by moon on 2017. 8. 11..
 */

public class MuseumViewPagerFragment extends Fragment {
    @BindView(R.id.imageview_museum_thumb)
    ImageView thumbImageView;
    @BindView(R.id.textview_museum_name)
    TextView nameTextView;
    @BindView(R.id.textview_museum_address)
    TextView addressTextView;

    @OnClick(R.id.relativelayout_museum_container)
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("museum", Parcels.wrap(museum));
        startActivity(intent);
    }


    private Museum museum;

    public static Fragment newInstance(Museum museum) {
        Fragment fragment = new MuseumViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("museum", Parcels.wrap(museum));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.item_museum, container, false);
        ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        museum = Parcels.unwrap(bundle.getParcelable("museum"));

        updateView();
    }

    private void updateView() {
        thumbImageView.setImageResource(museum.getImage());
        nameTextView.setText(museum.getName());
        addressTextView.setText(museum.getAddress());
    }
}
