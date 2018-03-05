package com.pihrit.nextflick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_grid_movie_poster)
    ImageView mMoviePosterImageView;
    private final MovieItemClickListener mMovieClickListener;

    public MovieAdapterViewHolder(View v, MovieItemClickListener clickListener) {
        super(v);
        ButterKnife.bind(this, v);
        mMovieClickListener = clickListener;
    }

    @OnClick
    public void onClick() {
        mMovieClickListener.onMovieItemClick(getAdapterPosition());
    }
}
