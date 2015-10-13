package ru.devtron.dagturism;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    private static final int MAINLAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(MAINLAYOUT);
        initToolbar();
        initNavigationView();
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

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
