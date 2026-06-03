package com.example.nearbite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import android.content.Intent;

public class RestaurantAdapter
        extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    List<Restaurant> restaurantList;

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Restaurant restaurant =
                restaurantList.get(position);

        holder.nameText.setText(
                restaurant.getName());

        holder.ratingText.setText(
                "⭐ Rating: " + restaurant.getRating());

        Glide.with(holder.itemView.getContext())
                .load(restaurant.getImageUrl())
                .into(holder.restaurantImage);

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    RestaurantDetailsActivity.class);

            intent.putExtra(
                    "name",
                    restaurant.getName());

            intent.putExtra(
                    "rating",
                    restaurant.getRating());

            intent.putExtra(
                    "image",
                    restaurant.getImageUrl());

            holder.itemView.getContext()
                    .startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView nameText, ratingText;
        ImageView restaurantImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText =
                    itemView.findViewById(R.id.nameText);

            ratingText =
                    itemView.findViewById(R.id.ratingText);

            restaurantImage =
                    itemView.findViewById(R.id.restaurantImage);
        }
    }
}