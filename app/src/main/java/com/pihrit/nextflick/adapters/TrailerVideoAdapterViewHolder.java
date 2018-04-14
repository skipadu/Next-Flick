package com.pihrit.nextflick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pihrit.nextflick.R;
import com.pihrit.nextflick.interfaces.TrailerVideoItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrailerVideoAdapterViewHolder extends RecyclerView.ViewHolder {
    
    @BindView(R.id.tv_trailervideo_name)
    TextView mTrailerVideoNameTextView;
    private final TrailerVideoItemClickListener mTrailerVideoItemClickListener;

    public TrailerVideoAdapterViewHolder(View v, TrailerVideoItemClickListener trailerVideoItemClickListener) {
        super(v);
        ButterKnife.bind(this, v);
        mTrailerVideoItemClickListener = trailerVideoItemClickListener;
    }

    @OnClick
    public void onClick() {
        mTrailerVideoItemClickListener.onTrailerVideoItemClick(getAdapterPosition());
    }
}
