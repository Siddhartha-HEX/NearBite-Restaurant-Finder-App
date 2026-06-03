package com.example.nearbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.widget.SearchView;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private RecyclerView recyclerView;
    private List<Restaurant> restaurantList;
    private RestaurantAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        searchView = findViewById(R.id.searchView);

        restaurantList = new ArrayList<>();

        adapter = new RestaurantAdapter(restaurantList);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        List<Restaurant> filteredList =
                                new ArrayList<>();

                        for (Restaurant restaurant : restaurantList) {

                            if (restaurant.getName()
                                    .toLowerCase()
                                    .contains(newText.toLowerCase())) {

                                filteredList.add(restaurant);
                            }
                        }

                        adapter = new RestaurantAdapter(filteredList);

                        recyclerView.setAdapter(adapter);

                        return true;
                    }
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        double lat = location.getLatitude();
                        double lng = location.getLongitude();

                        LatLng currentLocation =
                                new LatLng(lat, lng);

                        mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        currentLocation,
                                        15
                                ));

                        getNearbyRestaurants(lat, lng);
                    }
                });
    }

    private void getNearbyRestaurants(double lat, double lng) {

        String apiKey = "AIzaSyAz-MLbKbOO_dv7-jYSMquTsr5euTcUSzU";

        String url =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                        + "?location=" + lat + "," + lng
                        + "&radius=3000"
                        + "&type=restaurant"
                        + "&key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            try {

                                JSONArray results =
                                        response.getJSONArray("results");

                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject restaurant =
                                            results.getJSONObject(i);

                                    JSONObject geometry =
                                            restaurant.getJSONObject("geometry");

                                    JSONObject location =
                                            geometry.getJSONObject("location");

                                    double rLat =
                                            location.getDouble("lat");

                                    double rLng =
                                            location.getDouble("lng");

                                    String name =
                                            restaurant.getString("name");

                                    double rating = 0;

                                    if (restaurant.has("rating")) {
                                        rating =
                                                restaurant.getDouble("rating");
                                    }

                                    String imageUrl =
                                            "https://picsum.photos/400/300?random=" + i;

                                    restaurantList.add(
                                            new Restaurant(name, rating, imageUrl));

                                    adapter.notifyDataSetChanged();

                                    LatLng restaurantLocation =
                                            new LatLng(rLat, rLng);

                                    mMap.addMarker(
                                            new MarkerOptions()
                                                    .position(restaurantLocation)
                                                    .title(name)
                                    );
                                }

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        },

                        error -> Toast.makeText(
                                this,
                                error.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show()
                );

        queue.add(request);
    }
}