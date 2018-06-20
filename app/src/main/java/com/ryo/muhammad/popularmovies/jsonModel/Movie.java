package com.ryo.muhammad.popularmovies.jsonModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {
    /**
     * vote_count : 792
     * id : 351286
     * video : false
     * vote_average : 6.7
     * title : Jurassic World: Fallen Kingdom
     * popularity : 275.025827
     * poster_path : /c9XxwwhPHdaImA2f1WEfEsbhaFB.jpg
     * original_language : en
     * original_title : Jurassic World: Fallen Kingdom
     * genre_ids : [28,12,878]
     * backdrop_path : /gBmrsugfWpiXRh13Vo3j0WW55qD.jpg
     * adult : false
     * overview : A volcanic eruption threatens the remaining dinosaurs on the island of Isla Nublar, where the creatures have freely roamed for several years after the demise of an animal theme park known as Jurassic World. Claire Dearing, the former park manager, has now founded the Dinosaur Protection Group, an organization dedicated to protecting the dinosaurs. To help with her cause, Claire has recruited Owen Grady, a former dinosaur trainer who worked at the park, to prevent the extinction of the dinosaurs once again.
     * release_date : 2018-06-06
     */

    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("id")
    private int id;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }
}
