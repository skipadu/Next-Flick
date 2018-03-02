package com.pihrit.nextflick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                mReleasedDate.setText(getDateFormattedByLocale(movie.getReleaseDate()));
                mVoteAverage.setText(getVoteAverageStr(movie.getVoteAverage()));
                mSynopsis.setText(movie.getSynopsis());
                Picasso.with(this).load(movie.getFullPosterPath()).into(mMoviePoster);
            }
        }
    }

    private String getDateFormattedByLocale(String dateStr) {
        String formattedDateStr = dateStr;
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.tmdb_date_format));
        Date date;
        try {
            date = sdf.parse(formattedDateStr);
            formattedDateStr = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
        } catch (ParseException e) {
            // In case of exception, we are returning the date in form that we acquired it from the API
        }
        return formattedDateStr;
    }

    private String getVoteAverageStr(double voteAverage) {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat(getString(R.string.vote_average_decimalformat));
        sb.append(df.format(voteAverage)).append(getString(R.string.vote_average_max));
        return sb.toString();
    }
}
