package com.example.nearbite;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RestaurantDetailsActivity
        extends AppCompatActivity {

    ImageView detailImage;
    TextView detailName, detailRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailRating = findViewById(R.id.detailRating);

        String name =
                getIntent().getStringExtra("name");

        double rating =
                getIntent().getDoubleExtra("rating", 0);

        String image =
                getIntent().getStringExtra("image");

        detailName.setText(name);

        detailRating.setText(
                "⭐ Rating: " + rating);

        Glide.with(this)
                .load(image)
                .into(detailImage);
    }
}