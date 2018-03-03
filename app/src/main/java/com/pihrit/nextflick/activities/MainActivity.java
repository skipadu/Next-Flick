package com.pihrit.nextflick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pihrit.nextflick.BuildConfig;
import com.pihrit.nextflick.R;
import com.pihrit.nextflick.adapters.MovieAdapter;
import com.pihrit.nextflick.enums.SelectedFilter;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;
import com.pihrit.nextflick.interfaces.TmdbApi;
import com.pihrit.nextflick.model.Movie;
import com.pihrit.nextflick.model.TmdbJsonResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener {
    private static final String TMDB_URL_BASE = "http://api.themoviedb.org";
    private MovieAdapter mMovieAdapter;
    private Toast mToast;
    private SelectedFilter mSelectedFilter = SelectedFilter.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView moviesRecyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.movie_grid_column_span_count));
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        moviesRecyclerView.setAdapter(mMovieAdapter);

        loadMoviesFromAPI();
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
            public void onResponse(Call<TmdbJsonResponse> call, Response<TmdbJsonResponse> response) {
                if (response.isSuccessful()) {
                    TmdbJsonResponse jsonResponse = response.body();
                    List<Movie> movies = new ArrayList<>(Arrays.asList(jsonResponse.getResults()));
                    mMovieAdapter.setMovies(movies);
                    mMovieAdapter.notifyDataSetChanged();

                } else {
                    mToast = Toast.makeText(MainActivity.this, R.string.error_response_from_api_not_successful, Toast.LENGTH_LONG);
                    mToast.show();
                }
            }

            @Override
            public void onFailure(Call<TmdbJsonResponse> call, Throwable t) {
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
            loadMoviesFromAPI();
            return true;
        } else if (id == R.id.action_top_rated) {
            mSelectedFilter = SelectedFilter.TOP_RATED;
            loadMoviesFromAPI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
