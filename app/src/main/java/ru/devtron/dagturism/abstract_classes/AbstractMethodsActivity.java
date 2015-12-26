package ru.devtron.dagturism.abstract_classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.devtron.dagturism.CategoriesActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.settings.SettingsActivity;


public abstract class AbstractMethodsActivity extends AppCompatActivity {

    protected SharedPreferences sp;

    protected static Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected ViewPager viewPager;

    protected void settingTheme() {
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
