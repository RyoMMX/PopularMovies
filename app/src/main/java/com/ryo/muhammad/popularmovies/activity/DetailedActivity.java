package com.ryo.muhammad.popularmovies.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.adapter.GenreAdapter;
import com.ryo.muhammad.popularmovies.databinding.ActivityDetailedBinding;
import com.ryo.muhammad.popularmovies.jsonModel.Genres;
import com.ryo.muhammad.popularmovies.jsonModel.Movie;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

import java.util.List;

public class DetailedActivity extends AppCompatActivity {
    public static final String EXTRA_MOVE_JSON = "M";
    private ActivityDetailedBinding binding;
    private static final String TAG = DetailedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed);

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
                setupGenreRecylerView(Genres.getGenreStrings(movie.getGenreIds()));
            }

        } else {
            closeOnError();
        }
    }

    private void setupGenreRecylerView(List<String> genreTypes) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        binding.content.genreTypesRV.setLayoutManager(layoutManager);
        binding.content.genreTypesRV.setItemAnimator(new DefaultItemAnimator());
        GenreAdapter adapter = new GenreAdapter(genreTypes);
        binding.content.genreTypesRV.setAdapter(adapter);

    }

    private void closeOnError() {
        Log.e(TAG, "Intent is null in DetailActivity");
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
        finish();
    }
}


