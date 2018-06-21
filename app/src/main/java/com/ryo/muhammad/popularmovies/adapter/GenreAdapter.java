package com.ryo.muhammad.popularmovies.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryo.muhammad.popularmovies.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.Holder> {
    private final List<String> genres;

    public GenreAdapter(List<String> genres) {
        this.genres = genres;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_list_item, parent, false));

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String ingredient = genres.get(position);

        if (ingredient != null) {
            holder.labelTV.setText(ingredient);
        }
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        final TextView labelTV;

        Holder(View itemView) {
            super(itemView);
            labelTV = itemView.findViewById(R.id.label_tv);
        }
    }
}
