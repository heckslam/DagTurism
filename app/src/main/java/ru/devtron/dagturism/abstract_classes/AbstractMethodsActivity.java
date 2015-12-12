package ru.devtron.dagturism.abstract_classes;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.devtron.dagturism.CategoriesActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.SettingsActivity;


public abstract class AbstractMethodsActivity extends AppCompatActivity {


    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected ViewPager viewPager;


    protected void initToolbar(int resId) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle(resId);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }
    }

    protected void initNavigationView() {
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

    protected void openCat () {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }


    protected void openSettings () {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }




}
