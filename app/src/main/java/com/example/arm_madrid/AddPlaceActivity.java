package com.example.arm_madrid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlaceActivity extends AppCompatActivity {

    private class LocListener implements LocationListener{

        private double lat, lon;

        @Override
        public void onLocationChanged(@NonNull Location location) {

            lat = location.getLatitude();
            lon = location.getLongitude();

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

    }

    private boolean isEditing;
    private LocListener listener;
    private LocationManager locationManager;
    private double prevLat, prevLon;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_place);

        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        listener = new LocListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, listener);

        Intent intent = getIntent();

        isEditing = intent.getBooleanExtra("isEditing", false);

        if (isEditing){

            EditText place = findViewById(R.id.nombre);
            EditText comentario = findViewById(R.id.comentario);

            place.setText(intent.getStringExtra("place"));
            comentario.setText(intent.getStringExtra("comment"));
            prevLat = intent.getDoubleExtra("lat",0);
            prevLon = intent.getDoubleExtra("lon",0);
            id = intent.getIntExtra("id",0);

        }

    }
}