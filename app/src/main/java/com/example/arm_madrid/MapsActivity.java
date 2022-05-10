package com.example.arm_madrid;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.arm_madrid.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    LatLng sydney;

    private boolean isEditing, putForm;
    private String name;
    private double lat, lon;
    private double prevLat, prevLon;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.name = intent.getStringExtra("name");
        this.lat = intent.getDoubleExtra("lat",0);
        this.lon = intent.getDoubleExtra("lon",0);

        putForm = intent.getBooleanExtra("putForm", false);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        if(putForm){
            setContentView(R.layout.activity_add_place);
        }else{
            setContentView(binding.getRoot());

        }


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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getDeviceLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(putForm ? R.id.map2 : R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(isEditing ? prevLat : lat, isEditing ? prevLon : lon);
        //mMap.addMarker(new MarkerOptions().position(sydney).title(name).draggable(true));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng sydney = new LatLng(0,0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("New Place"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            lat = lastKnownLocation.getLatitude();
                            lon = lastKnownLocation.getLongitude();

                            LatLng sydney = new LatLng(lat,lon);
                            mMap.addMarker(new MarkerOptions().position(sydney).title(name).draggable(true));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,(float)15)); // 2.0 a 21.0
                        }
                    } else {
                        Log.d("", "Current location is null. Using defaults.");
                        Log.e("", "Exception: %s", task.getException());;
                    }
                }
            });
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void saveDataMaps(View view){

        SQLiteManager sqLiteManager = new SQLiteManager(this);

        EditText place = findViewById(R.id.nombre);
        EditText comentario = findViewById(R.id.comentario);

        if(place.getText().toString().isEmpty() || comentario.getText().toString().isEmpty()){
            Toast.makeText(this, "Todos los campos deben estar rellenos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isEditing){

            sqLiteManager.editPlace(id,place.getText().toString(), comentario.getText().toString(),prevLat,prevLon);

        }else{

            sqLiteManager.saveData(place.getText().toString(), comentario.getText().toString(), lat, lon);

        }

        Toast.makeText(this, "Información " + (isEditing ? "actualizada" : "guardada") +" correctamente.", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}