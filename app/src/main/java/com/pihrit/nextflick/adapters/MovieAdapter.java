package com.pihrit.nextflick.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.data.FavoriteMovieContract;
import com.pihrit.nextflick.enums.SelectedFilter;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapterViewHolder> {

    private final MovieItemClickListener mMovieClickListener;
    private final Context mContext;
    private List<Movie> mMovies;
    private Cursor mCursor;
    private SelectedFilter mSelectedFilter = SelectedFilter.POPULAR;

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
        switch (mSelectedFilter) {
            case TOP_RATED:
            case POPULAR:
                bindForJSONRequests(holder, position);
                break;
            case FAVORITES:
                bindForSQLite(holder, position);
                break;
            default:
                throw new UnsupportedOperationException("Unknown filter selected for adapter!");
        }
    }

    private void bindForJSONRequests(@NonNull MovieAdapterViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mMovies.get(position).getFullPosterPath())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .transform(new RoundedTransformation(mContext.getResources().getInteger(R.integer.movie_poster_corner_radius)))
                .into(holder.mMoviePosterImageView);
    }

    private void bindForSQLite(@NonNull MovieAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int posterIndex = mCursor.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH);
        String fullPosterPath = Movie.IMAGE_PATH_START + mCursor.getString(posterIndex);

        Picasso.with(mContext)
                .load(fullPosterPath)
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .transform(new RoundedTransformation(mContext.getResources().getInteger(R.integer.movie_poster_corner_radius)))
                .into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        switch (mSelectedFilter) {
            case TOP_RATED:
            case POPULAR:
                return mMovies == null ? 0 : mMovies.size();
            case FAVORITES:
                return mCursor == null ? 0 : mCursor.getCount();
            default:
                throw new UnsupportedOperationException("Unknown filter selected for adapter!");
        }
    }

    public void setMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    public Movie getMovieAt(int itemIndex) {
        switch (mSelectedFilter) {
            case TOP_RATED:
            case POPULAR:
                return mMovies.get(itemIndex);
            case FAVORITES:
                return createMovieFromCursorPosition(itemIndex);
            default:
                throw new UnsupportedOperationException("Unknown filter!");
        }
    }

    private Movie createMovieFromCursorPosition(int itemIndex) {
        Movie movie = new Movie();
        mCursor.moveToPosition(itemIndex);

        movie.setTitle(getStringFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE));
        movie.setId(getIntFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
        movie.setPosterPath(getStringFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH));
        movie.setVoteCount(getIntFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT));
        movie.setHasVideo(getBooleanFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_HAS_VIDEO));
        movie.setPopularity(getDoubleFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POPULARITY));
        movie.setSynopsis(getStringFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS));
        movie.setReleaseDate(getStringFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE));
        movie.setVoteAverage(getDoubleFromCursor(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE));

        // TODO: get the rest of the data from API with movie's id, if needed
        // OR save all the data for offline for easier in this phase. Load the rest of the data ( trailers, comments in the DetailActivity )
        //
        return movie;
    }

    // TODO: create a helper to do this
    private String getStringFromCursor(String column) {
        return mCursor.getString(mCursor.getColumnIndex(column));
    }

    private int getIntFromCursor(String column) {
        return mCursor.getInt(mCursor.getColumnIndex(column));
    }

    private boolean getBooleanFromCursor(String column) {
        return mCursor.getInt(mCursor.getColumnIndex(column)) == 1 ? true : false;
    }

    private double getDoubleFromCursor(String column) {
        return mCursor.getDouble(mCursor.getColumnIndex(column));
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor tmp = mCursor;
        this.mCursor = cursor;

        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return tmp;
    }

    public void setSelectedFilter(SelectedFilter filter) {
        mSelectedFilter = filter;
    }
}
