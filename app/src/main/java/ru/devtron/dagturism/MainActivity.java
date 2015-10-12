package ru.devtron.dagturism;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends TabActivity{
    private Toolbar toolbar;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initTabs();
       // setTabColor(tabHost);
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
     private void initTabs() {
         //создаем табы
         TabHost tabHost = getTabHost();
         TabHost.TabSpec tabSpec;
         tabSpec =   tabHost.newTabSpec("tag1");
         tabSpec.setIndicator("Список");
         tabSpec.setContent(new Intent(this, arrayActivity.class));

         tabHost.addTab(tabSpec);
         tabSpec =   tabHost.newTabSpec("tag2");
         tabSpec.setIndicator("Карта");
         tabSpec.setContent(new Intent(this, mapsActivity.class));
         tabHost.addTab(tabSpec);

     }
    public static void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.colorPrimary); //unselected
        }
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.color.colorIndicatorsTabs); // selected
    }
}
