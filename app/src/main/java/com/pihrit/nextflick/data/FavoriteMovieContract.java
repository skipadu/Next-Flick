package com.pihrit.nextflick.data;

import android.provider.BaseColumns;

public class FavoriteMovieContract {

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
