package com.moon.meojium.ui.reviews;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moon.meojium.R;
import com.moon.meojium.base.UpdateResult;
import com.moon.meojium.database.dao.ReviewDao;
import com.moon.meojium.model.review.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 15..
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {
    private List<Review> reviewList;
    private Context context;
    private ReviewDao reviewDao;
    private boolean isUpdated;

    public ReviewRecyclerViewAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;

        reviewDao = ReviewDao.getInstance();
        isUpdated = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_review_user_nickname)
        TextView nicknameTextView;
        @BindView(R.id.textview_review_content)
        TextView contentTextView;
        @BindView(R.id.textview_review_registered_date)
        TextView registeredDateTextView;
        @BindView(R.id.imageview_review_remove)
        ImageView removeImageView;
        @BindView(R.id.relativelayout_review_container)
        RelativeLayout container;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
        }

        private void bindView(final Review review) {
            nicknameTextView.setText(review.getNickname());
            contentTextView.setText(review.getContent());
            registeredDateTextView.setText(review.getRegisteredDate());
            if (review.isWriter()) {
                removeImageView.setVisibility(View.VISIBLE);
                container.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_button_click));
                container.setClickable(true);

                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("리뷰 삭제")
                        .setMessage("정말 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<UpdateResult> call = reviewDao.deleteReview(review.getId());
                                call.enqueue(new Callback<UpdateResult>() {
                                    @Override
                                    public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                                        UpdateResult result = response.body();

                                        if (result.getCode() == UpdateResult.RESULT_OK) {
                                            Log.d("Meojium/Review", "Success Deleting Review");
                                            Toasty.info(context, "리뷰를 삭제했습니다.").show();

                                            reviewList.remove(getAdapterPosition());
                                            notifyDataSetChanged();
                                            setUpdated(true);
                                        } else {
                                            Log.d("Meojium/Review", "Fail Deleting Review");
                                            Toasty.info(context, "서버 연결에 실패했습니다").show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateResult> call, Throwable t) {
                                        Toasty.info(context, "서버 연결에 실패했습니다").show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();

                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                    }
                });

            } else {
                removeImageView.setVisibility(View.GONE);
                container.setBackground(null);
                container.setClickable(false);
            }
        }
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
