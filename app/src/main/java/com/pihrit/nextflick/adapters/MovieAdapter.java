package com.pihrit.nextflick.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pihrit.nextflick.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapterViewHolder> {

    private final Context mContext;

    public MovieAdapter(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        // FIXME Just testing to show 20 items to see test data
        holder.mMoviePosterImageView.setImageResource(R.drawable.ic_movie);
    }

    @Override
    public int getItemCount() {
        // FIXME Just testing to show 20 items to see test data
        return 20;
    }
}
