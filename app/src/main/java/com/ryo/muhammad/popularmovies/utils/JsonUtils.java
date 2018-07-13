package com.ryo.muhammad.popularmovies.utils;

import com.google.gson.Gson;
import com.ryo.muhammad.popularmovies.jsonModel.movie.MovieRoot;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;

import java.util.List;

public class JsonUtils {
    public static List<Movie> getRootFromJson(String json) {
        MovieRoot root = new Gson().fromJson(json, MovieRoot.class);
        return root.getResults();
    }
}