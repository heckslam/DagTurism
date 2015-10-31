package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.devtron.dagturism.R;
/**
 * Фрагмент который загружается в MainActivity  при нажатии на таб Карты и загружает данные в него
 * @created 10.10.2015
 * @version $Revision 738 $
 * @author Эльвира Темирханова
 * since 0.0.1
 */

public class mapsFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment_maps;

    private View view;

    public static  mapsFragment getInstance() {
        Bundle args = new Bundle();
        mapsFragment fragment = new  mapsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }
}