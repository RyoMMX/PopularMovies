package com.ryo.muhammad.popularmovies.background;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.ryo.muhammad.popularmovies.utils.MovieSortBy;

public class DataManager {
    private static final int MOVIE_LOADER_ID = 0;
    private int page = 1;

    private final FragmentActivity fragmentActivity;
    private final MoviesLoaderCallbacks.OnMoviePageLoaded onMoviePageLoaded;
    private MovieSortBy sortAs;

    public DataManager(FragmentActivity fragmentActivity, MovieSortBy sortAs,
                       MoviesLoaderCallbacks.OnMoviePageLoaded onMoviePageLoaded) {

        this.fragmentActivity = fragmentActivity;
        this.sortAs = sortAs;
        this.onMoviePageLoaded = onMoviePageLoaded;
    }

    public void loadNextPage() {
        LoaderManager manager = fragmentActivity.getSupportLoaderManager();
        manager.destroyLoader(MOVIE_LOADER_ID);
        manager.initLoader(MOVIE_LOADER_ID, null,
                new MoviesLoaderCallbacks(fragmentActivity, page, sortAs, onMoviePageLoaded)).forceLoad();
        page++;
    }

    public void resetPage() {
        page = 1;
    }

    public void setSortAs(MovieSortBy sortAs) {
        this.sortAs = sortAs;
        resetPage();
    }
}
