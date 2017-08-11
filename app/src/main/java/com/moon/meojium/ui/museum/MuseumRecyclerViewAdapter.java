package com.moon.meojium.ui.museum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moon.meojium.R;
import com.moon.meojium.base.MuseumViewHolder;
import com.moon.meojium.model.museum.Museum;

import java.util.List;

/**
 * Created by moon on 2017. 8. 7..
 */

public class MuseumRecyclerViewAdapter extends RecyclerView.Adapter<MuseumViewHolder> {
    private List<Museum> museumList;
    private Context context;

    public MuseumRecyclerViewAdapter(List<Museum> museumList, Context context) {
        this.museumList = museumList;
        this.context = context;
    }

    @Override
    public MuseumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_museum, parent, false);
        return new MuseumViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MuseumViewHolder holder, int position) {
        holder.bindView(museumList.get(position));

    }

    @Override
    public int getItemCount() {
        return museumList != null ? museumList.size() : 0;
    }
}
