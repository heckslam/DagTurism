package ru.devtron.dagturism.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.devtron.dagturism.R;
import ru.devtron.dagturism.settings.AppCompatPreferenceActivity;

/**
 * Created by user on 23.12.2015.
 */

public class SettingsFragment extends PreferenceFragment {
    ListPreference listPreference;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        listPreference = (ListPreference) findPreference("selectedTheme");
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast toast = Toast.makeText(getActivity(),
                        "Чтобы настройки вступили в силу, необходимо перезапустить приложение", Toast.LENGTH_LONG);
                toast.show();

                return true;
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_settings, container, false);
        if (layout != null) {
            AppCompatPreferenceActivity activity = (AppCompatPreferenceActivity) getActivity();
            Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);

            ActionBar bar = activity.getSupportActionBar();
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            bar.setTitle(R.string.activity_settings_title);
        }
        return layout;
    }






}
