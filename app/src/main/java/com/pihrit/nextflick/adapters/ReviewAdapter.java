package com.pihrit.nextflick.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapterViewHolder> {

    private final Context mContext;
    private List<Review> mReviews = new ArrayList<>();

    public ReviewAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}
