package ru.devtron.dagturism;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OpenMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private double lat, lng, myLat, myLng;
    SupportMapFragment mapFragment;
    String title;
    private com.melnykov.fab.FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTheme = sp.getString("selectedTheme", "1");
        int selectedThemeValue = Integer.parseInt(selectedTheme);
        switch (selectedThemeValue) {
            case 1:
                setTheme(R.style.AppDefault);
                break;
            case 2:
                setTheme(R.style.AppOrange);
                break;
            case 3:
                setTheme(R.style.AppPurple);
                break;
            case 4:
                setTheme(R.style.AppGrey);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_map);

        initToolbar();

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        initMap();


        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLocation(location);
            }


        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
        }

        Toast.makeText(getApplicationContext(),
                R.string.clickMarker,
                Toast.LENGTH_LONG).show();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            title = getIntent().getStringExtra("title");
            toolbar.setTitle(title);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void setLocation(Location location) {
        myLat = location.getLatitude();
        myLng = location.getLongitude();
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            final LatLng placeLatLng = new LatLng(lat, lng);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(placeLatLng)
                    .zoom(15)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker)));
            UiSettings settings = map.getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setRotateGesturesEnabled(false);
            settings.setMyLocationButtonEnabled(true);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng myLatLng = new LatLng(myLat, myLng);
                    map.addMarker(new MarkerOptions().position(myLatLng));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(placeLatLng, 13);
                    map.animateCamera(cameraUpdate);
                }
            });


        }
    }

}
