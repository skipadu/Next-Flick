package com.pihrit.nextflick.enums;

import com.pihrit.nextflick.R;

public enum SelectedFilter {
    POPULAR(R.string.subtitle_popular_movies), TOP_RATED(R.string.subtitle_top_rated_movies);

    private int titleResId;

    SelectedFilter(int titleResId) {
        this.titleResId = titleResId;
    }

    public int getTitleResId() {
        return titleResId;
    }
}
