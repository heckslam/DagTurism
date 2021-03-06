package ru.devtron.dagturism;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.List;

import ru.devtron.dagturism.adapter.FullGalleryAdapter;
import ru.devtron.dagturism.model.ModelGallery;

public class OpenFullGalleryActivity extends Activity {

    private ViewPager mViewPager;
    FullGalleryAdapter adapterImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setTheme(android.R.style.Theme_Material_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_full_gallery);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        getPlaceFromActivity();
    }




    private void getPlaceFromActivity() {
        ModelGallery parcelWithPlace = getIntent().getParcelableExtra(ModelGallery.class.getCanonicalName());
        List<String> arrayImages = parcelWithPlace.getImages();
        int position = getIntent().getIntExtra("position", 0);

        adapterImages = new FullGalleryAdapter(this, arrayImages);
        mViewPager.setAdapter(adapterImages);
        mViewPager.setCurrentItem(position);
    }


}