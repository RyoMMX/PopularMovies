package com.ryo.muhammad.popularmovies.jsonModel;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Genres {

    public static List<String> getGenreStrings(List<Integer> genreIds) {
        ArrayList<String> genres = new ArrayList<>();
        SparseArray<String> genreArray = new SparseArray<>();
        genreArray.append(28, "Action");
        genreArray.append(12, "Adventure");
        genreArray.append(16, "Animation");
        genreArray.append(35, "Comedy");
        genreArray.append(80, "Crime");
        genreArray.append(99, "Documentary");
        genreArray.append(18, "Drama");
        genreArray.append(10751, "Family");
        genreArray.append(14, "Fantasy");
        genreArray.append(36, "History");
        genreArray.append(27, "Horror");
        genreArray.append(10402, "Music");
        genreArray.append(9648, "Mystery");
        genreArray.append(10749, "Romance");
        genreArray.append(878, "Science Fiction");
        genreArray.append(10770, "TV Movie");
        genreArray.append(53, "Thriller");
        genreArray.append(10752, "War");
        genreArray.append(37, "Western");

        for (Integer genreId : genreIds) {
            genres.add(genreArray.get(genreId));
        }
        return genres;
    }
}