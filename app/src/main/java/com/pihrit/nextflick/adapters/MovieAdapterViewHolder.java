package com.pihrit.nextflick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;

class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView mMoviePosterImageView;
    private final MovieItemClickListener mMovieClickListener;

    public MovieAdapterViewHolder(View v, MovieItemClickListener clickListener) {
        super(v);
        mMoviePosterImageView = v.findViewById(R.id.iv_grid_movie_poster);
        mMovieClickListener = clickListener;
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mMovieClickListener.onMovieItemClick(getAdapterPosition());
    }
}
