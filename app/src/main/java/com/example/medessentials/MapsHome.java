package com.example.medessentials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsHome extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener {
    public static final String TAG = "MapsHome";

    DatabaseReference myRef;

    private MapView mMapView;
    private Button logout;
    private Button request;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_home);

        initGoogleMap(savedInstanceState);
        myRef = FirebaseDatabase.getInstance().getReference().child("Offer");
        myRef.keepSynced(true);

        logout = findViewById(R.id.map_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        Intent onCreateIntent = getIntent();
        email = onCreateIntent.getStringExtra("Username");

        request = findViewById(R.id.request_button);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Request.class);
                intent.putExtra("Email", email);
                startActivity(intent);
            }
        });
    }

    public void done(View view) {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        ArrayList<Double> latitudes = new ArrayList<>();
        ArrayList<Double> longitudes = new ArrayList<>();
        latitudes.add(35.2493596);
        latitudes.add(34.2493596);
        longitudes.add(-118.3847136);
        longitudes.add(-118.3847136);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot MainSnapshot) {
                for(DataSnapshot datasnapShot: MainSnapshot.getChildren()){
                        System.out.println(datasnapShot.getValue());
                        HashMap<String, String> offer = (HashMap)datasnapShot.getValue();

                        String name = offer.get("companyName");
                        String product = offer.get("productName");
                        String type = offer.get("type");
                        String quantity = offer.get("quant");
                        String description = offer.get("descrip");
                        final String contact = offer.get("email");

                        double lat = Double.parseDouble(offer.get("latitude"));
                        double longi = Double.parseDouble(offer.get("longitude"));

                        Log.d(TAG, "Lat: " + lat + ", Long: " + longi);

                        String snipString = "Product: " + product + "\nType: " + type + "\nQuantity: " + quantity + "\nDescription: " + description + "\nContact: " + contact;
                        Marker marker = map.addMarker(new MarkerOptions().title(name).snippet(snipString).position(new LatLng(lat, longi)));

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                String[] emails_in_to = {contact};
                                intent.putExtra(Intent.EXTRA_REFERRER, "hello");
                                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Request");
                                intent.putExtra(Intent.EXTRA_TEXT, "I would like your stuff.\n\n If you like to contact me any other way, please email " + email);
                                intent.setType("text/html");
                                intent.setPackage("com.google.android.gm");
                                startActivity(intent);
                            }
                        });
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Data Access Failed" + databaseError.getMessage());
            }
        });

        InfoWndowAdapter markerInfoWindowAdapter = new InfoWndowAdapter(getApplicationContext());
        map.setInfoWindowAdapter(markerInfoWindowAdapter);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitudes.get(0), longitudes.get(0))));



        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), Authentication.class);
                        startActivity(intent);
                    }
                });
    }
}
