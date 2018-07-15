package com.ryo.muhammad.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.ViewModel.MainViewModel;
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
    private long lastMenuItemId;
    private MainViewModel mainViewModel;
    private boolean isLoading = false;
    private static final int FAVORITE_ID = 10;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        setupViewModel();
        setupNavDrawer(savedInstanceState);
        setupRecyclerView();
    }


    private void setupViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.setOnMoviePageLoaded(new DataManager.OnMoviePageLoaded() {
            @Override
            public void onLoadFinished(List<Movie> data) {
                isLoading = false;
                if (data == null) {
                    noPaginate.showError(true);
                } else {
                    adapter.addItems(data);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSortAs(drawer.getDrawerItem(lastMenuItemId));
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter(mainViewModel.getMovies(), this, new ListItemOnClick());
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

    private void updateRecyclerView() {
        mainViewModel.clearMovies();
        adapter.resetData();
    }

    private void setupNavDrawer(Bundle savedInstanceState) {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(binding.toolbar)
                .inflateMenu(R.menu.main_menu)
                .addStickyDrawerItems(new SecondaryDrawerItem().withName("My Favorite")
                        .withIcon(R.drawable.ic_favorite_black_24dp).withIdentifier(FAVORITE_ID))
                .addStickyDrawerItems()
                .withSavedInstance(savedInstanceState)
                .withDrawerWidthDp(200)
                .withOnDrawerItemClickListener(new DrawerListener())
                .build();
    }

    private void loadNewPage() {
        if (!isLoading) {
            if (NetworkUtils.isOnline(this)) {
                mainViewModel.loadNextPage();
                isLoading = true;
            } else {
                noPaginate.showError(true);
            }
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
        intent.putExtra(DetailedActivity.EXTRA_IS_FAVORITE, isFavorite);
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
                mainViewModel.setSortAs(MovieSortBy.POPULARITY);
            } else if (drawerItem.getIdentifier() == R.id.top_rated_mi) {
                mainViewModel.setSortAs(MovieSortBy.TOP_RATED);
            } else if (drawerItem.getIdentifier() == R.id.release_date_mi) {
                mainViewModel.setSortAs(MovieSortBy.RELEASE_DATE);
            } else if (drawerItem.getIdentifier() == R.id.revenue_mi) {
                mainViewModel.setSortAs(MovieSortBy.REVENUE);
            } else if (drawerItem.getIdentifier() == R.id.primary_release_date_mi) {
                mainViewModel.setSortAs(MovieSortBy.PRIMARY_RELEASE_DATE);
            } else if (drawerItem.getIdentifier() == R.id.original_title_mi) {
                mainViewModel.setSortAs(MovieSortBy.ORIGINAL_TITLE);
            } else if (drawerItem.getIdentifier() == R.id.vote_average_mi) {
                mainViewModel.setSortAs(MovieSortBy.VOTE_AVERAGE);
            } else if (drawerItem.getIdentifier() == R.id.vote_count_mi) {
                mainViewModel.setSortAs(MovieSortBy.VOTE_COUNT);
            } else if (drawerItem.getIdentifier() == FAVORITE_ID) {
                setupFavorite();
            } else {
                return;
            }
            if (drawerItem.getIdentifier() != FAVORITE_ID) {
                noPaginate.setNoMoreItems(false);
                updateRecyclerView();
                isFavorite = false;
            }
            lastMenuItemId = drawerItem.getIdentifier();
        }
    }

    private void setupFavorite() {
        isFavorite = true;
        noPaginate.setNoMoreItems(true);
        mainViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                updateRecyclerView();
                adapter.addItems(movies);
            }
        });
    }

    private class ListItemOnClick implements MovieAdapter.OnListItemClicked {

        @Override
        public void onClick(View v, int position, Movie movie) {
            goToDetailedActivity(movie);
        }
    }
}