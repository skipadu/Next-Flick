package com.pihrit.nextflick.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.model.TrailerVideo;

import java.util.ArrayList;
import java.util.List;

public class TrailerMovieAdapter extends RecyclerView.Adapter<TrailerMovieAdapterViewHolder> {

    private final Context mContext;
    // TODO clicklistener
    private List<TrailerVideo> mTrailers = new ArrayList<>();


    public TrailerMovieAdapter(@NonNull Context context) {
        mContext = context;
    }

    public void setTrailers(List<TrailerVideo> trailers) {
        mTrailers = trailers;
    }

    public TrailerVideo getTrailerAt(int itemIndex) {
        return mTrailers.get(itemIndex);
    }

    @NonNull
    @Override
    public TrailerMovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trailermovie_item, parent, false);
        return new TrailerMovieAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerMovieAdapterViewHolder holder, int position) {
        TrailerVideo trailerVideo = mTrailers.get(position);
        holder.mTrailerMovieNameTextView.setText(trailerVideo.getName());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}
