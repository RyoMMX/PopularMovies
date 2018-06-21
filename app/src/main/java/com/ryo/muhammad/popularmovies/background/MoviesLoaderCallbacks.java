package com.ryo.muhammad.popularmovies.background;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.ryo.muhammad.popularmovies.jsonModel.Movie;
import com.ryo.muhammad.popularmovies.utils.MovieSortBy;

import java.util.List;

public class MoviesLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private final Context context;
    private final OnMoviePageLoaded onMoviePageLoaded;
    private final MovieSortBy sortBy;
    private final int page;

    MoviesLoaderCallbacks(Context context, int page, MovieSortBy sortBy, OnMoviePageLoaded onMoviePageLoaded) {
        this.context = context;
        this.sortBy = sortBy;
        this.onMoviePageLoaded = onMoviePageLoaded;
        this.page = page;
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MovieAsyncTack(context, page, sortBy);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        onMoviePageLoaded.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
    }

    public interface OnMoviePageLoaded {
        void onLoadFinished(List<Movie> data);
    }
}