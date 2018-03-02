package com.pihrit.nextflick.interfaces;

import com.pihrit.nextflick.model.TmdbJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {
    @GET("3/movie/popular")
    Call<TmdbJsonResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<TmdbJsonResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}
