package com.ryo.muhammad.popularmovies.utils;

public enum MovieSortBy {
    POPULARITY("popular"),
    TOP_RATED("top_rated"),
    RELEASE_DATE("release_date.asc"),
    REVENUE("revenue.asc"),
    PRIMARY_RELEASE_DATE("primary_release_date.asc"),
    ORIGINAL_TITLE("original_title.asc"),
    VOTE_AVERAGE("vote_average.asc"),
    VOTE_COUNT("vote_count.asc");

    private final String value;

    MovieSortBy(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean equals(MovieSortBy movieSortBy) {
        return value.equals(movieSortBy.value);
    }

}
