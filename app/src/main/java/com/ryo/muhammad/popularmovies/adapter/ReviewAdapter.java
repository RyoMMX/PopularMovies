package com.ryo.muhammad.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryo.muhammad.popularmovies.R;
import com.ryo.muhammad.popularmovies.jsonModel.reivew.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Holder> {
    private List<Review> reviews;

    public ReviewAdapter() {
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Review review = reviews.get(position);

        if (review != null) {
            if (review.getAuthor() != null) {
                holder.nameTV.setText(String.format("By : %s", review.getAuthor()));
            }

            if (review.getContent() != null) {
                holder.commentTV.setText(review.getContent());
            }
        }
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        final TextView nameTV;
        final TextView commentTV;

        Holder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.name_tv);
            commentTV = itemView.findViewById(R.id.comment_tv);
        }
    }
}
