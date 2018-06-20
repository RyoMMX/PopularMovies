package com.ryo.muhammad.popularmovies.utils;

import com.google.gson.Gson;
import com.ryo.muhammad.popularmovies.jsonModel.Root;
import com.ryo.muhammad.popularmovies.jsonModel.Movie;

import java.util.List;

public class JsonUtils {
    public static List<Movie> getRootFromJson(String json) {
        Root root = new Gson().fromJson(json, Root.class);
        List<Movie> results = root.getResults();
        return results;
    }
}