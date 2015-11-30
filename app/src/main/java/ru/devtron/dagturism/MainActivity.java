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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.devtron.dagturism.adapter.TabsPagerFragmentAdapter;

import ru.devtron.dagturism.dialog.SearchPlaceDialogFragment;
import ru.devtron.dagturism.fragment.SplashFragment;
import ru.devtron.dagturism.model.ModelPlace;

/**
 * Стартовая активность приложения
 *
 * @created 07.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.1
 */

public class MainActivity extends AppCompatActivity
        implements SearchPlaceDialogFragment.SearchPlaceListener {

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
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        fragmentManager = getFragmentManager();
        runSplash();
        initToolbar();
        initNavigationView();
        initTabs();
        initFab();

        //makingRequest();


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
                    boolean splash = data.getBooleanExtra("switchSplash", true);
                    preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_VISIBLE, splash);
                    break;
                default:
                    break;
            }
        }
        else {
            Toast.makeText(this, "Hello from Settings!", Toast.LENGTH_SHORT).show();
        }
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
        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
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

    /*private void makingRequest() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getItemsUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int success = response.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        places = response.getJSONArray(TAG_ITEMS);

                        for (int i = 0; i < places.length(); i++) {
                            JSONObject c = places.getJSONObject(i);
                            String id = c.getString(TAG_PID);
                            String name = c.getString(TAG_NAME);
                            Log.d("MyTag", id);
                            Log.d("MyTag", name);

                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("key", "asdf");
                parameters.put("method", "getListView");
                return parameters;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
*/

    @Override
    public void onSearchStarted(String cityOrTown, String selectedRest) {
        Intent intent = new Intent(MainActivity.this, FilteredActivity.class);
        intent.putExtra("cityOrTown", cityOrTown);
        intent.putExtra("selectedRest", selectedRest);
        startActivity(intent);
    }

    @Override
    public void onSearchCanceled() {
        Toast.makeText(this, "Поиск мест по районам отменен", Toast.LENGTH_SHORT).show();
    }

}
