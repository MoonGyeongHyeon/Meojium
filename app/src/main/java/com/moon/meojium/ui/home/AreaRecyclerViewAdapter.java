package com.moon.meojium.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moon.meojium.R;
import com.moon.meojium.base.BaseRetrofitService;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 24..
 */

public class AreaRecyclerViewAdapter extends RecyclerView.Adapter<AreaRecyclerViewAdapter.ViewHolder> {
    private List<Museum> museumList;
    private Context context;

    public AreaRecyclerViewAdapter(List<Museum> museumList, Context context) {
        this.museumList = museumList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_museum_area, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(museumList.get(position));
    }

    @Override
    public int getItemCount() {
        return museumList != null ? museumList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_museum_area_thumb)
        ImageView thumbImageView;
        @BindView(R.id.textview_museum_area_name)
        TextView nameTextView;
        @BindView(R.id.textview_museum_area_address)
        TextView addressTextView;
        @BindView(R.id.relativelayout_museum_area_container)
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final Museum museum) {
            Glide.with(context)
                    .load(BaseRetrofitService.IMAGE_LOAD_URL + museum.getImagePath())
                    .placeholder(R.drawable.img_placeholder)
                    .into(thumbImageView);
            nameTextView.setText(museum.getName());
            addressTextView.setText(museum.getAddress());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", museum.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
