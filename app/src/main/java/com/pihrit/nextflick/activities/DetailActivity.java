package com.pihrit.nextflick.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.data.FavoriteMovieContract;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.utils.DetailUtils;
import com.pihrit.nextflick.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.iv_detail_poster)
    ImageView mMoviePosterIv;
    @BindView(R.id.tv_detail_title)
    TextView mTitleTv;
    @BindView(R.id.tv_detail_released)
    TextView mReleasedDateTv;
    @BindView(R.id.tv_detail_vote_average)
    TextView mVoteAverageTv;
    @BindView(R.id.tv_detail_synopsis)
    TextView mSynopsisTv;
    @BindView(R.id.iv_favorite_button)
    ImageView mFavoriteButtonIv;
    @BindView(R.id.tv_favorite_button)
    TextView mFavoriteButtonTv;

    private static final int FAVORITE_LOADER_ID = 1;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private Movie mMovie;
    private Uri mFavoriteUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Movie.PARCELABLE_ID)) {
            mMovie = callingIntent.getExtras().getParcelable(Movie.PARCELABLE_ID);
            if (mMovie != null) {
                getSupportActionBar().setTitle(mMovie.getTitle());
                mTitleTv.setText(mMovie.getTitle());
                mReleasedDateTv.setText(DetailUtils.getDateFormattedByLocale(this, mMovie.getReleaseDate()));
                mVoteAverageTv.setText(DetailUtils.getVoteAverageStr(this, mMovie.getVoteAverage()));
                mSynopsisTv.setText(mMovie.getSynopsis());
                Picasso.with(this)
                        .load(mMovie.getFullPosterPath())
                        .placeholder(R.drawable.movie_placeholder)
                        .error(R.drawable.movie_placeholder_error)
                        .transform(new RoundedTransformation(getResources().getInteger(R.integer.movie_poster_corner_radius)))
                        .into(mMoviePosterIv);

                mFavoriteUri = FavoriteMovieContract.FavoriteMovieEntry.buildUriWithId((int) mMovie.getId());

                // TODO: if found in favorite, show it also in the UI
                // get by id
                getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);
                // TODO: when async is finished, change the like button state
            }
        }
    }

    @OnClick(R.id.iv_btn_favorite)
    public void onClick() {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, mMovie.getTitle());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT, mMovie.getVoteCount());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_HAS_VIDEO, mMovie.isHasVideo());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POPULARITY, mMovie.getPopularity());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS, mMovie.getSynopsis());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
        Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, cv);

        // FIXME: Just for testing purposes for now
        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mFavoriteData = null;

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                    Log.v(TAG, "loadInBackground(), mFavoriteUri: " + mFavoriteUri.toString());
                    return getContentResolver().query(mFavoriteUri,
                            null,
                            null,
                            null,
                            null);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to load favorite movie in async.");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (mFavoriteData != null) {
                    deliverResult(mFavoriteData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable Cursor data) {
                mFavoriteData = data;
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // TODO: do binding and stuff here when the loader has finished loading
        if(mMovie != null) {
            if(data != null && data.moveToFirst()) {
                int movieId = data.getInt(data.getColumnIndex(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
                Log.v(TAG, "onLoadFinished(), movieId: " + movieId);
                mFavoriteButtonTv.setText(R.string.movie_detail_favoritebutton_unlike);
                mFavoriteButtonIv.setImageResource(R.drawable.favorite_movie_empty);

            } else {
                Log.v(TAG, "onLoadFinished(), movie was not found in favorites!");
                mFavoriteButtonTv.setText(R.string.movie_detail_favoritebutton_like);

                mFavoriteButtonIv.setImageResource(R.drawable.favorite_movie);
            }

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
