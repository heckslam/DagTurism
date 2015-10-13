package ru.devtron.dagturism;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity{
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        //initTabs();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);

    }
//
//     private void initTabs() {
//         //создаем табы
//         TabHost tabHost = getTabHost();
//         TabHost.TabSpec tabSpec;
//         tabSpec =   tabHost.newTabSpec("tag1");
//         tabSpec.setIndicator("Список");
//         tabSpec.setContent(new Intent(this, arrayActivity.class));
//
//         tabHost.addTab(tabSpec);
//         tabSpec =   tabHost.newTabSpec("tag2");
//         tabSpec.setIndicator("Карта");
//         tabSpec.setContent(new Intent(this, mapsActivity.class));
//         tabHost.addTab(tabSpec);
//
//     }

}
