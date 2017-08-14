package com.moon.meojium.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.model.searchlog.SearchLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by moon on 2017. 8. 14..
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
    private List<SearchLog> searchLogList;
    private Context context;

    public SearchRecyclerViewAdapter(List<SearchLog> searchLogList, Context context) {
        this.searchLogList = searchLogList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(searchLogList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchLogList != null ? searchLogList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_search_log_keyword)
        TextView keywordTextView;
        @BindView(R.id.textview_search_log_date)
        TextView dateTextView;
        @BindView(R.id.relativelayout_search_log_container)
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(final SearchLog searchLog) {
            keywordTextView.setText(searchLog.getKeyword());
            dateTextView.setText(searchLog.getSearchedDate());
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchResultActivity.class);
                    intent.putExtra("keyword", searchLog.getKeyword());
                    context.startActivity(intent);
                }
            });
        }
    }
}
