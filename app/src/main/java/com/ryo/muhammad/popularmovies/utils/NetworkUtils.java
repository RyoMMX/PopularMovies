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
import com.ryo.muhammad.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    public static final int THUMBNAIL_IMAGE = 0;
    public static final int LARGE_IMAGE = 1;

    private static final String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String API_KEY_KEY = "api_key";
    //TODO remove the api_key value before publishing
    private static final String API_KEY_VALUE = "b983f4d87827c8fb33e44a28e9438aa2";
    private static final String SORT_BY_KEY = "sort_by";
    private static final String PAGE_KEY = "page";

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static URL createURL(int page, MovieSortBy sortBy) {
        Uri.Builder uriBuilder = Uri.parse(THE_MOVIE_DB_BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                .appendQueryParameter(SORT_BY_KEY, sortBy.toString())
                .appendQueryParameter(PAGE_KEY, String.valueOf(page));

        URL url = null;
        try {
            url = new URL(uriBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "failed to create URL");
        }
        Log.v(TAG, "url : " + url);
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

    public static String getJsonPage(int page, MovieSortBy movieSortBy) throws IOException {
        return getJson(createURL(page, movieSortBy));
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
}
