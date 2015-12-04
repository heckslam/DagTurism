package ru.devtron.dagturism;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import ru.devtron.dagturism.adapter.TabsPagerFragmentAdapter;
import ru.devtron.dagturism.adapter.TabsPagerFragmentAdapterFilteredActivity;

public class FilteredActivity extends AppCompatActivity {

    private String cityOrTown, selectedRest;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    RequestQueue requestQueue;
    private final String getItemsUrl = "http://republic.tk/index.php/api/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered);

        initToolbar();
        initNavigationView();
        initTabs();
        getDataFromMainActivity();
    }

    private void getDataFromMainActivity() {
        cityOrTown = getIntent().getExtras().getString("cityOrTown");
        selectedRest = getIntent().getExtras().getString("selectedRest");
        Toast.makeText(this, "Поиск начался" + cityOrTown + "\nВид отдыха - " + selectedRest, Toast.LENGTH_SHORT).show();

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsPagerFragmentAdapterFilteredActivity adapter = new TabsPagerFragmentAdapterFilteredActivity(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.categories:
                        openCat();
                        break;
                    case R.id.settings:
                        openSettings();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Метод для открытия активности категорий CategoriesActivity
     * В NavigationDrawer
     */
    public void openCat () {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    /**
     * Метод для открытия активности настроек SettingsActivity
     * В NavigationDrawer
     */

    public void openSettings () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
