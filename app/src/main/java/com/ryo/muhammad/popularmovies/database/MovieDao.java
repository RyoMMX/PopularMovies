package com.ryo.muhammad.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.Review;
import com.ryo.muhammad.popularmovies.jsonModel.video.Trailer;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM Movie WHERE id = :id")
    LiveData<Movie> getMoviesById(int id);

    @Delete
    void deleteMovie(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview(Review review);


    @Query("SELECT * FROM Review WHERE movieId = :movieId")
    LiveData<List<Review>> getReviewsByMovieId(int movieId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrailer(Trailer trailer);


    @Query("SELECT * FROM Trailer WHERE movieId = :movieId")
    LiveData<List<Trailer>> getTrailerByMovieId(int movieId);
}
