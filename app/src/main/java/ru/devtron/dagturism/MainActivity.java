package ru.devtron.dagturism;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import ru.devtron.dagturism.abstract_classes.AbstractMethodsActivity;
import ru.devtron.dagturism.adapter.TabsFragmentAdapter;
import ru.devtron.dagturism.dialog.SearchPlaceDialogFragment;
import ru.devtron.dagturism.fragment.SplashFragment;

/**
 * Стартовая активность приложения
 *
 * @created 07.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.1
 */

public class MainActivity extends AbstractMethodsActivity
        implements SearchPlaceDialogFragment.SearchPlaceListener {

    private static final int LAYOUT = R.layout.activity_main;

    private FragmentManager fragmentManager;

    private PreferenceHelper preferenceHelper;

    BroadcastReceiver networkStateReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        fragmentManager = getFragmentManager();
        runSplash();



        initToolbar();
        initNavigationView();
        initTabs();
    }


/*    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networkStateReceiver != null) {
            this.unregisterReceiver(networkStateReceiver);
        }
    }*/

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
    }

/*    private void checkConnection(boolean IsConnected) {
        if (IsConnected) {
            toolbar.setTitle(R.string.app_name);
        }
        else  {
            toolbar.setTitle(R.string.wait_network);
        }
    }*/



    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void runSplash () {
        if (preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_VISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }



    @Override
    public void onSearchStarted(String cityOrTown, String selectedRest) {
        Intent intent = new Intent(MainActivity.this, FilteredActivity.class);
        intent.putExtra("cityOrTown", cityOrTown);
        intent.putExtra("selectedRest", selectedRest);
        startActivity(intent);
    }

    @Override
    public void onSearchCanceled() {

    }



}
