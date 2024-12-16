package com.android.locationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView latitudeTextView;
    private TextView longitudeTextView;

    private GoogleMap googleMap;

    private Handler handler = new Handler();
    private final int LOCATION_UPDATE_INTERVAL_MINUTES = 10;

    private Runnable locationUpdaterRunnable = new Runnable() {
        @Override
        public void run() {
            startLocationUpdates();
            handler.postDelayed(this, LOCATION_UPDATE_INTERVAL_MINUTES * 60 * 1000); // Convert minutes to milliseconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // TextViews for displaying latitude and longitude
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Check and request permissions if necessary
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        // Fetch the last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Display the latitude and longitude
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            latitudeTextView.setText("Latitude: " + latitude);
                            longitudeTextView.setText("Longitude: " + longitude);

                            // Update the map
                            if (googleMap != null) {
                                LatLng userLocation = new LatLng(latitude, longitude);
                                googleMap.clear(); // Clear any previous markers
                                googleMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Check if location permission is granted, and enable the user's location on the map
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start periodic location updates
        handler.post(locationUpdaterRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop periodic location updates when the activity is paused
        handler.removeCallbacks(locationUpdaterRunnable);
    }
}
