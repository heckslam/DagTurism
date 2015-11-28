package ru.devtron.dagturism.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.devtron.dagturism.fragment.ArrayFragment;
import ru.devtron.dagturism.fragment.MapsFragment;
import ru.devtron.dagturism.fragment.PlacesFilteredFragment;
import ru.devtron.dagturism.fragment.WhereToEat;

/**
 * Created by Ruslan Aliev on 28.11.2015.
 */
public class TabsPagerFragmentAdapterFilteredActivity extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapterFilteredActivity(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Места",
                "Где поесть",
                "Где поспать"
        };
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
                return PlacesFilteredFragment.createInstance(20);

            case 1:
                return WhereToEat.createInstance(0);

            case 2:
                return WhereToEat.createInstance(2);

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
