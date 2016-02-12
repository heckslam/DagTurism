package ru.devtron.dagturism.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends AbstractTabFragment
        implements
        GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback {

    private double lat, lng;
    SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private Context context;
    protected View view;

    final static String KEY_PLACES_ARRAY = "listPlaces";

    public static MapFragment getInstance(Context context, List<ModelPlace> list){
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_PLACES_ARRAY, (ArrayList<? extends Parcelable>) list);
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
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);


        mapFragment = (SupportMapFragment)  this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapFragment.this);


        Bundle bundle = getArguments();
        if (bundle != null) {
            listPlaces = bundle.getParcelableArrayList(KEY_PLACES_ARRAY);
            if (listPlaces != null) {
                Log.d("huetaMapBundle", String.valueOf(listPlaces.size()));
            }
        }


        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            UiSettings settings = map.getUiSettings();

            settings.setAllGesturesEnabled(true);
            settings.setRotateGesturesEnabled(false);
            for (ModelPlace place : listPlaces) {
                builder.include(new LatLng(place.getLat(), place.getLng()));
                map.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker)));
            }
            LatLngBounds bounds = builder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listmenu:
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.replace(R.id.root_frame, PopularFragment.getInstance(context, listPlaces));
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.menumap, false);
        super.onPrepareOptionsMenu(menu);
    }
}
