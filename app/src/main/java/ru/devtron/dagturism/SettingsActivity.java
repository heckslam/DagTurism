package ru.devtron.dagturism;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import ru.devtron.dagturism.abstract_classes.AppCompatPreferenceActivity;
import ru.devtron.dagturism.fragment.SettingsFragment;


public class SettingsActivity extends AppCompatPreferenceActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedTheme = sp.getString("selectedTheme", "1");
        int selectedThemeValue = Integer.parseInt(selectedTheme);
        switch (selectedThemeValue) {
            case 1:
                setTheme(R.style.AppDefault);
                break;
            case 2:
                setTheme(R.style.AppOrange);
                break;
            case 3:
                setTheme(R.style.AppPurple);
                break;
            case 4:
                setTheme(R.style.AppGrey);
                break;
        }

        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
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







}
