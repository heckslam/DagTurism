package ru.devtron.dagturism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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




}
