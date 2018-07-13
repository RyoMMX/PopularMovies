package com.ryo.muhammad.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters(AppTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static final String DATABASE_NAME = "movie database";
    private static Object LOCK = new Object();
    private static final String TAG = AppDatabase.class.getSimpleName();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                Log.e(TAG, "Creating database instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return instance;
    }


    public abstract MovieDao movieDao();
}
