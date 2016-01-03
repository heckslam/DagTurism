package ru.devtron.dagturism.Utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Работа с SharedPreference
 * функции для обработки данных о настройках от пользователя.
 *
 * @created 20.10.2015
 * @version $Revision 738 $
 * @author AlievRuslan
 * since 0.0.3
 */

public class PreferenceHelper {
    public static final String SPLASH_IS_VISIBLE = "splash_is_visible";

    private static PreferenceHelper instance;
    private Context context;
    private SharedPreferences preferences;

    private PreferenceHelper() {

    }

    public static PreferenceHelper getInstance() {
        if (instance == null) {
            instance = new PreferenceHelper();
        }

        return instance;
    }

    public void init(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, true);
    }
}
