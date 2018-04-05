package com.pihrit.nextflick.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class DetailActivity extends AppCompatActivity {

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

    private Movie mMovie;

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
            }
        }

        // TODO: if found in favorite, show it also in the UI
        // get by id
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
}
