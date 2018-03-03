package com.pihrit.nextflick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.utils.DetailUtils;
import com.pihrit.nextflick.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView mMoviePoster;
    private TextView mTitle;
    private TextView mReleasedDate;
    private TextView mVoteAverage;
    private TextView mSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMoviePoster = findViewById(R.id.iv_detail_poster);
        mTitle = findViewById(R.id.tv_detail_title);
        mReleasedDate = findViewById(R.id.tv_detail_released);
        mVoteAverage = findViewById(R.id.tv_detail_vote_average);
        mSynopsis = findViewById(R.id.tv_detail_synopsis);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Movie.PARCELABLE_ID)) {
            Movie movie = callingIntent.getExtras().getParcelable(Movie.PARCELABLE_ID);
            if (movie != null) {
                mTitle.setText(movie.getTitle());
                mReleasedDate.setText(DetailUtils.getDateFormattedByLocale(this, movie.getReleaseDate()));
                mVoteAverage.setText(DetailUtils.getVoteAverageStr(this, movie.getVoteAverage()));
                mSynopsis.setText(movie.getSynopsis());
                Picasso.with(this).load(movie.getFullPosterPath()).transform(new RoundedTransformation(getResources().getInteger(R.integer.movie_poster_corner_radius))).into(mMoviePoster);
            }
        }
    }
}
