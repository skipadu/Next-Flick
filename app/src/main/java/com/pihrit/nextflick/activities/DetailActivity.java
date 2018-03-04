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

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Movie.PARCELABLE_ID)) {
            Movie movie = callingIntent.getExtras().getParcelable(Movie.PARCELABLE_ID);
            if (movie != null) {
                getSupportActionBar().setTitle(movie.getTitle());
                mTitleTv.setText(movie.getTitle());
                mReleasedDateTv.setText(DetailUtils.getDateFormattedByLocale(this, movie.getReleaseDate()));
                mVoteAverageTv.setText(DetailUtils.getVoteAverageStr(this, movie.getVoteAverage()));
                mSynopsisTv.setText(movie.getSynopsis());
                Picasso.with(this).load(movie.getFullPosterPath()).transform(new RoundedTransformation(getResources().getInteger(R.integer.movie_poster_corner_radius))).into(mMoviePosterIv);
            }
        }
    }
}
