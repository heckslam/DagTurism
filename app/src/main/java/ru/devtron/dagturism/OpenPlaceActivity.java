package ru.devtron.dagturism;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.adapter.ImageGaleryRecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;
import ru.devtron.dagturism.model.ModelPlaceLatLng;

public class OpenPlaceActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_open_place;

    GoogleMap map;
    SupportMapFragment mapFragment;
    private double lat, lng;
    private Toolbar toolbar;
    private ModelPlace parcelWithPlace;
    private ModelPlaceLatLng modelPlaceLatLng;
    private TextView cityTitle, textVolleyError, descriptionTV;
    private String city, title, id, description;
    private List<String> arrayImages = new ArrayList<>();
    protected List<ModelPlaceLatLng> placeData = new ArrayList<>();
    SharedPreferences sp;

    private static final String STATE_OPEN_PLACE = "state_open_place";


    private RequestQueue queue;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEM = "item";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_DESC = "place_desc";

    private int success = 0;

    ViewPager viewPager;
    ImageGaleryRecyclerAdapter adapterImages;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_OPEN_PLACE, (ArrayList<? extends Parcelable>) placeData);
        Log.d("data", placeData.get(0).getDescription());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
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
        setContentView(LAYOUT);


        textVolleyError = (TextView) findViewById(R.id.textVolleyError);
        descriptionTV = (TextView) findViewById(R.id.descriptionPlace);
        viewPager = (ViewPager) findViewById(R.id.viewPagerForImages);
        cityTitle = (TextView) findViewById(R.id.cityTitle);

        getPlaceFromActivity();

        if (savedInstanceState!=null) {
            placeData = savedInstanceState.getParcelableArrayList(STATE_OPEN_PLACE);
            if (placeData != null && placeData.get(0).getDescription().length() > 3) {
                initVariables(placeData.get(0));
            }
            else {
                updateItem();
            }
        }
        else {
            updateItem();
        }


        initToolbar();

    }

    private void getPlaceFromActivity() {
        parcelWithPlace = getIntent().getParcelableExtra(ModelPlace.class.getCanonicalName());

        title = parcelWithPlace.getTitle();
        city = parcelWithPlace.getCity();
        id = parcelWithPlace.getId();
        arrayImages = parcelWithPlace.getImages();

        cityTitle.setText(city);

        adapterImages = new ImageGaleryRecyclerAdapter(this, arrayImages);

        viewPager.setAdapter(adapterImages);
        viewPager.setCurrentItem(ImageGaleryRecyclerAdapter.PAGER_PAGES_MIDDLE);
    }

    private ModelPlaceLatLng initVariables(ModelPlaceLatLng modelPlace) {
        if (modelPlace != null) {
            description = modelPlace.getDescription();
            lat = modelPlace.getLat();
            lng = modelPlace.getLng();
        }

        else
            updateItem();

        initUI(description, lat, lng);

        return modelPlace;
    }

    private void initUI(String description, double lat, double lng) {
        descriptionTV.setText(description);
        initMap(lat, lng);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void initMap(double lat, double lng) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng))
                    .zoom(13)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker)));
            map.getUiSettings().setAllGesturesEnabled(false);
        }

    }

    private void updateItem () {

        String getItemsUrl = "http://republic.tk/api/place/";

        getItemsUrl = getItemsUrl + id;

        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        modelPlaceLatLng = new ModelPlaceLatLng();

                        JSONObject place = response.getJSONObject(TAG_ITEM);
                        lat = place.getDouble(TAG_LAT);
                        lng = place.getDouble(TAG_LNG);
                        description = place.getString(TAG_DESC);

                        modelPlaceLatLng.setLat(lat);
                        modelPlaceLatLng.setLng(lng);
                        modelPlaceLatLng.setDescription(description);

                        placeData.clear();
                        placeData.add(modelPlaceLatLng);
                        initVariables(placeData.get(0));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textVolleyError.setVisibility(View.VISIBLE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    textVolleyError.setText(R.string.error_timeout);
                } else if (error instanceof AuthFailureError) {
                    textVolleyError.setText(R.string.error_auth);
                } else if (error instanceof ServerError) {
                    textVolleyError.setText(R.string.error_server);
                } else if (error instanceof NetworkError) {
                    textVolleyError.setText(R.string.no_network);
                } else if (error instanceof ParseError) {
                    textVolleyError.setText(R.string.error_server);
                }
            }
        });

        queue.add(jsonObjectRequest);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
}
