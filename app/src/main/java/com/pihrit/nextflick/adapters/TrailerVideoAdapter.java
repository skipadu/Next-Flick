package com.pihrit.nextflick.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.interfaces.TrailerVideoItemClickListener;
import com.pihrit.nextflick.model.TrailerVideo;

import java.util.ArrayList;
import java.util.List;

public class TrailerVideoAdapter extends RecyclerView.Adapter<TrailerVideoAdapterViewHolder> {

    private final Context mContext;
    private final TrailerVideoItemClickListener mTrailerVideoItemClickListener;
    private List<TrailerVideo> mTrailers = new ArrayList<>();


    public TrailerVideoAdapter(@NonNull Context context, TrailerVideoItemClickListener trailerVideoItemClickListener) {
        mContext = context;
        this.mTrailerVideoItemClickListener = trailerVideoItemClickListener;
    }

    public void setTrailers(List<TrailerVideo> trailers) {
        mTrailers = trailers;
    }

    public TrailerVideo getTrailerAt(int itemIndex) {
        return mTrailers.get(itemIndex);
    }

    @NonNull
    @Override
    public TrailerVideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trailervideo_item, parent, false);
        return new TrailerVideoAdapterViewHolder(v, mTrailerVideoItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerVideoAdapterViewHolder holder, int position) {
        TrailerVideo trailerVideo = mTrailers.get(position);
        holder.mTrailerVideoNameTextView.setText(trailerVideo.getName());
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}
