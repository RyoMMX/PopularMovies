package com.ryo.muhammad.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.jsonModel.movie.Movie;
import com.ryo.muhammad.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {
    private final List<Movie> movies;
    private final Context context;
    private final OnListItemClicked onListItemClicked;

    public MovieAdapter(Context context, OnListItemClicked onListItemClicked) {
        this.movies = new ArrayList<>();
        this.context = context;
        this.onListItemClicked = onListItemClicked;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final Movie movie = movies.get(position);
        final int i = position;
        if (movie != null) {
            if (movie.getPosterPath() != null) {
                NetworkUtils.loadImage(context, movie.getPosterPath(), holder.posterIV, NetworkUtils.THUMBNAIL_IMAGE);
            }

            if (movie.getTitle() != null) {
                holder.titleTV.setText(movie.getTitle());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListItemClicked.onClick(v, i, movie);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void addItem(List<Movie> movies) {
        if (movies != null && !movies.isEmpty()) {
            this.movies.addAll(movies);
            notifyDataSetChanged();
        }
    }

    public void resetData() {
        movies.clear();
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        private final ImageView posterIV;
        private final TextView titleTV;

        Holder(View itemView) {
            super(itemView);
            this.posterIV = itemView.findViewById(R.id.poster_iv);
            this.titleTV = itemView.findViewById(R.id.title_tv);
        }
    }

    public interface OnListItemClicked {
        void onClick(View v, int position, Movie movie);
    }
}
