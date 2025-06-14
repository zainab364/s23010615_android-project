package com.s23010615.zainab;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class  mapActivity extends AppCompatActivity implements OnMapReadyCallback {

    com.google.android.gms.maps.GoogleMap mMap;
    EditText addressInput;
    Button showLocationButton;
    View text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        addressInput = findViewById(R.id.addressInput);
        showLocationButton = findViewById(R.id.showLocationButton);
        text = findViewById(R.id.textViewFeelMusic);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        showLocationButton.setOnClickListener(v -> {
            String address = addressInput.getText().toString();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
                return;
            }

            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> list = geocoder.getFromLocationName(address, 1);
                if (list != null && !list.isEmpty()) {
                    Address location = list.get(0);
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Geocoding failed", Toast.LENGTH_SHORT).show();
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mapActivity.this, SensorActivity.class));

            }
        });
    }

    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
        mMap = googleMap;
    }
}

