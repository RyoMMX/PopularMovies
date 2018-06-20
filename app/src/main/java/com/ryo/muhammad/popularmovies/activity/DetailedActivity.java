package com.ryo.muhammad.popularmovies.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.databinding.ActivityDetailedBinding;
import com.ryo.muhammad.popularmovies.jsonModel.Movie;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

public class DetailedActivity extends Activity {
    public static final String EXTRA_MOVE_JSON = "M";
    private ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailed);

        setupUI();
    }

    @SuppressLint("DefaultLocale")
    private void setupUI() {
        Intent intent = getIntent();
        if (intent != null) {
            String json = intent.getStringExtra(EXTRA_MOVE_JSON);
            Movie movie = new Gson().fromJson(json, Movie.class);

            if (movie.getBackdropPath() != null) {
                NetworkUtils.loadImage(this, movie.getBackdropPath(), binding.posterIv, NetworkUtils.LARGE_IMAGE);
            }

            if (movie.getTitle() != null) {
                binding.toolbar.setTitle(movie.getTitle());
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

        }
    }
}
