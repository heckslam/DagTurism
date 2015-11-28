package ru.devtron.dagturism.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.devtron.dagturism.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFilteredFragment extends Fragment {
    public final static String ITEMS_COUNT_KEY = "PlacesFilteredFragment$ItemsCount";

    public static PlacesFilteredFragment createInstance(int itemsCount){
        Bundle args = new Bundle();
        args.putInt(ITEMS_COUNT_KEY, itemsCount);
        PlacesFilteredFragment placesFilteredFragment = new PlacesFilteredFragment();
        placesFilteredFragment.setArguments(args);
        return placesFilteredFragment;
    }


    public PlacesFilteredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_filtered, container, false);
    }

}
