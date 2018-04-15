package com.pihrit.nextflick.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoritemovie.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITEMOVIE_TABLE = "CREATE TABLE " +
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_HAS_VIDEO + " INTEGER DEFAULT(0), " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +

                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITEMOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//         If making new version, should make appending instead of dropping all the data
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
