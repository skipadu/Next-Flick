package com.pihrit.nextflick.interfaces;

import com.pihrit.nextflick.model.TmdbJsonResponse;
import com.pihrit.nextflick.model.TmdbJsonReviewsResponse;
import com.pihrit.nextflick.model.TmdbJsonVideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApi {
    @GET("3/movie/popular")
    Call<TmdbJsonResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<TmdbJsonResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<TmdbJsonVideosResponse> getVideos(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<TmdbJsonReviewsResponse> getReviews(@Path("id") long movieId, @Query("api_key") String apiKey);

}
