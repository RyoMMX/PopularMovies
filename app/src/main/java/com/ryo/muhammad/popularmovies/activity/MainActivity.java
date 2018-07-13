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
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.adapter.MovieAdapter;
import com.ryo.muhammad.popularmovies.background.DataManager;
import com.ryo.muhammad.popularmovies.databinding.ActivityMainBinding;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.utils.MovieSortBy;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

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
    private long lastMenuItemId;
    private static final String LAST_MENU_ITEM_ID_KEY = "I";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState != null) {
            lastMenuItemId = savedInstanceState.getLong(LAST_MENU_ITEM_ID_KEY);
        }

        setSupportActionBar(binding.toolbar);
        setupNavDrawer(savedInstanceState);
        setupRecyclerView();
        setupDataManager();
    }

    private void setupDataManager() {
        dataManager = new DataManager(MovieSortBy.POPULARITY,
                new DataManager.OnMoviePageLoaded() {
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
        setSortAs(drawer.getDrawerItem(lastMenuItemId));
        loadNewPage();
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter(this, new ListItemOnClick());
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

    private void updateRecylerView() {
        adapter.resetData();
    }

    private void setupNavDrawer(Bundle savedInstanceState) {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .inflateMenu(R.menu.main_menu)
                .withSavedInstance(savedInstanceState)
                .withDrawerWidthDp(200)
                .withOnDrawerItemClickListener(new DrawerListener())
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        drawer.saveInstanceState(outState);
        outState.putLong(LAST_MENU_ITEM_ID_KEY, lastMenuItemId);
    }

    private void loadNewPage() {
        if (NetworkUtils.isOnline(this)) {
            dataManager.loadNextPage();
        } else {
            noPaginate.showError(true);
        }
    }

    class CustomErrorItem implements ErrorItem {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_error, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, final OnRepeatListener repeatListener) {
            ImageButton imageButton = holder.itemView.findViewById(R.id.btnRepeat);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (repeatListener != null) {
                        repeatListener.onClickRepeat();
                    }
                }
            });
        }
    }

    private void goToDetailedActivity(Movie movie) {
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra(DetailedActivity.EXTRA_MOVE_JSON, new Gson().toJson(movie));
        startActivity(intent);
    }

    class DrawerListener implements Drawer.OnDrawerItemClickListener {

        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            setSortAs(drawerItem);
            return false;
        }
    }

    private void setSortAs(IDrawerItem drawerItem) {
        if (drawerItem != null && drawerItem.getIdentifier() != lastMenuItemId) {

            if (drawerItem.getIdentifier() == R.id.popularity_mi) {
                dataManager.setSortAs(MovieSortBy.POPULARITY);
            } else if (drawerItem.getIdentifier() == R.id.top_rated_mi) {
                dataManager.setSortAs(MovieSortBy.TOP_RATED);
            } else if (drawerItem.getIdentifier() == R.id.release_date_mi) {
                dataManager.setSortAs(MovieSortBy.RELEASE_DATE);
            } else if (drawerItem.getIdentifier() == R.id.revenue_mi) {
                dataManager.setSortAs(MovieSortBy.REVENUE);
            } else if (drawerItem.getIdentifier() == R.id.primary_release_date_mi) {
                dataManager.setSortAs(MovieSortBy.PRIMARY_RELEASE_DATE);
            } else if (drawerItem.getIdentifier() == R.id.original_title_mi) {
                dataManager.setSortAs(MovieSortBy.ORIGINAL_TITLE);
            } else if (drawerItem.getIdentifier() == R.id.vote_average_mi) {
                dataManager.setSortAs(MovieSortBy.VOTE_AVERAGE);
            } else if (drawerItem.getIdentifier() == R.id.vote_count_mi) {
                dataManager.setSortAs(MovieSortBy.VOTE_COUNT);
            }
            updateRecylerView();
            lastMenuItemId = drawerItem.getIdentifier();
        }
    }

    private class ListItemOnClick implements MovieAdapter.OnListItemClicked {

        @Override
        public void onClick(View v, int position, Movie movie) {
            goToDetailedActivity(movie);
        }
    }
}