package com.ryo.muhammad.popularmovies.background;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.ryo.muhammad.popularmovies.jsonModel.Movie;
import com.ryo.muhammad.popularmovies.utils.JsonUtils;
import com.ryo.muhammad.popularmovies.utils.MovieSortBy;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

class MovieAsyncTack extends AsyncTaskLoader<List<Movie>> {
    private final MovieSortBy movieSortBy;
    private final int page;

    MovieAsyncTack(@NonNull Context context, int page, MovieSortBy movieSortBy) {
        super(context);
        this.movieSortBy = movieSortBy;
        this.page = page;
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        List<Movie> movies = null;
        try {
            String json = NetworkUtils.getJsonPage(page, movieSortBy);
            movies = JsonUtils.getRootFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
