package com.pihrit.nextflick.activities;

import android.content.ActivityNotFoundException;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pihrit.nextflick.BuildConfig;
import com.pihrit.nextflick.R;
import com.pihrit.nextflick.adapters.ReviewAdapter;
import com.pihrit.nextflick.adapters.TrailerVideoAdapter;
import com.pihrit.nextflick.data.FavoriteMovieContract;
import com.pihrit.nextflick.interfaces.TmdbApi;
import com.pihrit.nextflick.interfaces.TrailerVideoItemClickListener;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.model.Review;
import com.pihrit.nextflick.model.TmdbJsonReviewsResponse;
import com.pihrit.nextflick.model.TmdbJsonVideosResponse;
import com.pihrit.nextflick.model.TrailerVideo;
import com.pihrit.nextflick.utils.DetailUtils;
import com.pihrit.nextflick.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, TrailerVideoItemClickListener {

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
    @BindView(R.id.rv_detail_trailers)
    RecyclerView mTrailerRecyclerView;
    @BindView(R.id.rv_detail_user_reviews)
    RecyclerView mReviewRecyclerView;

    private static final int FAVORITE_LOADER_ID = 1;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private Movie mMovie;
    private Uri mFavoriteUri;
    private boolean mIsFavorited;
    private Toast mToast;

    private TrailerVideoAdapter mTrailerVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private int mMainSelectedFilter;
    private int mMainLastScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent callingIntent = getIntent();
        if (callingIntent.hasExtra(Movie.PARCELABLE_ID)) {

            if (callingIntent.hasExtra(MainActivity.SELECTED_FILTER)) {
                mMainSelectedFilter = callingIntent.getIntExtra(MainActivity.SELECTED_FILTER, 0);
            }

            if (callingIntent.hasExtra(MainActivity.LAST_SCROLL_POSITION)) {
                mMainLastScrollPosition = callingIntent.getIntExtra(MainActivity.LAST_SCROLL_POSITION, 0);
            }

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

                LinearLayoutManager trailerVideoLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                mTrailerRecyclerView.setLayoutManager(trailerVideoLinearLayoutManager);
                mTrailerRecyclerView.setHasFixedSize(true);
                mTrailerVideoAdapter = new TrailerVideoAdapter(this, this);
                mTrailerRecyclerView.setAdapter(mTrailerVideoAdapter);

                LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                mReviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);
                mReviewRecyclerView.setHasFixedSize(true);
                mReviewAdapter = new ReviewAdapter(this);
                mReviewRecyclerView.setAdapter(mReviewAdapter);

                loadTrailers();
                loadReviews();

                getSupportLoaderManager().initLoader(FAVORITE_LOADER_ID, null, this);
            }
        }
    }

    private void loadReviews() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(MainActivity.TMDB_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApiRequest = rf.create(TmdbApi.class);
        Call<TmdbJsonReviewsResponse> call = tmdbApiRequest.getReviews(mMovie.getId(), BuildConfig.TMDB_API_KEY);

        call.enqueue(new Callback<TmdbJsonReviewsResponse>() {
            @Override
            public void onResponse(Call<TmdbJsonReviewsResponse> call, Response<TmdbJsonReviewsResponse> response) {
                if (response.isSuccessful()) {
                    List<Review> reviews = new ArrayList<>(Arrays.asList(response.body().getResults()));
                    mReviewAdapter.setReviews(reviews);
                    mReviewAdapter.notifyDataSetChanged();
                } else {
                    mToast = Toast.makeText(DetailActivity.this, R.string.error_response_from_api_not_successful, Toast.LENGTH_LONG);
                    mToast.show();
                }
            }

            @Override
            public void onFailure(Call<TmdbJsonReviewsResponse> call, Throwable t) {
                mToast = Toast.makeText(DetailActivity.this, R.string.error_failed_to_get_response_from_api, Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }

    private void loadTrailers() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(MainActivity.TMDB_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdpApiRequest = rf.create(TmdbApi.class);
        Call<TmdbJsonVideosResponse> call = tmdpApiRequest.getVideos(mMovie.getId(), BuildConfig.TMDB_API_KEY);


        call.enqueue(new Callback<TmdbJsonVideosResponse>() {
            @Override
            public void onResponse(Call<TmdbJsonVideosResponse> call, Response<TmdbJsonVideosResponse> response) {
                if (response.isSuccessful()) {
                    List<TrailerVideo> trailerVideos = new ArrayList<>(Arrays.asList(response.body().getResults()));
                    mTrailerVideoAdapter.setTrailers(trailerVideos);
                    mTrailerVideoAdapter.notifyDataSetChanged();
                } else {
                    mToast = Toast.makeText(DetailActivity.this, R.string.error_response_from_api_not_successful, Toast.LENGTH_LONG);
                    mToast.show();
                }
            }

            @Override
            public void onFailure(Call<TmdbJsonVideosResponse> call, Throwable t) {
                mToast = Toast.makeText(DetailActivity.this, R.string.error_failed_to_get_response_from_api, Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }

    @OnClick(R.id.iv_btn_favorite)
    public void onClick() {
        if (mIsFavorited) {
            Toast.makeText(getBaseContext(), R.string.toast_removed_from_favorites, Toast.LENGTH_LONG).show();
            getContentResolver().delete(FavoriteMovieContract.FavoriteMovieEntry.buildUriWithId((int) mMovie.getId()), null, null);
        } else {
            //
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

            if (uri != null) {
                Toast.makeText(getBaseContext(), R.string.toast_added_to_favorites, Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle loaderArgs) {
        if (id == FAVORITE_LOADER_ID) {
            return new AsyncTaskLoader<Cursor>(this) {
                Cursor mFavoriteData = null;

                @Nullable
                @Override
                public Cursor loadInBackground() {
                    try {
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
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == FAVORITE_LOADER_ID) {
            if (mMovie != null) {
                if (data != null && data.moveToFirst()) {
                    mFavoriteButtonTv.setText(R.string.movie_detail_favoritebutton_unlike);
                    mFavoriteButtonIv.setImageResource(R.drawable.favorite_movie_empty);
                    mIsFavorited = true;
                } else {
                    mFavoriteButtonTv.setText(R.string.movie_detail_favoritebutton_like);
                    mFavoriteButtonIv.setImageResource(R.drawable.favorite_movie);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    // Looked help from the answer of Roger Garzon Nieto:
    // https://stackoverflow.com/a/12439378/649474
    @Override
    public void onTrailerVideoItemClick(int itemIndex) {
        TrailerVideo trailerVideo = mTrailerVideoAdapter.getTrailerAt(itemIndex);
        String key = trailerVideo.getKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_app_intent) + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.youtube_url_start) + key));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(MainActivity.SELECTED_FILTER, mMainSelectedFilter);
        mainIntent.putExtra(MainActivity.LAST_SCROLL_POSITION, mMainLastScrollPosition);
        return mainIntent;
    }
}
