package ru.devtron.dagturism;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.adapter.RecyclerGalleryAdapter;
import ru.devtron.dagturism.customview.ExpandableTextView;
import ru.devtron.dagturism.db.DBHelper;
import ru.devtron.dagturism.model.ModelPlace;
import ru.devtron.dagturism.model.WaylineModel;

public class OpenPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LAYOUT = R.layout.activity_open_place;

    SupportMapFragment mapFragment;
    private double lat, lng;
    private ModelPlace modelPlace;
    private boolean hasRoute;
    private TextView textVolleyError;
    private WaylineModel waylineModel;
    ExpandableTextView descriptionTV;
    private Button howToGo;
    private CardView howToGoCard;
    private String title, id, description, city;
    private int idInt;
    SharedPreferences sp;
    NestedScrollView nestedScrollView;
    private SQLiteDatabase database;
    List<String> arrayImages;
    private double pointLat, pointLng;
    private String pointCaption;
    private int pointNumber;
    private List<WaylineModel> mDataList = new ArrayList<>();
    private int finalPrice;


    private static final String STATE_OPEN_PLACE = "state_open_place";


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ITEM = "item";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LNG = "longitude";
    private static final String TAG_DESC = "place_desc";
    private static final String TAG_PRICE = "price";
    private static final String TAG_POINTS = "points";
    private static final String TAG_CAPTION = "point_caption";
    private static final String TAG_POINT_LAT = "point_latitude";
    private static final String TAG_POINT_LNG = "point_longitude";
    private static final String TAG_POINT_NUMBER = "point_number";

    private int success = 0;

    ViewPager viewPager;
    RecyclerGalleryAdapter adapterImages;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_OPEN_PLACE, modelPlace);
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


        DBHelper dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        getPlaceFromActivity();

        String selectQuery = "SELECT " + DBHelper.KEY_DESCRIPTION + ", "
                + DBHelper.KEY_LAT + ", " + DBHelper.KEY_LNG +
                " FROM " + DBHelper.TABLE_PLACES + " WHERE "
                + DBHelper.KEY_PLACE_ID + " = " + id;

        //Сначала смотрим есть ли место в бд
        Cursor c = database.rawQuery(selectQuery, null);
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            initToolbar(true);
            String desc = c.getString(c.getColumnIndex(DBHelper.KEY_DESCRIPTION));
            double lat = Double.parseDouble(c.getString(c.getColumnIndex(DBHelper.KEY_LAT)));
            double lng = Double.parseDouble(c.getString(c.getColumnIndex(DBHelper.KEY_LNG)));

            ModelPlace place = new ModelPlace();
            place.setDescription(desc);
            place.setLat(lat);
            place.setLng(lng);

            initVariables(place);

            String selectPointsQuery = "SELECT  * FROM " + DBHelper.TABLE_POINTS + " WHERE "
                    + DBHelper.KEY_PLACE_ID + " = " + id;

            Cursor cursorPoints = database.rawQuery(selectPointsQuery, null);
            if (cursorPoints.moveToFirst()) {
                howToGoCard.setVisibility(View.VISIBLE);
                do {
                    String caption = cursorPoints.getString((cursorPoints.getColumnIndex(DBHelper.KEY_POINT_CAPTION)));
                    int pointNumber = cursorPoints.getInt((cursorPoints.getColumnIndex(DBHelper.KEY_POINT_NUMBER)));
                    double pointLng = cursorPoints.getDouble((cursorPoints.getColumnIndex(DBHelper.KEY_POINT_LAT)));
                    double pointLat = cursorPoints.getDouble((cursorPoints.getColumnIndex(DBHelper.KEY_POINT_LNG)));

                    WaylineModel model = new WaylineModel();
                    model.setPointCaption(caption);
                    model.setPointLat(pointLat);
                    model.setPointLng(pointLng);
                    model.setPointNumber(pointNumber);
                    finalPrice = cursorPoints.getInt((cursorPoints.getColumnIndex(DBHelper.KEY_POINTS_PRICE)));

                    mDataList.add(model);
                }
                while (cursorPoints.moveToNext());
            }
            cursorPoints.close();

            c.close();
        }
        //иначе если в базе нету, тогда смотрим в savedInstanceState
        else if (savedInstanceState!=null) {
            initToolbar(false);
            modelPlace = savedInstanceState.getParcelable(STATE_OPEN_PLACE);
            if (modelPlace != null && modelPlace.getDescription().length() > 3) {
                initVariables(modelPlace);
            }
            else {
                updateItem();
            }
        }
        //Если и в savedInstanceState пусто, тогда делаем запрос на сервер
        else {
            initToolbar(false);
            updateItem();
        }
    }

    private void getPlaceFromActivity() {
        ModelPlace parcelWithPlace = getIntent().getParcelableExtra(ModelPlace.class.getCanonicalName());

        if (parcelWithPlace != null) {
            title = parcelWithPlace.getTitle();
            city = parcelWithPlace.getCity();
            id = parcelWithPlace.getId();
            arrayImages = parcelWithPlace.getImages();
        }

        adapterImages = new RecyclerGalleryAdapter(this, arrayImages);

        viewPager.setAdapter(adapterImages);
        viewPager.setCurrentItem(RecyclerGalleryAdapter.PAGER_PAGES_MIDDLE);

        howToGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenPlaceActivity.this, SprintLineActivity.class);
                intent.putParcelableArrayListExtra("mDataList", (ArrayList<? extends Parcelable>) mDataList);
                intent.putExtra("finalPrice", finalPrice);
                startActivity(intent);
            }
        });
    }

    private ModelPlace initVariables(ModelPlace modelPlace) {
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


    private void initToolbar(boolean hasDb) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);


            MaterialFavoriteButton toolbarFavorite = new MaterialFavoriteButton.Builder(this)
                    .favorite(hasDb)
                    .color(MaterialFavoriteButton.STYLE_WHITE)
                    .type(MaterialFavoriteButton.STYLE_HEART)
                    .rotationDuration(400)
                    .create();

            LinearLayout layoutButton = (LinearLayout) findViewById(R.id.linearButton);
            layoutButton.addView(toolbarFavorite);
            toolbarFavorite.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite) {
                                ContentValues contentValuesPlace = new ContentValues();
                                contentValuesPlace.put(DBHelper.KEY_PLACE_ID, id);
                                contentValuesPlace.put(DBHelper.KEY_TITLE, title);
                                contentValuesPlace.put(DBHelper.KEY_CITY, city);
                                contentValuesPlace.put(DBHelper.KEY_LAT, modelPlace.getLat());
                                contentValuesPlace.put(DBHelper.KEY_LNG, modelPlace.getLng());
                                contentValuesPlace.put(DBHelper.KEY_DESCRIPTION, modelPlace.getDescription());

                                database.insert(DBHelper.TABLE_PLACES, null, contentValuesPlace);

                                ContentValues contentValuesImages = new ContentValues();

                                for (int i = 0; i < arrayImages.size(); i++) {
                                    contentValuesImages.put(DBHelper.KEY_PLACE_ID, id);
                                    contentValuesImages.put(DBHelper.KEY_IMAGE_URL, arrayImages.get(i));
                                    database.insert(DBHelper.TABLE_IMAGES, null, contentValuesImages);
                                }

                                if (hasRoute) {
                                    ContentValues contentValuesPoints = new ContentValues();
                                    howToGoCard.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < mDataList.size(); i++) {
                                        contentValuesPoints.put(DBHelper.KEY_PLACE_ID, id);
                                        contentValuesPoints.put(DBHelper.KEY_POINT_CAPTION, mDataList.get(i).getPointCaption());
                                        contentValuesPoints.put(DBHelper.KEY_POINT_NUMBER, mDataList.get(i).getPointNumber());
                                        contentValuesPoints.put(DBHelper.KEY_POINT_LAT, mDataList.get(i).getPointLat());
                                        contentValuesPoints.put(DBHelper.KEY_POINT_LNG, mDataList.get(i).getPointLng());
                                        contentValuesPoints.put(DBHelper.KEY_POINTS_PRICE, finalPrice);
                                        database.insert(DBHelper.TABLE_POINTS, null, contentValuesPoints);
                                    }

                                }

                                Cursor cursor = database.query(DBHelper.TABLE_IMAGES, null, null, null, null, null, null);
                                if (cursor.getCount() > 0) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Добавлено в избранные", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                cursor.close();
                            }

                            else {
                                database.delete(DBHelper.TABLE_PLACES, DBHelper.KEY_PLACE_ID + " = ? ", new String[] { id });
                                database.delete(DBHelper.TABLE_IMAGES, DBHelper.KEY_PLACE_ID + " = ? ", new String[] { id });

                                Cursor cursorPoints = database.query(DBHelper.TABLE_POINTS, null, null, null, null, null, null);
                                if (cursorPoints.getCount() > 0) {
                                    database.delete(DBHelper.TABLE_POINTS, DBHelper.KEY_PLACE_ID + " = ? ", new String[]{id});
                                }

                                cursorPoints.close();
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Удалено из избранных", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            }
                        }

                        );
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

                        modelPlace = new ModelPlace();

                        JSONObject place = response.getJSONObject(TAG_ITEM);
                        JSONArray points = place.getJSONArray(TAG_POINTS);
                        finalPrice = place.getInt(TAG_PRICE);
                        if (finalPrice > 0) {
                            howToGoCard.setVisibility(View.VISIBLE);
                            hasRoute = true;
                            for (int i = points.length() - 1; i >= 0; i--) {
                                JSONObject currentPoint = points.getJSONObject(i);
                                waylineModel = new WaylineModel();

                                pointLat = currentPoint.getDouble(TAG_POINT_LAT);
                                pointLng = currentPoint.getDouble(TAG_POINT_LNG);
                                pointCaption = currentPoint.getString(TAG_CAPTION);
                                pointNumber = currentPoint.getInt(TAG_POINT_NUMBER);

                                waylineModel.setPointLat(pointLat);
                                waylineModel.setPointLng(pointLng);
                                waylineModel.setPointCaption(pointCaption);
                                waylineModel.setPointNumber(pointNumber);


                                mDataList.add(waylineModel);
                            }
                        }

                        lat = place.getDouble(TAG_LAT);
                        lng = place.getDouble(TAG_LNG);
                        description = place.getString(TAG_DESC);

                        modelPlace.setLat(lat);
                        modelPlace.setLng(lng);
                        modelPlace.setDescription(description);

                        initVariables(modelPlace);
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
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(OpenPlaceActivity.this, OpenMapActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("title", title);
                startActivity(intent);
                return false;
            }
        });
    }
}
