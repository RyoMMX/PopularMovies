package com.ryo.muhammad.popularmovies.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ryo.muhammad.popularmovies.BuildConfig;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.jsonModel.movie.MovieRoot;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.ReviewRoot;
import com.ryo.muhammad.popularmovies.jsonModel.video.VideoRoot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    public static final int THUMBNAIL_IMAGE = 0;
    public static final int LARGE_IMAGE = 1;

    private static final String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String THE_MOVIE_DB_VIDEO_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String THE_MOVIE_DB_POPULAR_BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String THE_MOVIE_DB_TOP_RATED_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated";

    private static final String API_KEY_KEY = "api_key";
    //TODO ADD your api key here
    private static final String API_KEY_VALUE = BuildConfig.API_KEY;
    private static final String SORT_BY_KEY = "sort_by";
    private static final String PAGE_KEY = "page";

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static URL createURL(int page, MovieSortBy sortBy) {
        Uri.Builder uriBuilder;
        if (sortBy.toString().equals(MovieSortBy.POPULARITY.toString())) {
            uriBuilder = Uri.parse(THE_MOVIE_DB_POPULAR_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                    .appendQueryParameter(PAGE_KEY, String.valueOf(page));

        } else if (sortBy.toString().equals(MovieSortBy.TOP_RATED.toString())) {
            uriBuilder = Uri.parse(THE_MOVIE_DB_TOP_RATED_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                    .appendQueryParameter(PAGE_KEY, String.valueOf(page));
        } else {
            uriBuilder = Uri.parse(THE_MOVIE_DB_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                    .appendQueryParameter(SORT_BY_KEY, sortBy.toString())
                    .appendQueryParameter(PAGE_KEY, String.valueOf(page));
        }
        URL url = null;

        if (uriBuilder != null) {
            try {
                url = new URL(uriBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG, "failed to create URL");
            }
            Log.v(TAG, "url : " + url);
        }


        return url;
    }

    private static String getJson(URL url) throws IOException {
        String json = null;

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        if (scanner.hasNext()) {
            json = scanner.next();
        }

        return json;
    }

    @SuppressLint("CheckResult")
    public static void loadImage(Context context, String imagePath, ImageView imageView, int imageSize) {
        String url = null;
        if (imageSize == THUMBNAIL_IMAGE) {
            url = String.format("http://image.tmdb.org/t/p/w185%s", imagePath);
        } else if (imageSize == LARGE_IMAGE) {
            url = String.format("http://image.tmdb.org/t/p/original%s", imagePath);
        }

        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.ic_broken))
                    .into(imageView);
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Call<VideoRoot> getTrailers(int movieId) {
        MovieService movieService = getMovieService();

        return movieService.getTrailers(movieId);
    }

    public static Call<ReviewRoot> getReviews(int movieId) {
        MovieService movieService = getMovieService();

        return movieService.getReviews(movieId);
    }

    public static Call<MovieRoot> getMovies(int page, MovieSortBy sortAs) {
        MovieService movieService = getMovieService();
        Call<MovieRoot> movieRootCall;
        if (sortAs.equals(MovieSortBy.POPULARITY) || sortAs.equals(MovieSortBy.TOP_RATED)) {
            movieRootCall = movieService.getMovies(sortAs.toString(), page);
        } else {
            movieRootCall = movieService.getDiscoverMovies(sortAs.toString(), page);
        }

        return movieRootCall;
    }

    private static MovieService getMovieService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_VIDEO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MovieService.class);
    }
}
