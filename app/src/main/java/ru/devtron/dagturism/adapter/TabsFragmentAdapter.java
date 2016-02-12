package ru.devtron.dagturism.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import ru.devtron.dagturism.abstract_classes.AbstractTabFragment;
import ru.devtron.dagturism.fragment.FavoritesFragment;
import ru.devtron.dagturism.fragment.PopularFragment;
import ru.devtron.dagturism.fragment.RootFragment;

/**
 *Адаптер для работы с табами. Хранит назвнаия табов, их обработчики на нажатие
 *
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initTabsMap(context);
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

    private void initTabsMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, RootFragment.getInstance(context));
        tabs.put(1, FavoritesFragment.getInstance(context));
    }
}
