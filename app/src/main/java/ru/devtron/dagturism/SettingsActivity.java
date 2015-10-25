package ru.devtron.dagturism;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }



    public void onCheckboxChecked(View view) {
        boolean isChecked = ((CheckBox)view).isChecked();

        switch (view.getId()) {
            case R.id.enableSplash:
                if (isChecked) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Заставка включена",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Заставка выключена",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.enableAnimation:
                break;
            default:
                break;
        }
    }
}
