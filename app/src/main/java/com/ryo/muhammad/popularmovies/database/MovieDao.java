package com.ryo.muhammad.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM Movie WHERE id = :id")
    LiveData<Movie> getMoviesById(int id);

    @Delete
    void deleteMovie(Movie movie);
}
