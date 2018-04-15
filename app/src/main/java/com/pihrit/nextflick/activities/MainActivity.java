package com.pihrit.nextflick.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pihrit.nextflick.BuildConfig;
import com.pihrit.nextflick.R;
import com.pihrit.nextflick.adapters.MovieAdapter;
import com.pihrit.nextflick.data.FavoriteMovieContract;
import com.pihrit.nextflick.enums.SelectedFilter;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;
import com.pihrit.nextflick.interfaces.TmdbApi;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.model.TmdbJsonResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TMDB_URL_BASE = "http://api.themoviedb.org";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;
    public static final String SELECTED_FILTER = "selected_filter";
    public static final String LAST_SCROLL_POSITION = "last_scroll_position";

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;

    private GridLayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;
    private Toast mToast;
    private SelectedFilter mSelectedFilter = SelectedFilter.POPULAR;
    private int mLastScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.movie_grid_column_span_count));
        mMoviesRecyclerView.setLayoutManager(mGridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState != null) {
            int selectedFilter = savedInstanceState.getInt(MainActivity.SELECTED_FILTER);
            mSelectedFilter = SelectedFilter.values()[selectedFilter];
            mLastScrollPosition = savedInstanceState.getInt(MainActivity.LAST_SCROLL_POSITION);
        } else {
            Intent callingIntent = getIntent();
            if (callingIntent != null && callingIntent.hasExtra(MainActivity.SELECTED_FILTER)) {
                int returnedValue = callingIntent.getIntExtra(MainActivity.SELECTED_FILTER, 0);
                mSelectedFilter = SelectedFilter.values()[returnedValue];
            }
            if (callingIntent != null && callingIntent.hasExtra(MainActivity.LAST_SCROLL_POSITION)) {
                mLastScrollPosition = callingIntent.getIntExtra(MainActivity.LAST_SCROLL_POSITION, 0);
            }
        }

        if (mSelectedFilter == SelectedFilter.POPULAR || mSelectedFilter == SelectedFilter.TOP_RATED) {
            loadMoviesFromAPI();
        } else {
            favoriteFilterSelected();
        }

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int selectedFilter = mSelectedFilter.ordinal();
        outState.putInt(MainActivity.SELECTED_FILTER, selectedFilter);
        int position = mGridLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(MainActivity.LAST_SCROLL_POSITION, position);
    }

    private void loadMoviesFromAPI() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(MainActivity.TMDB_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApiRequest = rf.create(TmdbApi.class);
        Call<TmdbJsonResponse> call;
        if (mSelectedFilter == SelectedFilter.POPULAR) {
            call = tmdbApiRequest.getPopularMovies(BuildConfig.TMDB_API_KEY);
        } else if (mSelectedFilter == SelectedFilter.TOP_RATED) {
            call = tmdbApiRequest.getTopRatedMovies(BuildConfig.TMDB_API_KEY);
        } else {
            return;
        }
        getSupportActionBar().setSubtitle(mSelectedFilter.getTitleResId());

        call.enqueue(new Callback<TmdbJsonResponse>() {
            @Override
            public void onResponse(@NonNull Call<TmdbJsonResponse> call, @NonNull Response<TmdbJsonResponse> response) {
                if (response.isSuccessful()) {
                    TmdbJsonResponse jsonResponse = response.body();
                    List<Movie> movies = new ArrayList<>(Arrays.asList(jsonResponse.getResults()));
                    mMovieAdapter.setMovies(movies);
                    mMovieAdapter.notifyDataSetChanged();

                    if (mLastScrollPosition > 0) {
                        mGridLayoutManager.smoothScrollToPosition(mMoviesRecyclerView, null, mLastScrollPosition);
                    }

                } else {
                    mToast = Toast.makeText(MainActivity.this, R.string.error_response_from_api_not_successful, Toast.LENGTH_LONG);
                    mToast.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TmdbJsonResponse> call, @NonNull Throwable t) {
                mToast = Toast.makeText(MainActivity.this, R.string.error_failed_to_get_response_from_api, Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }

    @Override
    public void onMovieItemClick(int itemIndex) {
        Movie clickedMovie = mMovieAdapter.getMovieAt(itemIndex);

        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(Movie.PARCELABLE_ID, clickedMovie);
        detailIntent.putExtra(MainActivity.SELECTED_FILTER, mSelectedFilter.ordinal());

        int position = mGridLayoutManager.findFirstVisibleItemPosition();
        detailIntent.putExtra(MainActivity.LAST_SCROLL_POSITION, position);

        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            mSelectedFilter = SelectedFilter.POPULAR;
            mMovieAdapter.setSelectedFilter(mSelectedFilter);
            loadMoviesFromAPI();
            return true;
        } else if (id == R.id.action_top_rated) {
            mSelectedFilter = SelectedFilter.TOP_RATED;
            mMovieAdapter.setSelectedFilter(mSelectedFilter);
            loadMoviesFromAPI();
            return true;
        } else if (id == R.id.action_favorite) {
            mSelectedFilter = SelectedFilter.FAVORITES;
            favoriteFilterSelected();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void favoriteFilterSelected() {
        mMovieAdapter.setSelectedFilter(mSelectedFilter);

        getSupportActionBar().setSubtitle(mSelectedFilter.getTitleResId());
        mMoviesRecyclerView.removeAllViews();
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TIMESTAMP);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to load data async.");
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mMovieAdapter.swapCursor(cursor);

        if (mLastScrollPosition > 0) {
            mGridLayoutManager.smoothScrollToPosition(mMoviesRecyclerView, null, mLastScrollPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

}
