package ru.devtron.dagturism;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import ru.devtron.dagturism.abstract_classes.AbstractMethodsActivity;
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

    protected void runSplash () {
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
