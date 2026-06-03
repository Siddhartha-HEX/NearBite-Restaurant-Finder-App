package com.example.nearbite;

public class Restaurant {

    String name;
    double rating;
    String imageUrl;

    public Restaurant(String name,
                      double rating,
                      String imageUrl) {

        this.name = name;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}