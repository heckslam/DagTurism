package ru.devtron.dagturism;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ru.devtron.dagturism.abstract_classes.AbstractMethodsActivity;
import ru.devtron.dagturism.adapter.TabsFragmentAdapterFilteredActivity;

public class FilteredActivity extends AbstractMethodsActivity {

    private String cityOrTown, selectedRest;
    private TextView toolbarCityTV;
    private TextView toolbarRestTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered);


        toolbarCityTV = (TextView) findViewById(R.id.toolbarCityTV);
        toolbarRestTV = (TextView) findViewById(R.id.toolbarRestTV);

        cityOrTown = getIntent().getExtras().getString("cityOrTown");
        selectedRest = getIntent().getExtras().getString("selectedRest");

        initToolbar();
        initNavigationView();
        initTabs();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbarCityTV.setText(cityOrTown);
            toolbarRestTV.setText(selectedRest);
            setSupportActionBar(toolbar);
        }
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapterFilteredActivity adapter = new TabsFragmentAdapterFilteredActivity(this, getSupportFragmentManager(), cityOrTown, selectedRest);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


}
