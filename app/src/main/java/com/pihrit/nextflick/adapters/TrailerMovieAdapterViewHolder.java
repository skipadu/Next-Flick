package com.pihrit.nextflick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pihrit.nextflick.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerMovieAdapterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_trailermovie_name)
    TextView mTrailerMovieNameTextView;

    // TODO clickListener
    // TODO binding the trailer name

    public TrailerMovieAdapterViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
        // TODO click listener
    }
}
