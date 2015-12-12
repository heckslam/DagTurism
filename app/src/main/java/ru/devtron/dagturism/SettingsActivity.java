package ru.devtron.dagturism;


import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;

import ru.devtron.dagturism.abstract_classes.AbstractMethodsActivity;

/**
 * Активность настроек приложения
 *
 * @created 15.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.2
 */

public class SettingsActivity extends AbstractMethodsActivity implements CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switchSplash, switchAnimation;
    private PreferenceHelper preferenceHelper;
    private static final int LAYOUT = R.layout.activity_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar(R.string.activity_settings_title);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        switchSplash = (SwitchCompat) findViewById(R.id.switchSplash);
        switchAnimation = (SwitchCompat) findViewById(R.id.switchAnimation);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();


        switchSplash.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_VISIBLE));

        switchSplash.setOnCheckedChangeListener(this);
        switchAnimation.setOnCheckedChangeListener(this);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchSplash:
                switchSplash.setChecked(switchSplash.isChecked());
                preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_VISIBLE, switchSplash.isChecked());
                break;
            case R.id.switchAnimation:
                Log.d("switch_compat", isChecked + " 2");
                break;
        }
    }
}
