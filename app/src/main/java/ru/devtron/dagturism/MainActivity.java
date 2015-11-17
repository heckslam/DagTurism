package ru.devtron.dagturism;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.devtron.dagturism.adapter.TabsPagerFragmentAdapter;

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

public class MainActivity extends AppCompatActivity
        implements SearchPlaceDialogFragment.AddingTaskListener{

    private static final int LAYOUT = R.layout.activity_main;
    FragmentManager fragmentManager;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;

    PreferenceHelper preferenceHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        initToolbar();
        initNavigationView();
        initTabs();
        initFab();

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();

        fragmentManager = getFragmentManager();
        runSplash();



    }



    /**
     * Получаем данные из активити настроек SettingsActivity для возможности отключения SplashScreen
     *
     *  <p>@param  requestCode</p>
     *  <p>@param  resultCode</p>
     *  <p>@param  data</p>
     *
     *  @return Toast
     * since 0.0.3
     *
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_SETTINGS:
                    boolean splash = data.getBooleanExtra("enableSplash", true);
                    preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_VISIBLE, splash);
                    break;
                default:
                    break;
            }
        }
        else {
            Toast.makeText(this, "Hello Tagir!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
     //   toolbar.isOverflowMenuShowing();


    }

    private void initTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
   /* private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(PartThreeFragment.createInstance(20), getString(R.string.tab_1));
        pagerAdapter.addFragment(PartThreeFragment.createInstance(4), getString(R.string.tab_2));
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
*/
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
        startActivityForResult(intent, Constants.REQUEST_CODE_SETTINGS);
    }

    /**
     * Метод для запуска SplashScreen
     */

    public void runSplash () {
        if (preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_VISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment searchPlaceDialogFragment = new SearchPlaceDialogFragment();
                searchPlaceDialogFragment.show(fragmentManager, "SearchPlaceDialogFragment");
            }
        });
    }



    @Override
    public void onSearchStarted() {
        Toast.makeText(this, "Поиск начался, переходим на новое активити", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchCanceled() {
        Toast.makeText(this, "Поиск мест по районам отменен", Toast.LENGTH_SHORT).show();
    }
}
