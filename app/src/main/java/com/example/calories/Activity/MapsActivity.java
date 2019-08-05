package com.example.calories.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.calories.Interface.DirectionsApiInterface;
import com.example.calories.Model.Maps.Maps;
import com.example.calories.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.calories.Config.Config.DIRECTIONS_KEY;
import static com.example.calories.Config.Config.MAPS_URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int REQUEST_CODE = 101;

    private DirectionsApiInterface mapsApiInterface;

    String lat, lon;
    String lattitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restoran_maps);

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(MAPS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mapsApiInterface = retrofit.create(DirectionsApiInterface.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle bundleGet = getIntent().getExtras();

        lat = bundleGet.getString("lat");
        lon = bundleGet.getString("lon");
        lattitude = bundleGet.getString("lattitude");
        longitude = bundleGet.getString("longitude");

        Log.i("Hai", lattitude + ", " + longitude);

        getDirection();
    }

    private void getDirection() {
        Call<Maps> call = mapsApiInterface.getMaps(
                lattitude + "," + longitude,
                lat + "," + lon,
                DIRECTIONS_KEY
        );

        call.enqueue(new Callback<Maps>() {
            @Override
            public void onResponse(Call<Maps> call, Response<Maps> response) {
                if (!response.isSuccessful()) {
                    Log.i("Code", String.valueOf(response.code()));
                    return;
                }

                Maps maps = response.body();

                Log.i("Body Directions", maps.getStatus());
            }

            @Override
            public void onFailure(Call<Maps> call, Throwable t) {
                Log.i("Error", t.getMessage());
            }
        });
    }

    private void getLocation() {
        if (
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                            + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(Double.valueOf(lattitude), Double.valueOf(longitude));
        LatLng latLng1 = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("Your location");
        MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1)
                .title("Restaurant location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
        googleMap.addMarker(markerOptions1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                break;
        }
    }
}
