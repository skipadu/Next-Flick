package com.pihrit.nextflick.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.adapters.MovieAdapter;
import com.pihrit.nextflick.interfaces.MovieItemClickListener;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener {

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
        mMovieAdapter = new MovieAdapter(this, this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onMovieItemClick(int itemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, "Item index: " + itemIndex, Toast.LENGTH_LONG);
        mToast.show();
    }
}
