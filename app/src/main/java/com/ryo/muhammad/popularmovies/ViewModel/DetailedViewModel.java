package com.ryo.muhammad.popularmovies.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ryo.muhammad.popularmovies.database.AppDatabase;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.Review;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.ReviewRoot;
import com.ryo.muhammad.popularmovies.jsonModel.video.Trailer;
import com.ryo.muhammad.popularmovies.jsonModel.video.VideoRoot;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedViewModel extends AndroidViewModel {
    private static final String TAG = DetailedViewModel.class.getSimpleName();
    private List<Trailer> trailers;
    private List<Review> reviews;
    private LiveData<Movie> movieLiveData;

    public DetailedViewModel(@NonNull Application application) {
        super(application);
    }

    public void setOnTrailersLoadListener(int movieId, final OnTrailersLoadListener onTrailerLoadListener) {
        if (trailers == null) {
            Call<VideoRoot> videoRootCall = NetworkUtils.getTrailers(movieId);
            if (!videoRootCall.isExecuted()) {
                videoRootCall.enqueue(new Callback<VideoRoot>() {
                    @Override
                    public void onResponse(@NonNull Call<VideoRoot> call,
                                           @NonNull Response<VideoRoot> response) {
                        VideoRoot videoRoot = response.body();
                        if (videoRoot != null) {
                            if (videoRoot.getResults() != null) {
                                trailers = videoRoot.getResults();
                            } else {
                                trailers = new ArrayList<>();
                            }

                            onTrailerLoadListener.onTrailersLoad(trailers);

                            Log.v(TAG, "trailer size is" + (videoRoot.getResults() != null ?
                                    videoRoot.getResults().size() : 0));
                            Log.v(TAG, "trailer url " + response.raw().request().url());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VideoRoot> call, @NonNull Throwable t) {
                        onTrailerLoadListener.onTrailersLoad(trailers);
                    }
                });
            }
        } else {
            onTrailerLoadListener.onTrailersLoad(trailers);
        }
    }

    public void setOnReviewsLoadListener(int movieId, final OnReviewsLoadListener onReviewsLoadListener) {
        if (reviews == null) {
            Call<ReviewRoot> reviewRootCall = NetworkUtils.getReviews(movieId);

            if (!reviewRootCall.isExecuted()) {
                reviewRootCall.enqueue(new Callback<ReviewRoot>() {
                    @Override
                    public void onResponse(@NonNull Call<ReviewRoot> call, @NonNull Response<ReviewRoot> response) {
                        ReviewRoot reviewRoot = response.body();
                        if (reviewRoot != null) {
                            if (reviewRoot.getResults() != null) {
                                reviews = reviewRoot.getResults();
                            } else {
                                reviews = new ArrayList<>();
                            }

                            onReviewsLoadListener.onReviewsLoad(reviews);

                            Log.v(TAG, "review size is" + (reviewRoot.getResults() != null ?
                                    reviewRoot.getResults().size() : 0));
                            Log.v(TAG, "review url " + response.raw().request().url());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ReviewRoot> call, @NonNull Throwable t) {
                        onReviewsLoadListener.onReviewsLoad(reviews);
                    }
                });
            }
        } else {
            onReviewsLoadListener.onReviewsLoad(reviews);
        }
    }

    public LiveData<Movie> getMovieLiveData(int id) {
        if (movieLiveData == null) {
            movieLiveData = AppDatabase.getInstance(getApplication()
                    .getApplicationContext()).movieDao().getMoviesById(id);
        }
        return movieLiveData;
    }

    public interface OnTrailersLoadListener {
        void onTrailersLoad(List<Trailer> trailers);
    }

    public interface OnReviewsLoadListener {
        void onReviewsLoad(List<Review> reviews);
    }
}
