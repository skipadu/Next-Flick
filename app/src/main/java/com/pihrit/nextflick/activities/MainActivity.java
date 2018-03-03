package com.pihrit.nextflick.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pihrit.nextflick.BuildConfig;
import com.pihrit.nextflick.R;
import com.pihrit.nextflick.adapters.MovieAdapter;
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
    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerView = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.movie_grid_column_span_count));
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this, this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        loadMoviesFromAPI();
    }

    private void loadMoviesFromAPI() {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(MainActivity.TMDB_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApiRequest = rf.create(TmdbApi.class);
        Call<TmdbJsonResponse> call = tmdbApiRequest.getPopularMovies(BuildConfig.TMDB_API_KEY);
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
}
