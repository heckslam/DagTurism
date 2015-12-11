package ru.devtron.dagturism.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.devtron.dagturism.fragment.PlacesFilteredFragment;
import ru.devtron.dagturism.fragment.WhereToEat;
import ru.devtron.dagturism.fragment.WhereToSleep;

/**
 * Created by Ruslan Aliev on 28.11.2015.
 */
public class TabsFragmentAdapterFilteredActivity extends FragmentPagerAdapter {

    private String[] tabs;
    private String city;
    private String rest;


    public TabsFragmentAdapterFilteredActivity(FragmentManager fm, String city, String rest) {
        super(fm);
        tabs = new String[] {
                "Места",
                "Где поесть",
                "Где поспать"
        };

        this.city = city;
        this.rest = rest;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    /**
     * Устанавливает фрагменты табам
     * @param position
     * @return
     */

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PlacesFilteredFragment.createInstance(3, city, rest);

            case 1:
                return WhereToEat.createInstance(4, city, rest);

            case 2:
                return WhereToSleep.createInstance(5, city, rest);

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
