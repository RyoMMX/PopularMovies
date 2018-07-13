package com.ryo.muhammad.popularmovies.utils;

import com.ryo.muhammad.popularmovies.BuildConfig;
import com.ryo.muhammad.popularmovies.jsonModel.movie.MovieRoot;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.ReviewRoot;
import com.ryo.muhammad.popularmovies.jsonModel.video.VideoRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    String API_KEY_VALUE = BuildConfig.API_KEY;


    @GET("movie/{movie_id}/videos?api_key=" + API_KEY_VALUE)
    Call<VideoRoot> getTrailers(@Path("movie_id") int movieId);


    @GET("movie/{movie_id}/reviews?api_key=" + API_KEY_VALUE + "&page=1")
    Call<ReviewRoot> getReviews(@Path("movie_id") int movieId);

    @GET("movie/{sort_by}?api_key=" + API_KEY_VALUE)
    Call<MovieRoot> getMovies(@Path("sort_by") String sortBy, @Query("page") int page);

    @GET("discover/movie?api_key=" + API_KEY_VALUE)
    Call<MovieRoot> getDiscoverMovies(@Query("sort_by") String sortBy, @Query("page") int page);

}