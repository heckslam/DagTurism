package ru.devtron.dagturism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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

public class SettingsActivity extends AppCompatActivity {
    private CheckBox enableSplash;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        enableSplash = (CheckBox) findViewById(R.id.enableSplash);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();

        if (preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_VISIBLE)) {
            enableSplash.setChecked(true);
        }
        else enableSplash.setChecked(false);
    }

    public void onEnableSplash(View v) {
        Intent intent = new Intent();
        intent.putExtra("enableSplash", enableSplash.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }




}
