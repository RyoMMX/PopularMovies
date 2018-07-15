package com.ryo.muhammad.popularmovies.background;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.jsonModel.movie.MovieRoot;
import com.ryo.muhammad.popularmovies.utils.MovieSortBy;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {
    private int page = 1;
    private static final String TAG = DataManager.class.getSimpleName();

    private OnMoviePageLoaded onMoviePageLoaded;
    private MovieSortBy sortAs;

    public DataManager(MovieSortBy sortAs) {

        this.sortAs = sortAs;
    }

    public void setOnMoviePageLoaded(OnMoviePageLoaded onMoviePageLoaded) {
        this.onMoviePageLoaded = onMoviePageLoaded;
    }

    public void loadNextPage() {
        Call<MovieRoot> movieRootCall = NetworkUtils.getMovies(page, sortAs);
        if (!movieRootCall.isExecuted()) {
            movieRootCall.enqueue(new Callback<MovieRoot>() {
                @Override
                public void onResponse(@NonNull Call<MovieRoot> call,
                                       @NonNull Response<MovieRoot> response) {
                    if (response.body() != null) {
                        MovieRoot movieRoot = response.body();
                        if (movieRoot != null) {
                            onMoviePageLoaded.onLoadFinished(movieRoot.getResults());
                            Log.v(TAG, "url = " + response.raw().request().url());
                            Log.v(TAG, "data size = " + movieRoot.getResults().size());
                            page++;
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieRoot> call, @NonNull Throwable t) {
                    onMoviePageLoaded.onLoadFinished(null);
                }
            });
        }
    }

    private void resetPage() {
        page = 1;
    }

    public void setSortAs(MovieSortBy sortAs) {
        this.sortAs = sortAs;
        resetPage();
    }

    public MovieSortBy getSortAs() {
        return sortAs;
    }

    public interface OnMoviePageLoaded {
        void onLoadFinished(List<Movie> data);
    }
}
