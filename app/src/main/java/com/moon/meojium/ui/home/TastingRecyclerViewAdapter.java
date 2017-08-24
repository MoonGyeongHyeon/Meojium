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
import com.moon.meojium.model.story.Story;
import com.moon.meojium.model.tasting.Tasting;
import com.moon.meojium.ui.detail.DetailActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 11..
 */

public class TastingRecyclerViewAdapter extends RecyclerView.Adapter<TastingRecyclerViewAdapter.ViewHolder> {
    private List<Tasting> museumList;
    private Context context;

    public TastingRecyclerViewAdapter(List<Tasting> museumList, Context context) {
        this.museumList = museumList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_museum_tasting, parent, false);
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
        @BindView(R.id.textview_museum_tasting_name)
        TextView nameTextView;
        @BindView(R.id.textview_museum_tasting_story_title)
        TextView storyTitleTextView;
        @BindView(R.id.imageview_museum_tasting_thumb)
        ImageView thumbImageView;
        @BindView(R.id.relativelayout_museum_tasting_container)
        RelativeLayout container;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final Tasting tasting) {
            final Museum museum = tasting.getMuseum();
            final Story story = new Story();
            story.setId(tasting.getId());
            story.setTitle(tasting.getTitle());

            nameTextView.setText(museum.getName());
            storyTitleTextView.setText(story.getTitle());
            Glide.with(context)
                    .load(BaseRetrofitService.IMAGE_LOAD_URL + museum.getImagePath())
                    .placeholder(R.drawable.img_placeholder)
                    .into(thumbImageView);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("museum", Parcels.wrap(museum));
                    intent.putExtra("story", Parcels.wrap(story));
                    intent.putExtra("cascade", true);
                    context.startActivity(intent);
                }
            });
        }
    }
}
