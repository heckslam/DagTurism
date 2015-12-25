package ru.devtron.dagturism;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

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

    private static Context context;

    BroadcastReceiver networkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingTheme();
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        MainActivity.context = getApplicationContext();

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            runSplash();
        }



        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkStateReceiver, filter);


        initToolbar();
        initNavigationView();
        initTabs();
    }


   @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networkStateReceiver != null) {
            this.unregisterReceiver(networkStateReceiver);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }
    }

    private static void setToolbarTitle(int resId) {
        toolbar.setTitle(resId);
    }


    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsFragmentAdapter adapter = new TabsFragmentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void runSplash () {
        if (sp.getBoolean("splashEnabled", true)) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }

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

    protected static void checkConnection(boolean isConnected) {
        if (isConnected) {
            setToolbarTitle(R.string.app_name);
        }
        else {
            setToolbarTitle(R.string.wait_network);
            Toast toast = Toast.makeText(MainActivity.context,
                    R.string.no_network, Toast.LENGTH_LONG);
            toast.show();
        }
    }

}

class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
           MainActivity.checkConnection(NetworkUtil.getConnectivityStatusString(context));
    }
}