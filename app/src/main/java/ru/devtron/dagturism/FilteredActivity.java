package ru.devtron.dagturism;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import ru.devtron.dagturism.abstract_classes.AbstractMethodsActivity;
import ru.devtron.dagturism.adapter.TabsFragmentAdapterFilteredActivity;

public class FilteredActivity extends AbstractMethodsActivity {

    public String cityOrTown, selectedRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered);

        cityOrTown = getIntent().getExtras().getString("cityOrTown");
        selectedRest = getIntent().getExtras().getString("selectedRest");

        initToolbar(R.string.app_name);
        initNavigationView();
        initTabs();
    }

    protected void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapterFilteredActivity adapter = new TabsFragmentAdapterFilteredActivity(this, getSupportFragmentManager(), cityOrTown, selectedRest);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


}
