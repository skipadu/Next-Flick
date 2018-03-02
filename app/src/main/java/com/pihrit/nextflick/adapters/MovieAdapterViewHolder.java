package com.pihrit.nextflick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.pihrit.nextflick.R;

class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

    final ImageView mMoviePosterImageView;

    public MovieAdapterViewHolder(View v) {
        super(v);
        mMoviePosterImageView = v.findViewById(R.id.iv_grid_movie_poster);
    }
}
