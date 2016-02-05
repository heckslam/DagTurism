package ru.devtron.dagturism;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.devtron.dagturism.Utils.Constants;
import ru.devtron.dagturism.adapter.RecyclerAdapterEatSleep;
import ru.devtron.dagturism.adapter.RecyclerGalleryAdapter;
import ru.devtron.dagturism.customview.ExpandableTextView;
import ru.devtron.dagturism.db.DBHelper;
import ru.devtron.dagturism.listener.ClickListener;
import ru.devtron.dagturism.listener.RecyclerClickListener;
import ru.devtron.dagturism.model.ModelNearPlace;
import ru.devtron.dagturism.model.ModelPlace;
import ru.devtron.dagturism.model.WaylineModel;

public class OpenPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LAYOUT = R.layout.activity_open_place;

    private double lat, lng;
    private ModelPlace modelPlace;
    public boolean hasRoute;
    private TextView textVolleyError;
    private Button howToGo;
    private CardView howToGoCard;
    private String title, id, description, city;
    SharedPreferences sp;
    NestedScrollView nestedScrollView;
    private SQLiteDatabase database;
    List<String> arrayImages = new ArrayList<>();
    List<String> arrayImagesFrom1 = new ArrayList<>();
    private double pointLat, pointLng;
    private String pointCaption;
    private int pointNumber;
    public static List<WaylineModel> mDataList = new ArrayList<>();
    private int finalPrice;
    private List<ModelNearPlace> modelNearPlaces = new ArrayList<>();
    private boolean hasImages;
    LinearLayout linearLayout;
    TextView nearTV;
    ViewPager viewPager;
    RecyclerGalleryAdapter adapterImages;
    List<ModelNearPlace> fiveLastNearCafe = new ArrayList<>();
    List<ModelNearPlace> fiveLastNearHotels = new ArrayList<>();

    RecyclerView cafeRV, hotelRV;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.STATE_OPEN_PLACE, modelPlace);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingTheme();
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        textVolleyError = (TextView) findViewById(R.id.textVolleyError);

        viewPager = (ViewPager) findViewById(R.id.viewPagerForImages);
        howToGo = (Button) findViewById(R.id.howToGo);
        howToGoCard = (CardView) findViewById(R.id.howToGoCard);
        nestedScrollView =  (NestedScrollView) findViewById(R.id.nestedScroll);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutRV);
        nearTV = (TextView) findViewById(R.id.near);
        cafeRV = (RecyclerView) findViewById(R.id.cafeRV);
        hotelRV = (RecyclerView) findViewById(R.id.hotelRV);
        cafeRV.setLayoutManager(new LinearLayoutManager(this));
        hotelRV.setLayoutManager(new LinearLayoutManager(this));


        if (modelPlace == null) {
            getPlaceFromActivity();
        }

        ModelPlace placeFromDB = getDataFromDB();
        if (placeFromDB != null) {
            initToolbar(true);
            initVariables(placeFromDB);
        }

        //иначе если в базе нету, тогда смотрим в savedInstanceState
        else if (savedInstanceState!=null) {
            initToolbar(false);
            modelPlace = savedInstanceState.getParcelable(Constants.STATE_OPEN_PLACE);
            if (modelPlace != null && modelPlace.getDescription().length() > 3) {
                initVariables(modelPlace);
            }
        }
        //Если и в savedInstanceState пусто, тогда делаем запрос на сервер при условии что мы перешли из списка мест
        else if (modelPlace == null && hasImages){
            initToolbar(false);
            updateItem();
        }
        // //Если и в savedInstanceState пусто, тогда делаем запрос на сервер ПОЛНЫЙ
        else {
            initToolbar(false);
            FullUpdateItem(id);
        }
    }



    private void getPlaceFromActivity() {
        ModelPlace parcelWithPlace = getIntent().getParcelableExtra(ModelPlace.class.getCanonicalName());

        if (parcelWithPlace != null) {
            title = parcelWithPlace.getTitle();
            city = parcelWithPlace.getCity();
            id = parcelWithPlace.getId();
            arrayImages = parcelWithPlace.getImages();
            hasImages = true;
            adapterImages = new RecyclerGalleryAdapter(this, arrayImages);
            viewPager.setAdapter(adapterImages);
            viewPager.setCurrentItem(RecyclerGalleryAdapter.PAGER_PAGES_MIDDLE);
            loadNearPlaces(true);
            loadNearPlaces(false);
        }

        else {
            id = getIntent().getStringExtra(ModelNearPlace.class.getCanonicalName());
            title = getIntent().getStringExtra("title");
            hasImages = false;
        }



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
        List<String> mist = new ArrayList<>();
        if (modelPlace != null) {
            description = modelPlace.getDescription();
            mist = modelPlace.getImages();
            lat = modelPlace.getLat();
            lng = modelPlace.getLng();
        }

        else
            updateItem();

        initUI(description, mist);

        return modelPlace;
    }

    private void initUI(String description, List<String> list) {
        if (!hasImages) {
            adapterImages = new RecyclerGalleryAdapter(this, list);
            viewPager.setAdapter(adapterImages);
            viewPager.setCurrentItem(RecyclerGalleryAdapter.PAGER_PAGES_MIDDLE);
        }
        ExpandableTextView descriptionTV = (ExpandableTextView) findViewById(R.id.descriptionPlace);
        descriptionTV.setText(description);
        initMap();
    }


    private void initToolbar(boolean hasDb) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);

            setFavoriteListener(hasDb);
        }
            if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }


    private void FullUpdateItem(final String ip) {
        nestedScrollView.setVisibility(View.GONE);
        String getItemsUrl = "http://republic.tk/api/place/";

        getItemsUrl = getItemsUrl + ip;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt(Constants.TAG_SUCCESS);
                    if (success == 1) {
                        modelPlace = new ModelPlace();
                        JSONObject place = response.getJSONObject(Constants.TAG_ITEM);
                        JSONArray images = place.getJSONArray(Constants.TAG_IMAGES);
                        for (int j = 0; j < images.length(); j++){
                            arrayImagesFrom1.add(images.getString(j));
                        }

                        title = place.getString(Constants.TAG_NAME);
                        city = place.getString(Constants.TAG_CITY);

                        lat = place.getDouble(Constants.TAG_LAT);
                        lng = place.getDouble(Constants.TAG_LNG);
                        description = place.getString(Constants.TAG_DESC);

                        modelPlace.setId(ip);
                        modelPlace.setTitle(title);
                        modelPlace.setCity(city);
                        modelPlace.setImages(arrayImagesFrom1);

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
                Constants.displayErrors(textVolleyError, error);
            }
        });

        queue.add(jsonObjectRequest);
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
                    int success = response.getInt(Constants.TAG_SUCCESS);
                    if (success == 1) {

                        modelPlace = new ModelPlace();

                        JSONObject place = response.getJSONObject(Constants.TAG_ITEM);
                        JSONArray points = place.getJSONArray(Constants.TAG_POINTS);
                        finalPrice = place.getInt(Constants.TAG_PRICE);
                        if (finalPrice > 0) {
                            howToGoCard.setVisibility(View.VISIBLE);
                            hasRoute = true;
                            for (int i = points.length() - 1; i >= 0; i--) {
                                JSONObject currentPoint = points.getJSONObject(i);
                                WaylineModel waylineModel = new WaylineModel();

                                pointLat = currentPoint.getDouble(Constants.TAG_POINT_LAT);
                                pointLng = currentPoint.getDouble(Constants.TAG_POINT_LNG);
                                pointCaption = currentPoint.getString(Constants.TAG_CAPTION);
                                pointNumber = currentPoint.getInt(Constants.TAG_POINT_NUMBER);

                                waylineModel.setPointLat(pointLat);
                                waylineModel.setPointLng(pointLng);
                                waylineModel.setPointCaption(pointCaption);
                                waylineModel.setPointNumber(pointNumber);


                                mDataList.add(waylineModel);
                            }
                        }

                        lat = place.getDouble(Constants.TAG_LAT);
                        lng = place.getDouble(Constants.TAG_LNG);
                        description = place.getString(Constants.TAG_DESC);

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
                Constants.displayErrors(textVolleyError, error);
            }
        });

        queue.add(jsonObjectRequest);

    }

    private void loadNearPlaces(final boolean bool) {
        String encodeCity = "";
        String any = "";

        try {
            if (city != null) {
                String[] splitedCity = city.split(" ");
                for (int i = 0; i < splitedCity.length; i++) {
                    if (i != splitedCity.length - 1){
                        encodeCity = encodeCity + URLEncoder.encode(splitedCity[i], "utf-8") + "%20";
                    }
                    else encodeCity = encodeCity + URLEncoder.encode(splitedCity[i], "utf-8");
                }
            }
            any = URLEncoder.encode("Любой", "utf-8");
        }
        catch (Exception e) {
            System.out.println("Tried to encode URL. Bad luck");
        }


        String getItemsUrl;

        if (bool) {
            getItemsUrl = "http://republic.tk/api/listview/filter/" + encodeCity + "/" + any + "/2";
        }

        else {
            getItemsUrl = "http://republic.tk/api/listview/filter/" + encodeCity + "/" + any + "/3";
        }


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt(Constants.TAG_SUCCESS);
                    if (success == 1) {
                        JSONArray places = response.getJSONArray(Constants.TAG_ITEMS);

                        for (int i = 0; i < places.length(); i++) {
                            JSONObject nearPlace = places.getJSONObject(i);
                            JSONObject locations = nearPlace.getJSONObject(Constants.TAG_LOCATION);
                            ModelNearPlace place = new ModelNearPlace();
                            place.setId(nearPlace.getString(Constants.TAG_PID));
                            place.setTitle(nearPlace.getString(Constants.TAG_NAME));
                            place.setLat(locations.getDouble(Constants.TAG_LAT));
                            place.setLng(locations.getDouble(Constants.TAG_LNG));

                            modelNearPlaces.add(place);
                        }
                        setAdapters(modelNearPlaces, bool);
                        modelNearPlaces.clear();

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.displayErrors(textVolleyError, error);
            }
        });

        queue.add(jsonObjectRequest);


    }




    private void setAdapters (List<ModelNearPlace> modelNearPlaces, boolean cafeOrHotel) {
        //вычисляем расстояние
        Map<Integer, Integer> floatDistances = new HashMap<>();
        for (int i = 0; i < modelNearPlaces.size(); i++) {
            ModelNearPlace currentPlace = modelNearPlaces.get(i);
            Location locationA = new Location("pointA");
            locationA.setLatitude(lat);
            locationA.setLongitude(lng);
            Location locationB = new Location("pointB");
            locationB.setLatitude(currentPlace.getLat());
            locationB.setLongitude(currentPlace.getLng());

            int distance = (int) locationA.distanceTo(locationB);
            currentPlace.setDistance(distance);
            if (distance > 5 && distance < 3000)
                floatDistances.put(Integer.valueOf(currentPlace.getId()), distance);
        }

        Map<Integer, Integer> sortedMap = Constants.sortMapByValue(floatDistances);


        List<Integer> listIdNear = new ArrayList<>();

        for (Integer key : sortedMap.keySet()) {
            listIdNear.add(key);
        }

        if (listIdNear.size() > 0) {
            linearLayout.setVisibility(View.VISIBLE);
            nearTV.setVisibility(View.VISIBLE);
        }


        if (cafeOrHotel) {
            for (int i = 0; i < listIdNear.size(); i++) {
                if ( listIdNear.get(i) == Integer.parseInt(modelNearPlaces.get(i).getId())) {
                    fiveLastNearCafe.add(modelNearPlaces.get(i));
                }
            }
            if (fiveLastNearCafe.size() > 0) {
                cafeRV.setVisibility(View.VISIBLE);
            }

            Log.d("hren", String.valueOf(fiveLastNearCafe.size()) + "cafe");

            RecyclerAdapterEatSleep adapterEat = new RecyclerAdapterEatSleep(this, fiveLastNearCafe);
            cafeRV.setAdapter(adapterEat);



            cafeRV.addOnItemTouchListener(new RecyclerClickListener(this, cafeRV, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(OpenPlaceActivity.this, OpenPlaceActivity.class);
                    intent.putExtra(ModelNearPlace.class.getCanonicalName(), fiveLastNearCafe.get(position).getId());
                    intent.putExtra("title", fiveLastNearCafe.get(position).getTitle());
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }

        else {

            for (int i = 0; i < listIdNear.size(); i++) {
                if ( listIdNear.get(i) == Integer.parseInt(modelNearPlaces.get(i).getId())) {
                    fiveLastNearHotels.add(modelNearPlaces.get(i));
                }
            }

            ModelNearPlace modelNearPlace2 = new ModelNearPlace();
            modelNearPlace2.setDistance(5);
            modelNearPlace2.setTitle("Хуяк хуяк и в продакшн");
            fiveLastNearHotels.add(modelNearPlace2);

            if (fiveLastNearHotels.size() > 0) {
                hotelRV.setVisibility(View.VISIBLE);
            }


            Log.d("hren", String.valueOf(fiveLastNearHotels.size()) + "hotel");

            RecyclerAdapterEatSleep adapterSleep = new RecyclerAdapterEatSleep(this, fiveLastNearHotels);
            hotelRV.setAdapter(adapterSleep);

            adapterSleep.notifyDataSetChanged();

           hotelRV.addOnItemTouchListener(new RecyclerClickListener(this, hotelRV, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(OpenPlaceActivity.this, OpenPlaceActivity.class);
                    intent.putExtra(ModelNearPlace.class.getCanonicalName(), fiveLastNearHotels.get(position).getId());
                    intent.putExtra("title", fiveLastNearHotels.get(position).getTitle());
                    startActivity(intent);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }

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

    private void settingTheme() {
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
    }

    private ModelPlace getDataFromDB() {
        DBHelper dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        ModelPlace place = new ModelPlace();

        String selectQuery = "SELECT " + DBHelper.KEY_DESCRIPTION + ", "
                + DBHelper.KEY_LAT + ", " + DBHelper.KEY_LNG +
                " FROM " + DBHelper.TABLE_PLACES + " WHERE "
                + DBHelper.KEY_PLACE_ID + " = " + id;

        //Сначала смотрим есть ли место в бд
        Cursor c = database.rawQuery(selectQuery, null);
        if(c.moveToFirst()){

            String desc = c.getString(c.getColumnIndex(DBHelper.KEY_DESCRIPTION));
            double lat = Double.parseDouble(c.getString(c.getColumnIndex(DBHelper.KEY_LAT)));
            double lng = Double.parseDouble(c.getString(c.getColumnIndex(DBHelper.KEY_LNG)));

            place.setDescription(desc);
            place.setLat(lat);
            place.setLng(lng);

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
            return place;
        }

        else return null;
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

    private void setFavoriteListener(boolean hasDb) {
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
                        } else {
                            database.delete(DBHelper.TABLE_PLACES, DBHelper.KEY_PLACE_ID + " = ? ", new String[]{id});
                            database.delete(DBHelper.TABLE_IMAGES, DBHelper.KEY_PLACE_ID + " = ? ", new String[]{id});

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

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

}
