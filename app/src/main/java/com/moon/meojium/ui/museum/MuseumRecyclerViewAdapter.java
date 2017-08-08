package com.moon.meojium.ui.museum;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.museumdetail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 7..
 */

public class MuseumRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Museum> museumList;
    private Context context;

    public MuseumRecyclerViewAdapter(List<Museum> museumList, Context context) {
        this.museumList = museumList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_museum, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindView(museumList.get(position));
    }

    @Override
    public int getItemCount() {
        return museumList != null ? museumList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_museum_thumb)
        ImageView thumbImageView;
        @BindView(R.id.textview_museum_name)
        TextView nameTextView;
        @BindView(R.id.textview_museum_address)
        TextView addressTextView;
        @BindView(R.id.relativelayout_museum_container)
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final Museum museum) {
            thumbImageView.setImageResource(museum.getImage());
            nameTextView.setText(museum.getName());
            addressTextView.setText(museum.getAddress());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", String.valueOf(museum.getId()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
