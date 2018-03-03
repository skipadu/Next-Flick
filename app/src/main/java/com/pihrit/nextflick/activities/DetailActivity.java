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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView moviePosterIv = findViewById(R.id.iv_detail_poster);
        TextView titleTv = findViewById(R.id.tv_detail_title);
        TextView releasedDateTv = findViewById(R.id.tv_detail_released);
        TextView voteAverageTv = findViewById(R.id.tv_detail_vote_average);
        TextView synopsisTv = findViewById(R.id.tv_detail_synopsis);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Movie.PARCELABLE_ID)) {
            Movie movie = callingIntent.getExtras().getParcelable(Movie.PARCELABLE_ID);
            if (movie != null) {
                getSupportActionBar().setTitle(movie.getTitle());
                titleTv.setText(movie.getTitle());
                releasedDateTv.setText(DetailUtils.getDateFormattedByLocale(this, movie.getReleaseDate()));
                voteAverageTv.setText(DetailUtils.getVoteAverageStr(this, movie.getVoteAverage()));
                synopsisTv.setText(movie.getSynopsis());
                Picasso.with(this).load(movie.getFullPosterPath()).transform(new RoundedTransformation(getResources().getInteger(R.integer.movie_poster_corner_radius))).into(moviePosterIv);
            }
        }
    }
}
