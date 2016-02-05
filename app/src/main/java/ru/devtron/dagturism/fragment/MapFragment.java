package ru.devtron.dagturism.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
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
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment
        implements
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback {

    private double lat, lng;
    SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    FragmentManager fragmentManager;
    private Context context;
    protected View view;
    private List<ModelPlace> modelPlaces = new ArrayList<>();

    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";
    final static String TAG_3 = "FRAGMENT_3";
    final static String KEY_PLACES_ARRAY = "PLACES_ARRAY";
    final static String KEY_MSG_2 = "FRAGMENT2_MSG";
    final static String KEY_MSG_3 = "FRAGMENT3_MSG";

    public static MapFragment getInstance(Context context){
        Bundle args = new Bundle();
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(args);
        mapFragment.setContext(context);
        return mapFragment;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        fragmentManager = getFragmentManager();

        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            modelPlaces = bundle.getParcelableArrayList(KEY_PLACES_ARRAY);
        }


        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build();

        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            final LatLng placeLatLng = new LatLng(lat, lng);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(placeLatLng)
                    .zoom(15)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);
            UiSettings settings = map.getUiSettings();

            settings.setAllGesturesEnabled(true);
            settings.setRotateGesturesEnabled(false);


        }
    }
}
