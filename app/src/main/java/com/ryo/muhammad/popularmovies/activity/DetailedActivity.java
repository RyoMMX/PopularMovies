package com.ryo.muhammad.popularmovies.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.rey.material.widget.FloatingActionButton;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.ViewModel.DetailedViewModel;
import com.ryo.muhammad.popularmovies.adapter.GenreAdapter;
import com.ryo.muhammad.popularmovies.adapter.ReviewAdapter;
import com.ryo.muhammad.popularmovies.adapter.TrailerAdapter;
import com.ryo.muhammad.popularmovies.database.AppDatabase;
import com.ryo.muhammad.popularmovies.database.AppExecutor;
import com.ryo.muhammad.popularmovies.database.MovieDao;
import com.ryo.muhammad.popularmovies.databinding.ActivityDetailedBinding;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Genres;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.Review;
import com.ryo.muhammad.popularmovies.jsonModel.video.Trailer;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

import java.util.List;

public class DetailedActivity extends AppCompatActivity implements TrailerAdapter.OnItemClickListener {
    public static final String EXTRA_MOVE_JSON = "M";
    public static final String EXTRA_IS_FAVORITE = "F";
    private ActivityDetailedBinding binding;
    private static final String TAG = DetailedActivity.class.getSimpleName();
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private DetailedViewModel detailedViewModel;
    private Movie movie;
    private boolean isOnOpenActivity = true;
    private boolean isDBProcessing = false;
    private Toast toast;
    private StyleableToast styleableToast;
    private List<Review> reviews;
    private List<Trailer> trailers;
    private boolean isDataLoaded = false;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed);
        detailedViewModel = ViewModelProviders.of(this).get(DetailedViewModel.class);
        isFavorite = getIntent().getBooleanExtra(EXTRA_IS_FAVORITE, false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupUI();
    }

    @SuppressLint("DefaultLocale")
    private void setupUI() {
        Intent intent = getIntent();
        if (intent != null) {
            String json = intent.getStringExtra(EXTRA_MOVE_JSON);
            Movie movie = new Gson().fromJson(json, Movie.class);
            this.movie = movie;

            if (movie.getBackdropPath() != null) {
                NetworkUtils.loadImage(this, movie.getBackdropPath(),
                        binding.posterIv, NetworkUtils.LARGE_IMAGE);
                binding.posterIv.setPadding(0, 0, 0, 0);
            } else if (movie.getPosterPath() != null) {
                NetworkUtils.loadImage(this, movie.getPosterPath(),
                        binding.posterIv, NetworkUtils.LARGE_IMAGE);
                binding.posterIv.setPadding(0, 0, 0, 0);
            }

            if (movie.getTitle() != null) {
                binding.collapsingToolbarLayout.setTitle(movie.getTitle());
            }

            if (movie.getOriginalTitle() != null) {
                binding.content.originalTitleTv.setText(movie.getOriginalTitle());
            }

            if (movie.getReleaseDate() != null) {
                binding.content.releaseDateTV.setText(movie.getReleaseDate());
            }

            binding.content.voteTv.setText(
                    String.format("Number of Votes: %d\nVote Average: %s",
                            movie.getVoteCount(), movie.getVoteAverage()));

            if (movie.getOverview() != null) {
                binding.content.overviewTv.setText(movie.getOverview());
            }

            if (!movie.isAdult()) {
                binding.content.adultCV.setVisibility(View.GONE);
            }

            if (movie.getGenreIds() != null) {
                setupGenreRecyclerView(Genres.getGenreStrings(movie.getGenreIds()));
            }

            if (movie.getId() != 0) {
                setupTrailerRecyclerView(movie.getId());
                setupReviewRecyclerView(movie.getId());
                setupFAB(movie.getId());
            }

        } else {
            closeOnError();
        }
    }

    private void setupFAB(final int moveId) {
        detailedViewModel.getMovieLiveData(moveId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie == null) {
                    binding.buttonBtFloatColor.setLineMorphingState(0, !isOnOpenActivity);
                } else {
                    binding.buttonBtFloatColor.setLineMorphingState(1, !isOnOpenActivity);
                    if (!isOnOpenActivity) {
                        styleableToast = StyleableToast.makeText(DetailedActivity.this,
                                "The movie is add to favorite", Toast.LENGTH_LONG, R.style.save_to_favorite_toast);
                        styleableToast.show();
                        styleableToast = null;
                    }
                }
                isOnOpenActivity = false;
            }
        });

        binding.buttonBtFloatColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v instanceof FloatingActionButton) {
                    if (isDBProcessing) {
                        toast = Toast.makeText(DetailedActivity.this,
                                "Please don't break the button (^_^)", Toast.LENGTH_SHORT);
                        toast.show();
                        toast = null;
                    } else {
                        isDBProcessing = true;
                        detailedViewModel.getMovieLiveData(moveId).
                                observe(DetailedActivity.this, new Observer<Movie>() {
                                    @Override
                                    public void onChanged(@Nullable Movie movie) {
                                        if (movie == null) {
                                            saveToFavorite();
                                        } else {
                                            deleteFromFavorite();
                                        }
                                        detailedViewModel.getMovieLiveData(moveId).removeObserver(this);
                                        isDBProcessing = false;
                                    }
                                });
                    }
                }
            }
        });
    }

    private void deleteFromFavorite() {
        AppExecutor.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(DetailedActivity.this).movieDao().deleteMovie(movie);
            }
        });
    }

    private void saveToFavorite() {
        if (isDataLoaded) {
            AppExecutor.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    MovieDao movieDao = AppDatabase.getInstance(getApplicationContext()).movieDao();
                    movieDao.insertMovie(movie);
                    for (Review review : reviews) {
                        review.setMovieId(movie.getId());
                        movieDao.insertReview(review);
                    }

                    for (Trailer trailer : trailers) {
                        trailer.setMovieId(movie.getId());
                        movieDao.insertTrailer(trailer);
                    }
                }
            });
        } else {
            styleableToast = StyleableToast.makeText(DetailedActivity.this,
                    "Please wait", Toast.LENGTH_LONG, R.style.wait_toast);
            styleableToast.show();
            styleableToast = null;
        }
    }

    private void setupReviewRecyclerView(final int movieId) {
        reviewAdapter = new ReviewAdapter();
        RecyclerView.LayoutManager layoutManager = new NotScrollableLinerLayoutManager(this);
        binding.content.reviewRV.setAdapter(reviewAdapter);
        binding.content.reviewRV.setLayoutManager(layoutManager);
        binding.content.reviewRV.setItemAnimator(new DefaultItemAnimator());
        binding.content.reviewRV.setHasFixedSize(true);

        final LoadListener loadListener = new LoadListener();

        if (isFavorite) {
            binding.content.reviewErrorFl.setVisibility(View.GONE);
            binding.content.reviewPb.setVisibility(View.GONE);
            detailedViewModel.getReviewsLiveData(movieId).observe(this, new Observer<List<Review>>() {
                @Override
                public void onChanged(@Nullable List<Review> reviews) {
                    reviewAdapter.setReviews(reviews);
                }
            });
        } else {
            detailedViewModel.setOnReviewsLoadListener(movieId, loadListener);

            binding.content.reviewErrorInclude.btnRepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailedViewModel.setOnReviewsLoadListener(movieId, loadListener);
                }
            });
        }

    }

    private void setupTrailerRecyclerView(final int movieId) {
        trailerAdapter = new TrailerAdapter(this);
        RecyclerView.LayoutManager layoutManager = new NotScrollableLinerLayoutManager(this);
        binding.content.trailersRV.setAdapter(trailerAdapter);
        binding.content.trailersRV.setLayoutManager(layoutManager);
        binding.content.trailersRV.setItemAnimator(new DefaultItemAnimator());
        binding.content.trailersRV.setHasFixedSize(true);

        final LoadListener loadListener = new LoadListener();

        if (isFavorite) {
            binding.content.trailerErrorFl.setVisibility(View.GONE);
            binding.content.trailersPb.setVisibility(View.GONE);
            detailedViewModel.getTrailersLiveData(movieId).observe(this, new Observer<List<Trailer>>() {
                @Override
                public void onChanged(@Nullable List<Trailer> trailers) {
                    trailerAdapter.setTrailers(trailers);
                }
            });
        } else {
            detailedViewModel.setOnTrailersLoadListener(movieId, loadListener);

            binding.content.trailersErrorInclude.btnRepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailedViewModel.setOnTrailersLoadListener(movieId, loadListener);
                }
            });
        }
    }

    private void setupGenreRecyclerView(List<String> genreTypes) {
        RecyclerView.LayoutManager layoutManager = new NotScrollableLinerLayoutManager(this);
        binding.content.genreTypesRV.setLayoutManager(layoutManager);
        binding.content.genreTypesRV.setHasFixedSize(true);
        binding.content.genreTypesRV.setItemAnimator(new DefaultItemAnimator());
        GenreAdapter adapter = new GenreAdapter(genreTypes);
        binding.content.genreTypesRV.setAdapter(adapter);
    }

    private void closeOnError() {
        Log.e(TAG, "Intent is null in DetailActivity");
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onClick(View view, Trailer trailer) {
        if (trailer.getKey() != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://youtu.be/" + trailer.getKey()));
            startActivity(intent);
        }
    }

    private class LoadListener implements DetailedViewModel.OnTrailersLoadListener,
            DetailedViewModel.OnReviewsLoadListener {

        @Override
        public void onTrailersLoad(List<Trailer> trailers) {
            if (trailers != null) {
                DetailedActivity.this.trailers = trailers;
                trailerAdapter.setTrailers(trailers);
                binding.content.trailerErrorFl.setVisibility(View.GONE);
                if (reviews != null) {
                    isDataLoaded = true;
                }
            } else {
                binding.content.trailerErrorFl.setVisibility(View.VISIBLE);
            }
            binding.content.trailersPb.setVisibility(View.GONE);
        }

        @Override
        public void onReviewsLoad(List<Review> reviews) {
            if (reviews != null) {
                DetailedActivity.this.reviews = reviews;
                reviewAdapter.setReviews(reviews);
                binding.content.reviewErrorFl.setVisibility(View.GONE);
                if (trailers != null) {
                    isDataLoaded = true;
                }
            } else {
                binding.content.reviewErrorFl.setVisibility(View.VISIBLE);
            }
            binding.content.reviewPb.setVisibility(View.GONE);
        }
    }

    private static class NotScrollableLinerLayoutManager extends LinearLayoutManager {

        NotScrollableLinerLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }
}


