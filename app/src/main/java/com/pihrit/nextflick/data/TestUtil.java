package com.pihrit.nextflick.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }
        List<ContentValues> fakeFavoriteMovies = new ArrayList<>();
        fakeFavoriteMovies.add(createFakeFavoriteMovie("Zootopia", 269149));
        fakeFavoriteMovies.add(createFakeFavoriteMovie("Jumanji: Welcome to the Jungle", 353486));
        fakeFavoriteMovies.add(createFakeFavoriteMovie("Star Wars: The Last Jedi", 181808));

        try {
            db.beginTransaction();
            // Clear the table
            db.delete(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, null);
            for (ContentValues movie : fakeFavoriteMovies) {
                db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, movie);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("TEST", "Problem occurred when trying to insert fake data");
        } finally {
            db.endTransaction();
        }

    }

    private static ContentValues createFakeFavoriteMovie(String title, int movieId) {
        ContentValues cv = new ContentValues();
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, title);
        cv.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movieId);
        return cv;
    }
}
