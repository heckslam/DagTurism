package ru.devtron.dagturism;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.devtron.dagturism.adapter.ImageGaleryRecyclerAdapter;
import ru.devtron.dagturism.model.ModelPlace;

public class OpenPlaceActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_open_place;

    GoogleMap map;
    SupportMapFragment mapFragment;
    private Toolbar toolbar;
    private ModelPlace parcelWithPlace;
    private TextView cityTitle;
    private String city, title;
    private long id;
    private List<String> arrayImages = new ArrayList<>();

    ViewPager viewPager;
    ImageGaleryRecyclerAdapter adapterImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        viewPager = (ViewPager) findViewById(R.id.viewPagerForImages);
        cityTitle = (TextView) findViewById(R.id.cityTitle);

        getPlaceFromActivity();

        initToolbar();

        initMap();
    }

    private void getPlaceFromActivity() {
        parcelWithPlace = (ModelPlace) getIntent().getParcelableExtra(ModelPlace.class.getCanonicalName());

        title = parcelWithPlace.getTitle();
        city = parcelWithPlace.getCity();
        id = Long.parseLong(parcelWithPlace.getId());
        arrayImages = parcelWithPlace.getImages();

        cityTitle.setText(city);

        adapterImages = new ImageGaleryRecyclerAdapter(this, arrayImages);

        viewPager.setAdapter(adapterImages);
        viewPager.setCurrentItem(ImageGaleryRecyclerAdapter.PAGER_PAGES_MIDDLE);

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

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();

        if (map != null) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(42.966667, 47.500000))
                    .zoom(15)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);
            map.addMarker(new MarkerOptions().position(new LatLng(42.966667, 47.500000)));
            map.getUiSettings().setAllGesturesEnabled(false);
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
}
