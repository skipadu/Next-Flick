package com.pihrit.nextflick.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pihrit.nextflick.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_review_author)
    TextView authorTextView;
    @BindView(R.id.tv_review_content)
    TextView contentTextView;

    public ReviewAdapterViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
