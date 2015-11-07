package ru.devtron.dagturism.adapterNavBar;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.devtron.dagturism.fragment.ArrayFragment;
import ru.devtron.dagturism.fragment.MapsFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Популярные",
                "Карта"
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ArrayFragment.createInstance(20);
            case 1:
                return MapsFragment.createInstance(0);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
