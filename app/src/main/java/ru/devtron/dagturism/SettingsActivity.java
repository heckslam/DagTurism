package ru.devtron.dagturism;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import ru.devtron.dagturism.R;

/**
 * Активность настроек приложения
 *
 * @created 15.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.2
 */

public class SettingsActivity extends AppCompatActivity  {
     private SwitchCompat switchSplash, switchAnimation;
    PreferenceHelper preferenceHelper;
    private static final int LAYOUT = R.layout.activity_settings;
    FragmentManager fragmentManager;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        fragmentManager = getFragmentManager();

        initToolbar();
        initNavigationView();

        switchSplash = (SwitchCompat) findViewById(R.id.switchSplash);
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();

        if (preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_VISIBLE)) {
            switchSplash.setChecked(true);
        }
        else switchSplash.setChecked(false);
    }

    public void onEnableSplash(View v) {
        Intent intent = new Intent();
        intent.putExtra("switchSplash", switchSplash.isClickable());
        setResult(RESULT_OK, intent);
        finish();
    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle(R.string.activity_settings_title);
            setSupportActionBar(toolbar);
        }
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
                        //openCat();
                        break;
                    case R.id.settings:
                        //openSettings();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
