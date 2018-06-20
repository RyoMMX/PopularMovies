package com.ryo.muhammad.popularmovies.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.adapter.MovieAdapter;
import com.ryo.muhammad.popularmovies.background.DataManager;
import com.ryo.muhammad.popularmovies.background.MoviesLoaderCallbacks;
import com.ryo.muhammad.popularmovies.databinding.ActivityMainBinding;
import com.ryo.muhammad.popularmovies.jsonModel.Movie;
import com.ryo.muhammad.popularmovies.utils.MovieSortBy;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.callback.OnLoadMoreListener;
import ru.alexbykov.nopaginate.callback.OnRepeatListener;
import ru.alexbykov.nopaginate.item.ErrorItem;
import ru.alexbykov.nopaginate.paginate.NoPaginate;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MovieAdapter adapter;
    private Drawer drawer;
    private NoPaginate noPaginate;
    private DataManager dataManager;
    private long lastMenuItemPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupToolbar();
        setupNavDrawer(savedInstanceState);
        setupRecylerView();
        setupDataManager();
    }

    private void setupDataManager() {
        dataManager = new DataManager(this, MovieSortBy.POPULARITY,
                new MoviesLoaderCallbacks.OnMoviePageLoaded() {
                    @Override
                    public void onLoadFinished(List<Movie> data) {
                        if (data == null) {
                            noPaginate.showError(true);
                            if (dataManager != null) {
                                dataManager.resetPage();
                            }
                        } else {
                            adapter.addItem(data);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNewPage();
    }

    private void setupRecylerView() {
        adapter = new MovieAdapter(new ArrayList<Movie>(), this, new ListItemOnClick());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        binding.contentMain.moviesRv.setLayoutManager(layoutManager);
        binding.contentMain.moviesRv.setAdapter(adapter);
        binding.contentMain.moviesRv.setHasFixedSize(true);

        noPaginate = NoPaginate.with(binding.contentMain.moviesRv)
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        loadNewPage();
                    }
                })
                .setCustomErrorItem(new CustomErrorItem())
                .build();
    }

    private void setupNavDrawer(Bundle savedInstanceState) {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .inflateMenu(R.menu.main_menu)
                .withOnDrawerItemClickListener(new drawerListener())
                .withSavedInstance(savedInstanceState)
                .withDrawerWidthDp(200)
                .withOnDrawerItemClickListener(new DrawerListener())
                .build();
    }

    private void resetRecyclerView() {
        adapter = new MovieAdapter(new ArrayList<Movie>(), this, new ListItemOnClick());
        binding.contentMain.moviesRv.setAdapter(adapter);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private static class drawerListener implements Drawer.OnDrawerItemClickListener {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            return false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        drawer.saveInstanceState(outState);
    }

    private void loadNewPage() {
        dataManager.loadNextPage();
    }

    public class CustomErrorItem implements ErrorItem {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_error, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, final OnRepeatListener repeatListener) {
            Button btnRepeat = (Button) holder.itemView.findViewById(R.id.btnRepeat);
            btnRepeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (repeatListener != null) {
                        repeatListener.onClickRepeat();
                    }
                }
            });
        }
    }

    public void goToDetailedActivity(Movie movie) {
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra(DetailedActivity.EXTRA_MOVE_JSON, new Gson().toJson(movie));
        startActivity(intent);
    }

    public class DrawerListener implements Drawer.OnDrawerItemClickListener {

        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

            if (position != lastMenuItemPostion) {

                if (drawerItem.getIdentifier() == R.id.popularity_mi) {
                    dataManager.setSortAs(MovieSortBy.POPULARITY);
                } else if (drawerItem.getIdentifier() == R.id.release_date_mi) {
                    dataManager.setSortAs(MovieSortBy.RELEASE_DATE);
                } else if (drawerItem.getIdentifier() == R.id.revenue_mi) {
                    dataManager.setSortAs(MovieSortBy.REVENUE);
                } else if (drawerItem.getIdentifier() == R.id.primary_release_date_mi) {
                    dataManager.setSortAs(MovieSortBy.PRIMARY_RELEASE_DATE);
                } else if (drawerItem.getIdentifier() == R.id.origina_title_mi) {
                    dataManager.setSortAs(MovieSortBy.ORIGINAL_TITLE);
                } else if (drawerItem.getIdentifier() == R.id.vote_average_mi) {
                    dataManager.setSortAs(MovieSortBy.VOTE_AVERAGE);
                } else if (drawerItem.getIdentifier() == R.id.vote_count_mi) {
                    dataManager.setSortAs(MovieSortBy.VOTE_COUNT);
                }
                setupRecylerView();
                lastMenuItemPostion = position;
            }

            return false;
        }
    }

    private class ListItemOnClick implements MovieAdapter.OnListItemClicked {

        @Override
        public void onClick(View v, int position, Movie movie) {
            goToDetailedActivity(movie);
        }
    }
}