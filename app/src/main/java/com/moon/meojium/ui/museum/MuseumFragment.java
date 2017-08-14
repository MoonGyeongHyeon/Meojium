package com.moon.meojium.ui.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 7..
 */

public class MuseumFragment extends Fragment {
    @BindView(R.id.recyclerview_museum)
    RecyclerView recyclerView;

    private List<Museum> museumList;

    public static Fragment newInstance(List<Museum> museumList) {
        Fragment fragment = new MuseumFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("museumList", Parcels.wrap(museumList));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_museum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        museumList = Parcels.unwrap(bundle.getParcelable("museumList"));

        MuseumRecyclerViewAdapter adapter = new MuseumRecyclerViewAdapter(museumList, getContext());
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }
}
