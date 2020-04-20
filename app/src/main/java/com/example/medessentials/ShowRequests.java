package com.example.medessentials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowRequests extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Show_Requests";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private MapView mapView;

    private DatabaseReference requestRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);

         requestRef = FirebaseDatabase.getInstance().getReference("Request");

         initGoogleMap(savedInstanceState);

    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.show_req_map);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);
    }

    public void done(View view) {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        latitudes.add(35.2493596);
        latitudes.add(34.2493596);
        longitudes.add(-118.3847136);
        longitudes.add(-118.3847136);

        requestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, String> request = (HashMap) snapshot.getValue();

                    final String user = request.get("user");
                    String type = request.get("type");
                    String quantity = request.get("quantity");
                    String description = request.get("description");
                    String latitude = request.get("latitude");
                    String longitude = request.get("longitude");

                    double lat = Double.parseDouble(latitude);
                    double longi = Double.parseDouble(longitude);

                    Log.d(TAG, lat + " " + longi);

                    String snipString = "Product: " + type + "\nQuantity: " + quantity + "\nDescription: " + description + "\nRequested by: " + user;
                    Marker mark = googleMap.addMarker(new MarkerOptions().title("Hello").snippet(snipString).position(new LatLng(lat, longi)));

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String[] emails_in_to = {user};
                            intent.putExtra(Intent.EXTRA_REFERRER, "hello");
                            intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Request");
                            intent.putExtra(Intent.EXTRA_TEXT, "I would like your stuff.\n\n If you like to contact me any other way, please email " + user);
                            intent.setType("text/html");
                            intent.setPackage("com.google.android.gm");
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Database Error: " + databaseError.getMessage());
            }
        });
        InfoWndowAdapter markerInfoWindowAdapter = new InfoWndowAdapter(getApplicationContext());
        googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(34.0, -115.0)));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }
}
