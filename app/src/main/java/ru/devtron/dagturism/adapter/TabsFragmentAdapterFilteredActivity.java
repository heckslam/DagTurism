package ru.devtron.dagturism.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ru.devtron.dagturism.abstract_classes.AbstractTabFilterFragment;
import ru.devtron.dagturism.fragment.PlacesFilteredFragment;
import ru.devtron.dagturism.fragment.WhereToEat;
import ru.devtron.dagturism.fragment.WhereToSleep;

/**
 * Created by Ruslan Aliev on 28.11.2015.
 */
public class TabsFragmentAdapterFilteredActivity extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFilterFragment> tabs;
    private Context context;

    private String city;
    private String rest;


    public TabsFragmentAdapterFilteredActivity(Context context, FragmentManager fm, String city, String rest) {
        super(fm);
        this.city = city;
        this.rest = rest;
        this.context = context;
        initTabsMap(context, city, rest);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }


    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);

    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabsMap(Context context, String city, String rest) {
        tabs = new HashMap<>();
        tabs.put(0, PlacesFilteredFragment.getInstance(context, city, rest));
        tabs.put(1, WhereToEat.getInstance(context, city, rest));
        tabs.put(2, WhereToSleep.getInstance(context, city, rest));
    }
}
