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

import ru.devtron.dagturism.MainActivity;
import ru.devtron.dagturism.ViewListPlacesActivity;
import ru.devtron.dagturism.R;
import ru.devtron.dagturism.SettingsActivity;


public abstract class AbstractMethodsActivity extends AppCompatActivity {

    protected SharedPreferences sp;

    protected static Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected ViewPager viewPager;

    // JSON Node names
    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ITEMS = "items";
    protected static final String TAG_PID = "place_id";
    protected static final String TAG_NAME = "place_name";
    protected static final String TAG_CITY = "place_city";
    protected static final String TAG_IMAGES = "images";

    protected void settingTheme() {
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
                    case R.id.places:
                        goTOViewList(1);
                        break;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case R.id.eat:
                        goTOViewList(2);
                        break;
                    case R.id.sleep:
                        goTOViewList(3);
                        break;
                    case R.id.deep_search:
                        startActivity(new Intent(getApplicationContext(), ViewListPlacesActivity.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void goTOViewList (int selected) {
        Intent intentSleep = new Intent(getApplicationContext(), ViewListPlacesActivity.class);
        intentSleep.putExtra("selectedItem", selected);
        startActivity(intentSleep);
    }




}
