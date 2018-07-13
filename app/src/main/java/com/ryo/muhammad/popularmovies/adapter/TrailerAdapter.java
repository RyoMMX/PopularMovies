package com.ryo.muhammad.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.jsonModel.video.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.Holder> {
    private List<Trailer> trailers;
    private OnItemClickListener onItemClickListener;

    public TrailerAdapter(List<Trailer> trailers, OnItemClickListener onItemClickListener) {
        this.trailers = trailers;
        this.onItemClickListener = onItemClickListener;
    }

    public TrailerAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final Trailer trailer = trailers.get(position);

        if (trailer != null) {
            if (trailer.getName() != null) {
                holder.labelTV.setText(trailer.getName());
            }

            if (trailer.getKey() != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onClick(view, trailer);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        final TextView labelTV;
        final View itemView;

        Holder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            labelTV = itemView.findViewById(R.id.label_tv);
        }
    }

    public static interface OnItemClickListener {

        void onClick(View view, Trailer trailer);
    }
}
