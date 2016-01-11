package ru.devtron.dagturism;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.devtron.dagturism.adapter.RecyclerGalleryAdapter;
import ru.devtron.dagturism.customview.ExpandableTextView;
import ru.devtron.dagturism.model.ModelPlace;
import ru.devtron.dagturism.model.ModelPlaceLatLng;

public class OpenPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LAYOUT = R.layout.activity_open_place;

    SupportMapFragment mapFragment;
    private double lat, lng;
    private ModelPlaceLatLng modelPlaceLatLng;
    private TextView textVolleyError;
    ExpandableTextView descriptionTV;
    private Button howToGo;
    private CardView howToGoCard;
    private String title, id, description;
    private int idInt;
    SharedPreferences sp;
    NestedScrollView nestedScrollView;

    private static final String STATE_OPEN_PLACE = "state_open_place";


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEM = "item";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_DESC = "place_desc";
    private static final String TAG_PRICE = "price";

    private int success = 0;

    ViewPager viewPager;
    RecyclerGalleryAdapter adapterImages;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_OPEN_PLACE, modelPlaceLatLng);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTheme = sp.getString("selectedTheme", "1");
        int selectedThemeValue = Integer.parseInt(selectedTheme);
        switch (selectedThemeValue) {
            case 1:
                setTheme(R.style.AppDefaultTransparent);
                break;
            case 2:
                setTheme(R.style.AppOrangeTransparent);
                break;
            case 3:
                setTheme(R.style.AppPurpleTransparent);
                break;
            case 4:
                setTheme(R.style.AppGreyTransparent);
                break;
        }
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        textVolleyError = (TextView) findViewById(R.id.textVolleyError);
        descriptionTV = (ExpandableTextView) findViewById(R.id.descriptionPlace);
        viewPager = (ViewPager) findViewById(R.id.viewPagerForImages);
        howToGo = (Button) findViewById(R.id.howToGo);
        howToGoCard = (CardView) findViewById(R.id.howToGoCard);
        nestedScrollView =  (NestedScrollView) findViewById(R.id.nestedScroll);

        getPlaceFromActivity();



        if (savedInstanceState!=null) {
            modelPlaceLatLng = savedInstanceState.getParcelable(STATE_OPEN_PLACE);
            if (modelPlaceLatLng != null && modelPlaceLatLng.getDescription().length() > 3) {
                initVariables(modelPlaceLatLng);
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
        ModelPlace parcelWithPlace = getIntent().getParcelableExtra(ModelPlace.class.getCanonicalName());

        title = parcelWithPlace.getTitle();
        id = parcelWithPlace.getId();
        List<String> arrayImages = parcelWithPlace.getImages();

        adapterImages = new RecyclerGalleryAdapter(this, arrayImages);

        viewPager.setAdapter(adapterImages);
        viewPager.setCurrentItem(RecyclerGalleryAdapter.PAGER_PAGES_MIDDLE);

        howToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenPlaceActivity.this, SprintLineActivity.class);
                idInt = Integer.parseInt(id);
                intent.putExtra("id", idInt);
                startActivity(intent);
            }
        });
    }

    private ModelPlaceLatLng initVariables(ModelPlaceLatLng modelPlace) {
        if (modelPlace != null) {
            description = modelPlace.getDescription();
            lat = modelPlace.getLat();
            lng = modelPlace.getLng();
        }

        else
            updateItem();

        initUI(description);

        return modelPlace;
    }

    private void initUI(String description) {
        descriptionTV.setText(description);
        initMap();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    private void updateItem () {
        nestedScrollView.setVisibility(View.GONE);
        String getItemsUrl = "http://republic.tk/api/place/";

        getItemsUrl = getItemsUrl + id;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {

                        modelPlaceLatLng = new ModelPlaceLatLng();

                        JSONObject place = response.getJSONObject(TAG_ITEM);
                        if (place.getInt(TAG_PRICE) > 0) {
                            howToGoCard.setVisibility(View.VISIBLE);
                        }

                        lat = place.getDouble(TAG_LAT);
                        lng = place.getDouble(TAG_LNG);
                        description = place.getString(TAG_DESC);

                        modelPlaceLatLng.setLat(lat);
                        modelPlaceLatLng.setLng(lng);
                        modelPlaceLatLng.setDescription(description);

                        initVariables(modelPlaceLatLng);
                    }

                    nestedScrollView.setVisibility(View.VISIBLE);
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

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(13)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker)));
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(OpenPlaceActivity.this, OpenMapActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }
}
