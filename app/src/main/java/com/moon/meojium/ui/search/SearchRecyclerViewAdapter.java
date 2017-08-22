package com.moon.meojium.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.base.util.SharedPreferencesService;
import com.moon.meojium.database.dao.SearchDao;
import com.moon.meojium.model.UpdateResult;
import com.moon.meojium.model.searchlog.SearchLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 14..
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
    private List<SearchLog> searchLogList;
    private Context context;
    private SearchDao searchDao;

    public SearchRecyclerViewAdapter(List<SearchLog> searchLogList, Context context) {
        this.searchLogList = searchLogList;
        this.context = context;

        searchDao = SearchDao.getInstance();
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
                    Call<UpdateResult> call = searchDao.updateSearchLog(SharedPreferencesService.getInstance().getStringData(SharedPreferencesService.KEY_ENC_ID),
                            searchLog.getKeyword());
                    call.enqueue(new Callback<UpdateResult>() {
                        @Override
                        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                            UpdateResult result = response.body();

                            if (result.getCode() == UpdateResult.RESULT_OK) {
                                Log.d("Meojium/Search", "Success Updating SearchLog");
                            } else {
                                Log.d("Meojium/Search", "Fail Updating SearchLog");
                                Toasty.info(context, "서버 연결에 실패했습니다").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateResult> call, Throwable t) {
                            Toasty.info(context, "서버 연결에 실패했습니다").show();
                        }
                    });

                    Intent intent = new Intent(context, SearchResultActivity.class);
                    intent.putExtra("keyword", searchLog.getKeyword());
                    context.startActivity(intent);
                }
            });
        }
    }
}
