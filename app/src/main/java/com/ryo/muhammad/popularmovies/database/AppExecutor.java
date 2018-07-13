package com.ryo.muhammad.popularmovies.database;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {
    private static Executor diskIO;

    public static Executor diskIO() {
        if (diskIO == null) {
            diskIO = Executors.newSingleThreadExecutor();
        }
        return diskIO;
    }

}
