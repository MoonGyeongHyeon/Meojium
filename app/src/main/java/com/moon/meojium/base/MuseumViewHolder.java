package com.moon.meojium.base;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.museumdetail.DetailActivity;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 11..
 */

public class MuseumViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_museum_thumb)
    ImageView thumbImageView;
    @BindView(R.id.textview_museum_name)
    TextView nameTextView;
    @BindView(R.id.textview_museum_address)
    TextView addressTextView;
    @BindView(R.id.relativelayout_museum_container)
    RelativeLayout container;

    private Context context;

    public MuseumViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
    }

    public void bindView(final Museum museum) {
        thumbImageView.setImageResource(museum.getImage());
        nameTextView.setText(museum.getName());
        addressTextView.setText(museum.getAddress());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("museum", Parcels.wrap(museum));
                context.startActivity(intent);
            }
        });
    }
}