package ru.devtron.dagturism.adapterNavBar;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.devtron.dagturism.fragment.arrayFragment;
import ru.devtron.dagturism.fragment.mapsFragment;

/**
 *Адаптер для работы с табами. Хранит назвнаия табов, их обработчики на нажатие
 *
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */

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

    /**
     * Устанавливает фрагменты табам
     * @param position
     * @return
     */

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return arrayFragment.getInstance();
            case 1:
                return mapsFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
