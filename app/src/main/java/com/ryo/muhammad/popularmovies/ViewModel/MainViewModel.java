package com.ryo.muhammad.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.ryo.muhammad.popularmovies.background.DataManager;
import com.ryo.muhammad.popularmovies.database.AppDatabase;
import com.ryo.muhammad.popularmovies.database.AppExecutor;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.utils.MovieSortBy;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private DataManager dataManager;
    private List<Movie> movies = new ArrayList<>();
    private MovieListener movieListener;
    private LiveData<List<Movie>> favoriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        dataManager = new DataManager(MovieSortBy.POPULARITY);
    }

    public List<Movie> getMovies() {
        return movies;
    }


    public void clearMovies() {
        movies.clear();
    }

    public void loadNextPage() {
        dataManager.loadNextPage();
    }

    public void setSortAs(MovieSortBy sortAs) {
        dataManager.setSortAs(sortAs);
    }

    public MovieSortBy getSortAs() {
        return dataManager.getSortAs();
    }


    public void setOnMoviePageLoaded(DataManager.OnMoviePageLoaded onMoviePageLoaded) {
        if (movieListener == null) {
            movieListener = new MovieListener(onMoviePageLoaded);
            dataManager.setOnMoviePageLoaded(movieListener);
        } else {
            movieListener.setOnMoviePageLoaded(onMoviePageLoaded);
        }
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        if (favoriteMovies == null) {
            favoriteMovies = AppDatabase.getInstance(getApplication()
                    .getApplicationContext()).movieDao().getMovies();
        }
        return favoriteMovies;
    }

    private class MovieListener implements DataManager.OnMoviePageLoaded {
        private DataManager.OnMoviePageLoaded onMoviePageLoaded;

        MovieListener(DataManager.OnMoviePageLoaded onMoviePageLoaded) {
            this.onMoviePageLoaded = onMoviePageLoaded;
        }

        void setOnMoviePageLoaded(DataManager.OnMoviePageLoaded onMoviePageLoaded) {
            this.onMoviePageLoaded = onMoviePageLoaded;
        }

        @Override
        public void onLoadFinished(List<Movie> data) {
            if (movies == null) {
                movies = new ArrayList<>();
            }
            if (data != null) {
                movies.addAll(data);
            }
            onMoviePageLoaded.onLoadFinished(data);
        }
    }
}
