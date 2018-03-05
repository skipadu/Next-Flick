package com.pihrit.nextflick.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapterViewHolder> {

    private final MovieItemClickListener mMovieClickListener;
    private final Context mContext;
    private List<Movie> mMovies;

    public MovieAdapter(@NonNull Context context, MovieItemClickListener clickListener) {
        mContext = context;
        mMovieClickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(v, mMovieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Picasso.with(mContext).
                load(mMovies.get(position).getFullPosterPath())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .transform(new RoundedTransformation(mContext.getResources().getInteger(R.integer.movie_poster_corner_radius)))
                .into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }

    public void setMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    public Movie getMovieAt(int itemIndex) {
        return mMovies.get(itemIndex);
    }
}
